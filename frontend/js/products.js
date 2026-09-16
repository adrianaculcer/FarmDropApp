const api_base = "http://localhost:8080";

async function load_products() {
    const container = document.getElementById("products-list");
    if (!container) return;

    try {
        const res = await fetch(`${api_base}/api/products`);
        const products = await res.json();

        container.innerHTML = products.map(p => `
      <article class="product-card">
        <img class="product-card__img" src="assets/images/${p.id}.jpg" alt="${p.name}">
        <h3>${p.name}</h3>
        <p>${p.price} lei / ${p.unit}</p>

        <button class="add-btn"
          data-add-to-cart
          data-type="product"
          data-id="${p.id}"
          data-name="${p.name}"
          data-price="${p.price}"
          data-unit="${p.unit}">
          Add to cart
        </button>
      </article>
    `).join("");

        // IMPORTANT: dupa ce ai generat butoanele, le “legam”
        if (typeof hookAddToCartButtons === "function") {
            hookAddToCartButtons();
        }

    } catch (e) {
        console.error("cannot load products", e);
        container.innerHTML = `<p>Could not load products.</p>`;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    load_products();
});
