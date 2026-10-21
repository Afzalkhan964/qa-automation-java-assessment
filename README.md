# QA Automation Engineer Technical Assessment

Java + Selenium + TestNG UI automation and REST Assured API automation.

## Stack
- Java 17
- Maven
- Selenium WebDriver + WebDriverManager
- TestNG
- REST Assured
- Git / GitHub

## Test coverage

### UI (`ShoppingUiTests`)
1. Home page: checks a key shopping navigation element and saves a screenshot.
2. Category/landing navigation: opens a category/landing link and verifies navigation.
3. Search: searches for `laptop`, checks the results page/search term, and saves a screenshot.

The UI tests use eBay as a public shopping website. Public sites may change their HTML, apply regional redirects, or show bot checks; update locators if the site changes. Screenshots capture the browser viewport.

### API (`PetstoreApiTests`)
1. POST: create a pet with a dynamically generated ID/name.
2. GET: retrieve it and verify ID/name.
3. PUT: update the name/status and verify.
4. DELETE: delete and verify the follow-up GET response.

The Swagger Petstore is a shared public demo API, so it can be intermittently unavailable or inconsistent. For a production-grade assessment, run against an isolated test API if one is provided.

## Prerequisites
- JDK 17 installed
- Maven installed
- Google Chrome installed
- Git installed

Check:
```bash
java -version
mvn -version
git --version
```

## Run the tests
Open a terminal in the project root:

```bash
mvn clean test
```

Run UI tests in headless mode:
```bash
mvn clean test -Dheadless=true
```

Screenshots are saved to `screenshots/` when the relevant UI tests pass the point where the screenshot is captured. TestNG/Surefire reports are under `target/surefire-reports/`.

## Push this project to GitHub

1. Create a new **empty** repository on GitHub, for example `qa-automation-java-assessment`. Do not add a README or `.gitignore` on GitHub because this project already contains them.
2. Open a terminal in this project folder.
3. Run the following commands, replacing `YOUR_GITHUB_USERNAME` with your username:

```bash
git init
git add .
git commit -m "Add Java Selenium and REST Assured assessment"
git branch -M main
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/qa-automation-java-assessment.git
git push -u origin main
```

If Git says `remote origin already exists`, use:
```bash
git remote set-url origin https://github.com/YOUR_GITHUB_USERNAME/qa-automation-java-assessment.git
git push -u origin main
```

GitHub authentication: use the browser sign-in flow if Git Credential Manager opens. If Git asks for a password over HTTPS, use a GitHub personal access token instead of your account password. Never put tokens/passwords in source code or commit them.

## Before submitting
- Run `mvn clean test` and review the test report.
- Confirm screenshots exist under `screenshots/`.
- Update the README with your name, execution date, and any issues found.
- Do not claim all tests passed if public-site/API instability caused failures.
