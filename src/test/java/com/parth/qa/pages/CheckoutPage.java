package com.parth.qa.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {
    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void fillDetails(String first, String last, String postalCode) {
        page.fill("[data-test=\"firstName\"]", first);
        page.fill("[data-test=\"lastName\"]", last);
        page.fill("[data-test=\"postalCode\"]", postalCode);
        page.click("[data-test=\"continue\"]");
    }

    public void finish() {
        page.click("[data-test=\"finish\"]");
    }

    public String confirmationHeader() {
        return page.locator("[data-test=\"complete-header\"]").innerText();
    }

    public String errorMessage() {
        return page.locator("[data-test=\"error\"]").innerText();
    }
}
