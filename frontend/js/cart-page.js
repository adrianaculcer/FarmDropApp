const api_base = "http://localhost:8080";

function money(n) {
    return Number(n || 0).toFixed(2);
}

function get_cart() {
    return JSON.parse(localStorage.getItem("farmdrop_cart")) || [];
}

function save_cart(cart) {
    localStorage.setItem("farmdrop_cart", JSON.stringify(cart));
}

function update_badge() {
    const badge = document.getElementById("cart-count");
    if (!badge) return;

    const cart = get_cart();
    const count = cart.reduce((sum, item) => sum + (item.qty ?? 1), 0);
    badge.textContent = String(count);
}

function guess_img(item) {
    const map = {
        tomatoes: "assets/images/tomatoes.jpg",
        carrots: "assets/images/carrots.jpg",
        sweetcorn: "assets/images/sweetcorn.jpg",
        "box-soup-m": "assets/images/box-soup.jpg",
        "box-salad-m": "assets/images/box-salad.jpg"
    };
    return map[item.id] || "assets/images/hero-basket.jpg";
}

function build_checkout_payload(cart) {
    // backend vrea: { items: [ {type, id, qty}, ... ] }
    return {
        items: cart.map(x => ({
            type: x.type,
            id: x.id,
            qty: x.qty ?? 1
        }))
    };
}

async function call_checkout(cart) {
    const payload = build_checkout_payload(cart);

    const res = await fetch(`${api_base}/api/checkout`, {
        method: "POST",
        headers: { "content-type": "application/json" },
        body: JSON.stringify(payload)
    });

    // daca backend trimite 400 cu json, tot incercam sa il citim
    const data = await res.json().catch(() => null);
    return { ok: res.ok, data };
}

function render_cart_items(cart) {
    const container = document.getElementById("cart-items");
    if (!container) return;

    if (!cart || cart.length === 0) {
        container.innerHTML = `<p class="cart__empty">Your cart is empty.</p>`;
        return;
    }

    container.innerHTML = cart.map(item => {
        const qty = item.qty ?? 1;
        const line_sum = Number(item.price) * qty;

        const meta = item.type === "product"
            ? `${Number(item.price).toFixed(2)} lei / ${item.unit ?? "item"}`
            : `Box · ${Number(item.price).toFixed(2)} lei`;

        return `
      <article class="cart-item">
        <img class="cart-item__img" src="${guess_img(item)}" alt="${item.name}">
        <div class="cart-item__info">
          <h3>${item.name}</h3>
          <p class="cart-item__meta">${meta}</p>

          <div class="cart-item__qty">
            <button class="qty-btn" type="button" data-qty-minus data-type="${item.type}" data-id="${item.id}">-</button>
            <span class="qty-value">${qty}</span>
            <button class="qty-btn" type="button" data-qty-plus data-type="${item.type}" data-id="${item.id}">+</button>
          </div>
        </div>

        <div class="cart-item__right">
          <p class="cart-item__price">${money(line_sum)} lei</p>
          <button class="remove-btn" type="button" data-remove data-type="${item.type}" data-id="${item.id}">
            <i class="fa-solid fa-trash"></i> Remove
          </button>
        </div>
      </article>
    `;
    }).join("");

    hook_cart_buttons();
}

function hook_cart_buttons() {
    document.querySelectorAll("[data-qty-plus]").forEach(btn => {
        btn.addEventListener("click", () => change_qty(btn.dataset.type, btn.dataset.id, +1));
    });

    document.querySelectorAll("[data-qty-minus]").forEach(btn => {
        btn.addEventListener("click", () => change_qty(btn.dataset.type, btn.dataset.id, -1));
    });

    document.querySelectorAll("[data-remove]").forEach(btn => {
        btn.addEventListener("click", () => remove_item(btn.dataset.type, btn.dataset.id));
    });
}

function change_qty(type, id, delta) {
    const cart = get_cart();
    const item = cart.find(x => x.type === type && x.id === id);
    if (!item) return;

    const next = (item.qty ?? 1) + delta;
    if (next <= 0) {
        const filtered = cart.filter(x => !(x.type === type && x.id === id));
        save_cart(filtered);
    } else {
        item.qty = next;
        save_cart(cart);
    }

    refresh_cart_ui();
}

function remove_item(type, id) {
    const cart = get_cart();
    const filtered = cart.filter(x => !(x.type === type && x.id === id));
    save_cart(filtered);
    refresh_cart_ui();
}

async function refresh_checkout_ui(cart) {
    const products_el = document.getElementById("sum-products");
    const boxes_el = document.getElementById("sum-boxes");
    const delivery_el = document.getElementById("sum-delivery");
    const total_el = document.getElementById("cart-total");
    const rule_el = document.getElementById("cart-rule");
    const checkout_btn = document.getElementById("checkout-btn");
    const msg_el = document.getElementById("checkout-msg");

    if (!products_el || !boxes_el || !delivery_el || !total_el || !rule_el || !checkout_btn) return;

    // cos gol
    if (!cart || cart.length === 0) {
        products_el.textContent = money(0);
        boxes_el.textContent = money(0);
        delivery_el.textContent = money(0);
        total_el.textContent = money(0);

        rule_el.textContent = "No summary. Add items to checkout.";
        checkout_btn.disabled = true;
        checkout_btn.style.opacity = "0.55";
        checkout_btn.style.pointerEvents = "none";
        if (msg_el) msg_el.style.display = "none";
        return;
    }

    // apel real la backend
    const { data } = await call_checkout(cart);

    if (!data) {
        rule_el.textContent = "Checkout api did not return json.";
        checkout_btn.disabled = true;
        checkout_btn.style.opacity = "0.55";
        checkout_btn.style.pointerEvents = "none";
        return;
    }

    products_el.textContent = money(data.productsTotal);
    boxes_el.textContent = money(data.boxesTotal);
    delivery_el.textContent = money(data.delivery);
    total_el.textContent = money(data.total);

    rule_el.textContent = data.message;

    if (data.canCheckout) {
        checkout_btn.disabled = false;
        checkout_btn.style.opacity = "1";
        checkout_btn.style.pointerEvents = "auto";
    } else {
        checkout_btn.disabled = true;
        checkout_btn.style.opacity = "0.55";
        checkout_btn.style.pointerEvents = "none";
    }
}

async function refresh_cart_ui() {
    const cart = get_cart();
    render_cart_items(cart);
    update_badge();
    await refresh_checkout_ui(cart);
}

function hook_checkout_button() {
    const checkout_btn = document.getElementById("checkout-btn");
    const msg_el = document.getElementById("checkout-msg");
    if (!checkout_btn) return;

    checkout_btn.addEventListener("click", async () => {
        const cart = get_cart();
        if (checkout_btn.disabled) return;

        const { data } = await call_checkout(cart);

        if (!data) return;

        if (msg_el) {
            msg_el.style.display = "block";
            msg_el.textContent = data.message;
        }

        // daca backend zice ca e ok, golesc cosul
        if (data.canCheckout) {
            localStorage.removeItem("farmdrop_cart");
            await refresh_cart_ui();
        }
    });
}

document.addEventListener("DOMContentLoaded", async () => {
    await refresh_cart_ui();
    hook_checkout_button();
});
