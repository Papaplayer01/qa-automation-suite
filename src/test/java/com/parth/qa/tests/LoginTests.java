package com.parth.qa.tests;

import com.parth.qa.base.BaseTest;
import com.parth.qa.pages.InventoryPage;
import com.parth.qa.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LoginTests extends BaseTest {

    @Test
    @DisplayName("Valid user lands on the products page with 6 items")
    void validLogin() {
        new LoginPage(page).open(BASE_URL).loginAs("standard_user", "secret_sauce");
        InventoryPage inventory = new InventoryPage(page);
        assertEquals("Products", inventory.title());
        assertEquals(6, inventory.itemCount());
    }

    @Test
    @DisplayName("Locked-out user is rejected with a clear message")
    void lockedOutUser() {
        LoginPage login = new LoginPage(page).open(BASE_URL);
        login.loginAs("locked_out_user", "secret_sauce");
        assertTrue(login.errorMessage().contains("locked out"));
    }

    @ParameterizedTest(name = "[{index}] user={0} pass={1} -> {2}")
    @CsvSource({
            "standard_user, wrong_password, do not match",
            "not_a_user, secret_sauce, do not match",
            "'', secret_sauce, Username is required",
            "standard_user, '', Password is required"
    })
    @DisplayName("Invalid credential combinations show the right error")
    void invalidCredentials(String user, String pass, String expectedFragment) {
        LoginPage login = new LoginPage(page).open(BASE_URL);
        login.loginAs(user, pass);
        assertTrue(login.isErrorVisible());
        assertTrue(login.errorMessage().contains(expectedFragment),
                "Unexpected error: " + login.errorMessage());
    }

    @Test
    @DisplayName("Inventory page is not reachable without logging in")
    void protectedPageRequiresLogin() {
        page.navigate(BASE_URL + "/inventory.html");
        assertTrue(new LoginPage(page).isErrorVisible());
    }
}
