package com.Qa;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestCase {

    ExtentReports extent;
    ExtentSparkReporter sparkReporter;
    ExtentTest test;

    @BeforeClass
    public void setup() {
        sparkReporter = new ExtentSparkReporter("extentReport.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("API Test Report");
        sparkReporter.config().setReportName("API Test Report");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", "Bassel Zeidan");
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
    }

    @Test
    public void testStatusCode() {
        test = extent.createTest("Test Status Code");

                 given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().log().all()
                .assertThat().statusCode(200);

        test.pass("Status code is 200");
    }

    @Test
    public void testUsingEqualTo() {
        test = extent.createTest("Test Using Equal To");

        given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().log().all()
                .assertThat().body("[0].member.name", equalTo("john Doe"));

        test.pass("Body contains 'john Doe'");
    }

    @Test
    public void testUsingHasItem() {
        test = extent.createTest("Test Using Has Item");

        given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().log().all()
                .assertThat().body("member.name", hasItem("john Doe"));

        test.pass("Body contains 'john Doe' as an item");
    }

    @Test
    public void testUsingHasItems() {
        test = extent.createTest("Test Using Has Items");

        given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().log().all()
                .assertThat().body("member.name", hasItems("john Doe", "Bassel Zeidan", "Abo Eljood"));

        test.pass("Body contains 'john Doe', 'Bassel Zeidan', and 'Abo Eljood' as items");
    }

    @Test
    public void testUsingIsNotEmpty() {
        test = extent.createTest("Test Using Is Not Empty");

        given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().log().all()
                .assertThat().body("member.name", is(not(empty())));

        test.pass("Body is not empty");
    }

    @Test
    public void testUsingEveryItem() {
        test = extent.createTest("Test Using Every Item");

        given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().log().all()
                .assertThat().body("member.phone_number", everyItem(startsWith("123-456-7890")));

        test.pass("Every item in 'phone_number' starts with '123-456-7890'");
    }




    @Test
    public void extractFullResponse() {
        test = extent.createTest("Test Using extractFullResponse");

       Response res= given().baseUri("http://localhost:3005")
                .when().get("getAllmembers")
                .then().extract().response();
       String name = res.path("member[0].name");
        System.out.println(res.asString());
        System.out.println(name);

        test.pass("This is All Response");
    }


    //json path

    



}
