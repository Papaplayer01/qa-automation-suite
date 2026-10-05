package com.parth.qa.tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/** API tests against the public JSONPlaceholder service, using Playwright APIRequestContext. */
class ApiTests {
    private static Playwright playwright;
    private static APIRequestContext api;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        api = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL("https://jsonplaceholder.typicode.com"));
    }

    @AfterAll
    static void teardown() {
        api.dispose();
        playwright.close();
    }

    @Test
    @DisplayName("GET /posts returns 100 posts")
    void listPosts() {
        APIResponse res = api.get("/posts");
        assertEquals(200, res.status());
        JsonArray posts = JsonParser.parseString(res.text()).getAsJsonArray();
        assertEquals(100, posts.size());
    }

    @Test
    @DisplayName("GET /posts/1 returns the expected schema and values")
    void getSinglePost() {
        APIResponse res = api.get("/posts/1");
        assertEquals(200, res.status());
        assertTrue(res.headers().get("content-type").contains("application/json"));
        JsonObject post = JsonParser.parseString(res.text()).getAsJsonObject();
        for (String field : new String[]{"userId", "id", "title", "body"}) {
            assertTrue(post.has(field), "Missing field: " + field);
        }
        assertEquals(1, post.get("id").getAsInt());
        assertFalse(post.get("title").getAsString().isBlank());
    }

    @Test
    @DisplayName("GET /posts/99999 returns 404 (negative case)")
    void missingPost() {
        assertEquals(404, api.get("/posts/99999").status());
    }

    @Test
    @DisplayName("POST /posts echoes the payload and returns 201")
    void createPost() {
        JsonObject payload = new JsonObject();
        payload.addProperty("title", "qa-automation");
        payload.addProperty("body", "created by test");
        payload.addProperty("userId", 7);

        APIResponse res = api.post("/posts", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(payload.toString()));
        assertEquals(201, res.status());
        JsonObject created = JsonParser.parseString(res.text()).getAsJsonObject();
        assertEquals("qa-automation", created.get("title").getAsString());
        assertEquals(7, created.get("userId").getAsInt());
        assertTrue(created.has("id"));
    }

    @Test
    @DisplayName("GET /posts?userId=1 returns only that user's posts (filtering)")
    void filterByUser() {
        JsonArray posts = JsonParser.parseString(api.get("/posts?userId=1").text()).getAsJsonArray();
        assertFalse(posts.isEmpty());
        posts.forEach(p -> assertEquals(1, p.getAsJsonObject().get("userId").getAsInt()));
    }
}
