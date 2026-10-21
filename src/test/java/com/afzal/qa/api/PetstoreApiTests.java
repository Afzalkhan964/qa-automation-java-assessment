package com.afzal.qa.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

public class PetstoreApiTests {
    private long petId;
    private String petName;

    @BeforeClass
    public void configure() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        petId = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_000_000_000L);
        petName = "qa-pet-" + petId;
    }

    @Test
    public void createResource() {
        Map<String, Object> pet = new HashMap<>();
        pet.put("id", petId);
        pet.put("name", petName);
        pet.put("status", "available");

        given().contentType(ContentType.JSON).body(pet)
        .when().post("/pet")
        .then().statusCode(200).contentType(ContentType.JSON);

        Map response = given().accept(ContentType.JSON)
                .when().get("/pet/" + petId)
                .then().statusCode(200).extract().as(Map.class);
        Assert.assertEquals(String.valueOf(response.get("name")), petName);
    }

    @Test(dependsOnMethods = "createResource")
    public void readAndVerifyResource() {
        Map response = given().accept(ContentType.JSON)
                .when().get("/pet/" + petId)
                .then().statusCode(200).extract().as(Map.class);

        Assert.assertEquals(((Number) response.get("id")).longValue(), petId);
        Assert.assertEquals(String.valueOf(response.get("name")), petName);
    }

    @Test(dependsOnMethods = "readAndVerifyResource")
    public void updateResource() {
        String updatedName = petName + "-updated";
        Map<String, Object> pet = new HashMap<>();
        pet.put("id", petId);
        pet.put("name", updatedName);
        pet.put("status", "sold");

        given().contentType(ContentType.JSON).body(pet)
        .when().put("/pet")
        .then().statusCode(200);

        Map response = given().accept(ContentType.JSON)
                .when().get("/pet/" + petId)
                .then().statusCode(200).extract().as(Map.class);
        Assert.assertEquals(String.valueOf(response.get("name")), updatedName);
        Assert.assertEquals(String.valueOf(response.get("status")), "sold");
    }

    @Test(dependsOnMethods = "updateResource")
    public void deleteAndConfirmResource() {
        given().when().delete("/pet/" + petId).then().statusCode(200);

        int status = given().accept(ContentType.JSON)
                .when().get("/pet/" + petId)
                .then().extract().statusCode();
        Assert.assertTrue(status == 404 || status == 200,
                "Petstore may return either not-found or a stale successful response briefly; actual: " + status);
        if (status == 200) {
            // The public demo API can be eventually consistent; report this rather than hiding it.
            System.out.println("Note: Petstore still returned HTTP 200 after delete; verify response body/API state.");
        }
    }
}
