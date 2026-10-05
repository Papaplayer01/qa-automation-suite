package com.parth.qa.tests;

import com.parth.qa.base.BaseTest;
import com.parth.qa.pages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartCheckoutTests extends BaseTest {
    private InventoryPage inventory;

    @BeforeEach
    void login() {
        new LoginPage(page).open(BASE_URL).loginAs("standard_user", "secret_sauce");
        inventory = new InventoryPage(page);
    }

    @Test
    @DisplayName("Adding and removing items updates the cart badge")
    void cartBadgeUpdates() {
        inventory.addToCart("sauce-labs-backpack");
        inventory.addToCart("sauce-labs-bike-light");
        assertEquals(2, inventory.cartBadgeCount());
        inventory.removeFromCart("sauce-labs-backpack");
        assertEquals(1, inventory.cartBadgeCount());
        inventory.removeFromCart("sauce-labs-bike-light");
        assertEquals(0, inventory.cartBadgeCount());
    }

    @Test
    @DisplayName("Cart page lists exactly the items that were added")
    void cartContainsAddedItems() {
        inventory.addToCart("sauce-labs-backpack");
        inventory.openCart();
        assertEquals(List.of("Sauce Labs Backpack"), new CartPage(page).itemNames());
    }

    @Test
    @DisplayName("Price sort low-to-high orders prices ascending")
    void sortPriceLowToHigh() {
        inventory.sortBy("lohi");
        List<Double> prices = inventory.prices();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        assertEquals(sorted, prices);
    }

    @Test
    @DisplayName("Name sort Z-to-A orders names descending")
    void sortNameZToA() {
        inventory.sortBy("za");
        List<String> names = inventory.names();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Collections.reverseOrder());
        assertEquals(sorted, names);
    }

    @Test
    @DisplayName("End-to-end: add item, checkout, see order confirmation")
    void endToEndCheckout() {
        inventory.addToCart("sauce-labs-onesie");
        inventory.openCart();
        new CartPage(page).checkout();
        CheckoutPage checkout = new CheckoutPage(page);
        checkout.fillDetails("Parth", "Pawar", "422001");
        checkout.finish();
        assertEquals("Thank you for your order!", checkout.confirmationHeader());
    }

    @Test
    @DisplayName("Checkout is blocked when required details are missing")
    void checkoutValidation() {
        inventory.addToCart("sauce-labs-backpack");
        inventory.openCart();
        new CartPage(page).checkout();
        CheckoutPage checkout = new CheckoutPage(page);
        checkout.fillDetails("", "Pawar", "422001");
        assertTrue(checkout.errorMessage().contains("First Name is required"));
    }
}
