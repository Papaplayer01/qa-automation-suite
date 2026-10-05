package com.parth.qa.pages;

import com.microsoft.playwright.Page;

import java.util.List;

public class InventoryPage {
    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    public String title() {
        return page.locator("[data-test=\"title\"]").innerText();
    }

    public int itemCount() {
        return page.locator("[data-test=\"inventory-item\"]").count();
    }

    public void addToCart(String productSlug) {
        page.click("[data-test=\"add-to-cart-" + productSlug + "\"]");
    }

    public void removeFromCart(String productSlug) {
        page.click("[data-test=\"remove-" + productSlug + "\"]");
    }

    /** Returns 0 when the cart badge is not shown (empty cart). */
    public int cartBadgeCount() {
        var badge = page.locator("[data-test=\"shopping-cart-badge\"]");
        return badge.count() == 0 ? 0 : Integer.parseInt(badge.innerText());
    }

    public void sortBy(String optionValue) {
        page.selectOption("[data-test=\"product-sort-container\"]", optionValue);
    }

    public List<Double> prices() {
        return page.locator("[data-test=\"inventory-item-price\"]").allInnerTexts().stream()
                .map(t -> Double.parseDouble(t.replace("$", "")))
                .toList();
    }

    public List<String> names() {
        return page.locator("[data-test=\"inventory-item-name\"]").allInnerTexts();
    }

    public void openCart() {
        page.click("[data-test=\"shopping-cart-link\"]");
    }
}
