package com.parth.qa.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public LoginPage open(String baseUrl) {
        page.navigate(baseUrl);
        return this;
    }

    public void loginAs(String username, String password) {
        page.fill("[data-test=\"username\"]", username);
        page.fill("[data-test=\"password\"]", password);
        page.click("[data-test=\"login-button\"]");
    }

    public String errorMessage() {
        return page.locator("[data-test=\"error\"]").innerText();
    }

    public boolean isErrorVisible() {
        return page.locator("[data-test=\"error\"]").isVisible();
    }
}
