package com.parth.qa.pages;

import com.microsoft.playwright.Page;

import java.util.List;

public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public List<String> itemNames() {
        return page.locator("[data-test=\"inventory-item-name\"]").allInnerTexts();
    }

    public void checkout() {
        page.click("[data-test=\"checkout\"]");
    }
}
