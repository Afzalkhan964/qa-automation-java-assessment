package com.afzal.qa.ui;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class ShoppingUiTests extends BaseUiTest {
    private static final String BASE_URL = "https://www.ebay.com/";

    private void screenshot(String name) throws IOException {
        Path folder = Path.of("screenshots");
        Files.createDirectories(folder);
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(source.toPath(), folder.resolve(name + ".png"),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void homePageShowsKeyNavigationAndScreenshot() throws IOException {
        driver.get(BASE_URL);
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        Assert.assertTrue(pageText.contains("shop by category") ||
                        driver.findElements(By.xpath("//span[text()='Shop by category']")).size() > 0,
                "Expected a key shopping navigation element on the home page.");
        screenshot("01-home-page");
    }

    @Test
    public void categoryNavigationOpensDestinationPage() throws IOException {
        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // eBay navigation labels vary by region; try a common category link.
        List<WebElement> categoryLinks = driver.findElements(
                By.cssSelector("a[href*='/b/'], a[href*='/deals'], a[href*='/fashion']"));
        Assert.assertFalse(categoryLinks.isEmpty(), "No category/landing links were found.");
        WebElement category = categoryLinks.get(0);
        String label = category.getText().trim();
        category.click();

        wait.until(d -> !d.getCurrentUrl().equals(BASE_URL));
        Assert.assertNotEquals(driver.getCurrentUrl(), BASE_URL,
                "Expected category navigation to open a destination page.");
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());
        screenshot("02-category-page");
        System.out.println("Navigated through category link: " + label + " -> " + driver.getCurrentUrl());
    }
    @Test
    public void productSearchShowsResultsAndScreenshot() throws IOException {

        driver.get(BASE_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Locate the search box using XPath
        WebElement searchBox = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@id='gh-ac' or contains(@placeholder,'Search') or contains(@aria-label,'Search')]")
            )
        );

        // Search for a product
        searchBox.clear();
        searchBox.sendKeys("laptop");
        searchBox.sendKeys(Keys.ENTER);

        // Wait until product results are visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//li[contains(@class,'s-item')]")
        ));

        // Verify the search results
        String pageText = driver.findElement(By.xpath("//body"))
                                .getText()
                                .toLowerCase();

        Assert.assertTrue(
            pageText.contains("laptop"),
            "Expected search results to reflect the search term."
        );
        screenshot("03-search-results");
    }
}
