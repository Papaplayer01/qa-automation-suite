package com.parth.qa.base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

/**
 * Shared setup for UI tests: one browser per class, a fresh context + page per test,
 * and a Playwright trace saved for every test.
 */
public abstract class BaseTest {
    protected static final String BASE_URL = "https://www.saucedemo.com";

    private static Playwright playwright;
    private static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        boolean headed = "false".equalsIgnoreCase(System.getProperty("headless", "true"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(!headed));
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 720));
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true).setSnapshots(true).setSources(false));
        page = context.newPage();
    }

    @AfterEach
    void saveTraceAndClose(TestInfo info) {
        String name = info.getTestMethod().map(m -> m.getName()).orElse("test");
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("target", "traces", name + ".zip")));
        context.close();
    }
}
