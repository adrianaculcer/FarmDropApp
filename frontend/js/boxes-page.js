(() => {
    const api_base = "http://localhost:8080";

    function escape_html(str) {
        return String(str)
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }

    function class_by_box_id(id) {
        if (id === "box-soup-m") return "box--soup";
        if (id === "box-salad-m") return "box--salad";
        return "";
    }

    function format_qty(qty, unit) {
        const num = Number(qty);
        const shown = Number.isInteger(num) ? String(num) : String(num);
        return `${shown} ${unit || ""}`.trim();
    }

    async function load_boxes() {
        const grid = document.getElementById("boxes-grid");
        const err = document.getElementById("boxes-error");
        if (!grid) return;

        try {
            const res = await fetch(`${api_base}/api/boxes`);
            if (!res.ok) throw new Error("api error: " + res.status);

            const boxes = await res.json();

            if (!Array.isArray(boxes) || boxes.length === 0) {
                grid.innerHTML = `<p style="color:#8e9867;">No boxes returned by api.</p>`;
                return;
            }

            grid.innerHTML = boxes.map(b => {
                const items_html = (b.items || []).map(it => `
          <li>${escape_html(it.name)} ${escape_html(format_qty(it.quantity, it.unit))}</li>
        `).join("");

                return `
          <article class="box-card ${class_by_box_id(b.id)}">
            <div class="box-card__img">
              <img src="${escape_html(b.image || "")}" alt="${escape_html(b.name)}">
            </div>

            <div class="box-card__body">
              <h3>${escape_html(b.name)}</h3>
              <p class="box-card__subtitle">${escape_html(b.subtitle || "")}</p>

              <ul class="box-card__list">
                ${items_html}
              </ul>

              <div class="box-card__bottom">
                <div class="box-card__price">
                  <span class="price__big">${Number(b.price).toFixed(0)} lei</span>
                  <span class="price__small">includes packaging</span>
                </div>

                <button class="box-card__btn"
                        data-add-to-cart
                        data-type="box"
                        data-id="${escape_html(b.id)}"
                        data-name="${escape_html(b.name)}"
                        data-price="${escape_html(b.price)}">
                  Add to cart
                </button>
              </div>
            </div>
          </article>
        `;
            }).join("");

            if (typeof hookAddToCartButtons === "function") hookAddToCartButtons();
            if (typeof updateCartBadge === "function") updateCartBadge();

        } catch (e) {
            console.error(e);
            if (err) {
                err.style.display = "block";
                err.textContent = "Cannot load boxes. Check: api on localhost:8080 + boxes-page.js loaded.";
            }
        }
    }

    document.addEventListener("DOMContentLoaded", load_boxes);
})();
