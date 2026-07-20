package bd.com;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class Basics {

    static final String KEY = "qaclick123";

    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // 1. Create a place, capture the place_id it returns
        String placeId = addPlace();
        System.out.println("Created place_id: " + placeId);

        // 2. Fetch it back
        getPlace(placeId);

        // 3. Update it
        updatePlace(placeId);

        // 4. Delete it
        deletePlace(placeId);
    }

    // ---------- ADD ----------
    public static String addPlace() {
        Response response =
            given()
                .log().all()
                .queryParam("key", KEY)
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}")
            .when()
                .post("/maps/api/place/add/json")
            .then()
                .log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // pull place_id straight out of the JSON response
        return response.jsonPath().getString("place_id");
    }

    // ---------- GET ----------
    public static void getPlace(String placeId) {
        given()
            .log().all()
            .queryParam("key", KEY)
            .queryParam("place_id", placeId)
            .header("Content-Type", "application/json")
        .when()
            .get("/maps/api/place/get/json")
        .then()
            .log().all()
            .assertThat().statusCode(200);
    }

    // ---------- UPDATE ----------
    public static void updatePlace(String placeId) {
        given()
            .log().all()
            .queryParam("key", KEY)
            .header("Content-Type", "application/json")
            .body("{\n" +
                    "  \"place_id\": \"" + placeId + "\",\n" +
                    "  \"address\": \"Updated address, 42 new layout\"\n" +
                    "}")
        .when()
            .put("/maps/api/place/update/json")
        .then()
            .log().all()
            .assertThat().statusCode(200);
    }

    // ---------- DELETE ----------
    public static void deletePlace(String placeId) {
        given()
            .log().all()
            .queryParam("key", KEY)
            .header("Content-Type", "application/json")
            .body("{\n" +
                    "  \"place_id\": \"" + placeId + "\"\n" +
                    "}")
        .when()
            .delete("/maps/api/place/delete/json")
        .then()
            .log().all()
            .assertThat().statusCode(200);
    }
}