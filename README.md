![Tests](https://github.com/Papaplayer01/qa-automation-suite/actions/workflows/tests.yml/badge.svg)

# QA Automation Suite (Java + Playwright + JUnit 5)

UI and API test automation, run automatically in CI.

- **UI tests:** login, cart, sorting, and checkout flows on saucedemo.com (a public practice site), written with the Page Object Model.
- **API tests:** status codes, schema fields, filtering, and negative cases against JSONPlaceholder via Playwright APIRequestContext.
- **Debugging:** a Playwright trace (screenshots + DOM snapshots) is saved for every UI test in target/traces/.
- **CI:** GitHub Actions runs the suite on every push and pull request and uploads reports and traces.

## Run locally (Java 17+ and Maven)

    mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"   # one-time
    mvn test                      # headless
    mvn test -Dheadless=false     # watch the browser
    mvn test -Dtest=ApiTests      # one class only

## View a trace

    mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace target/traces/endToEndCheckout.zip"
