// products-page.js -> GET /api/products -> products_controller -> catalog_service -> list<ProductDto> -> JSON -> inapoi in JS

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

    async function load_products() {
        const grid = document.getElementById("products-grid");
        const err = document.getElementById("products-error");
        if (!grid) return;

        try {
            const res = await fetch(`${api_base}/api/products`);
            if (!res.ok) throw new Error("api error: " + res.status);

            const products = await res.json();

            if (!Array.isArray(products) || products.length === 0) {
                grid.innerHTML = `<p style="color:#8e9867;">No products returned by api.</p>`;
                return;
            }

            grid.innerHTML = products.map(p => `
        <div class="product__card">
          <img src="${escape_html(p.image || `assets/images/${p.id}.jpg`)}" alt="${escape_html(p.name)}">
          <h3>${escape_html(p.name)}</h3>
          <p>${Number(p.price).toFixed(2)} lei / ${escape_html(p.unit || "")}</p>

          <button
            data-add-to-cart
            data-type="product"
            data-id="${escape_html(p.id)}"
            data-name="${escape_html(p.name)}"
            data-price="${escape_html(p.price)}"
            data-unit="${escape_html(p.unit || "")}">
            Add to cart
          </button>
        </div>
      `).join("");

            if (typeof hookAddToCartButtons === "function") hookAddToCartButtons();
            if (typeof updateCartBadge === "function") updateCartBadge();

        } catch (e) {
            console.error(e);
            if (err) {
                err.style.display = "block";
                err.textContent = "Cannot load products. Check: api on localhost:8080 + products-page.js loaded.";
            }
        }
    }

    document.addEventListener("DOMContentLoaded", load_products);
})();
