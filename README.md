# RestAssuredAPI-Testing
# Google Maps Place API — REST Assured Automation
 
API automation suite for the **Google Maps Place API** (practice API hosted by [rahulshettyacademy.com](https://rahulshettyacademy.com)), built with **Java, Maven, REST Assured, TestNG, and Hamcrest**.
 
Covers the full CRUD lifecycle of a "place": **Add → Get → Update → Delete**, with response validation and dynamic `place_id` chaining between requests.
 
---
 
## Tech Stack
 
| Tool | Purpose |
|---|---|
| **Java 17** | Language runtime |
| **Maven** | Build tool & dependency management |
| **REST Assured** | HTTP client / API testing DSL |
| **TestNG** | Test framework & runner |
| **Hamcrest** | Fluent assertion matchers |
 
---
 
## Prerequisites
 
- JDK 17+ installed and configured (`java -version`)
- Maven installed and on PATH (`mvn -version`)
- An IDE with Java + Maven support (e.g. VS Code with the *Extension Pack for Java*, or IntelliJ IDEA)
---
 
## Project Structure
 
```
autoAPI/
├─ pom.xml
└─ src/
   ├─ main/java/bd/com/
   │   └─ Basics.java        # addPlace / getPlace / updatePlace / deletePlace
   └─ test/java/...          # TestNG test classes
```
 
---
 
## Dependencies (`pom.xml`)
 
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
 
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.testng/testng -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.12.0</version>
    </dependency>
 
    <!-- https://mvnrepository.com/artifact/org.hamcrest/hamcrest -->
    <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest</artifactId>
        <version>3.0</version>
    </dependency>
 
    <!-- https://mvnrepository.com/artifact/io.rest-assured/rest-assured -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>6.0.1</version>
    </dependency>
</dependencies>
```
 
> **Note:** REST Assured 6.x requires **Java 17+**. Running on an older JDK throws `UnsupportedClassVersionError`.
 
---
 
## API Reference
 
**Base URL:** `https://rahulshettyacademy.com`
**Auth:** query param `key=qaclick123` (hardcoded, always the same value) on every request
 
### 1. Add Place — `POST /maps/api/place/add/json`
 
| | |
|---|---|
| Query Params | `key` |
| Body | place object (see below) |
 
**Request body:**
```json
{
  "location": { "lat": -38.383494, "lng": 33.427362 },
  "accuracy": 50,
  "name": "Frontline house",
  "phone_number": "(+91) 983 893 3937",
  "address": "29, side layout, cohen 09",
  "types": ["shoe park", "shop"],
  "website": "http://google.com",
  "language": "French-IN"
}
```
 
**Response:**
```json
{
  "status": "OK",
  "place_id": "928b51f64aed18713b0d164d9be8d67f",
  "scope": "APP",
  "reference": "736f3c9bec384af62a184a1936d42bb0736f3c9bec384af62a184a1936d42bb0",
  "id": "736f3c9bec384af62a184a1936d42bb0"
}
```
`place_id` from this response is required by all three endpoints below.
 
---
 
### 2. Get Place — `GET /maps/api/place/get/json`
 
| | |
|---|---|
| Query Params | `key`, `place_id` |
 
**Response:**
```json
{
  "location": { "lat": -38.383494, "lng": 33.427362 },
  "accuracy": 50,
  "name": "Frontline house",
  "phone_number": "(+91) 983 893 3937",
  "address": "29, side layout, cohen 09",
  "types": ["shoe park", "shop"],
  "website": "http://google.com",
  "language": "French-IN"
}
```
 
---
 
### 3. Update Place — `PUT /maps/api/place/update/json`
 
| | |
|---|---|
| Query Params | `key` |
| Body | `place_id` + fields to update |
 
**Request body:**
```json
{
  "place_id": "8d2573bdf6ceec0e474c5f388fa917fb",
  "address": "70 Summer walk, USA",
  "key": "qaclick123"
}
```
 
Returns the full, updated place object (same shape as Get Place).
 
---
 
### 4. Delete Place — `DELETE /maps/api/place/delete/json`
 
| | |
|---|---|
| Query Params | `key` |
| Body | `place_id` |
 
**Request body:**
```json
{ "place_id": "928b51f64aed18713b0d164d9be8d67f" }
```
 
**Response:**
```json
{ "status": "OK" }
```
 
---
 
## Running the Tests
 
```bash
mvn clean test
```
 
Run a single class:
```bash
mvn -Dtest=Basics test
```
 
---
 
## Key Learnings / Gotchas
 
- **`place` vs `place_id`** — the Get/Update/Delete endpoints expect the query/body key to be exactly `place_id`, matching what Add Place returns. A mismatched param name fails silently: the server returns `200 OK` with an empty body instead of an error.
- **`queryParam(name, val1, val2, ...)`** in REST Assured treats every argument after the first as another value for *that same* parameter name. Two distinct query params need two separate `.queryParam()` calls.
- **JDK/REST Assured version mismatch** shows up as `UnsupportedClassVersionError` at runtime — check your IDE's *actual* configured project JDK (not just what `java -version` shows in a terminal), since IDEs can bind a project to a different JDK than your global default.
