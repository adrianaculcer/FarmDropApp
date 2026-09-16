(() => {
    const cart_key = "farmdrop_cart";

    function get_cart() {
        return JSON.parse(localStorage.getItem(cart_key)) || [];
    }

    function save_cart(cart) {
        localStorage.setItem(cart_key, JSON.stringify(cart));
    }

    function updateCartBadge() {
        const badge = document.getElementById("cart-count");
        if (!badge) return;

        const cart = get_cart();
        const count = cart.reduce((sum, item) => sum + (item.qty ?? 1), 0);
        badge.textContent = String(count);
    }

    function addToCart(item) {
        const cart = get_cart();

        const existing = cart.find(x => x.type === item.type && x.id === item.id);
        if (existing) existing.qty = (existing.qty ?? 1) + 1;
        else cart.push({ ...item, qty: 1 });

        save_cart(cart);
        updateCartBadge();
    }

    function hookAddToCartButtons(root = document) {
        root.querySelectorAll("[data-add-to-cart]").forEach(btn => {
            btn.addEventListener("click", () => {
                const item = {
                    type: btn.dataset.type,
                    id: btn.dataset.id,
                    name: btn.dataset.name,
                    price: Number(btn.dataset.price),
                    unit: btn.dataset.unit || null
                };
                addToCart(item);
            });
        });
    }

    // expunem global (ca sa le poata folosi products-page.js si boxes-page.js)
    window.getCart = get_cart;
    window.saveCart = save_cart;
    window.updateCartBadge = updateCartBadge;
    window.addToCart = addToCart;
    window.hookAddToCartButtons = hookAddToCartButtons;

    document.addEventListener("DOMContentLoaded", () => {
        updateCartBadge();
    });
})();
