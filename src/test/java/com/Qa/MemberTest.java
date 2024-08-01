package com.Qa;

import com.Qa.pojo.LoginPojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import static org.testng.Assert.assertTrue;

public class MemberTest {

    private SimpleSmtpServer smtpServer;
    private Faker faker;

    String accessToken;


    @BeforeClass
    public void setup() throws IOException {
        // Start TestMailServer
        smtpServer = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);
        // Initialize Faker
        faker = new Faker();
    }

    @AfterClass
    public void tearDown() {
        // Stop TestMailServer
        smtpServer.stop();
    }

    @Test

    public void testSignUp() throws InterruptedException {
        String email = faker.internet().emailAddress();

        String baseUrl = "https://srevamp-spark.spacejat.com/api";
        HashMap<String, String> payLoadSignUp = new HashMap<>();
        payLoadSignUp.put("username", faker.name().username());
        payLoadSignUp.put("email",    faker.internet().emailAddress());
        payLoadSignUp.put("firstName",faker.name().firstName());
        payLoadSignUp.put("lastName", faker.name().lastName());
        payLoadSignUp.put("password", faker.internet().password(8, 16));

        given().baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(payLoadSignUp)
                .log().all()
                .when().post("/user/v1_createaccount")
                .then().log().all().assertThat().statusCode(200);

        // Poll for the email
//
//        boolean emailReceived = false;
//        for (int i = 0; i < 60; i++) {
//            List<SmtpMessage> emailMessages = smtpServer.getReceivedEmails();
//            if (!emailMessages.isEmpty()) {
//                for (SmtpMessage message : emailMessages) {
//                    System.out.println("Received email: " + message.getHeaderValue("Subject")); // Print email subject
//                    System.out.println("Email body: " + new String(message.getBody())); // Print email body
//                    if (message.getHeaderValue("Subject").contains("Update Your Account") &&
//                            message.getHeaderValue("To").contains(email)) {
//                        emailReceived = true;
//                        break;
//                    }
//                }
//                if (emailReceived) break;
//            }
//            Thread.sleep(1000);
//        }
//
//        assertTrue(emailReceived, "Expected email was not received in the expected time");
    }


    @Test


    public void testLogin() {

        LoginPojo bodyData = new LoginPojo();
        bodyData.setEmail(faker.internet().emailAddress());
        bodyData.setPassword(faker.internet().password(8, 16));
//                                                   ^^
        // or we can use constructor instead of that ||


        System.out.println("bodyData: "+ bodyData);

        File payloadLogin = new File("src/test/resources/login.json");

        String baseUrl = "https://srevamp-spark.spacejat.com/api";

                Response response =   given().baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .log().all()
                .when().post("/user/v1_login")
                .then().log()

                .all().assertThat().statusCode(200)
                .extract().response();
                accessToken = response.jsonPath().getString("access_token");
                System.out.println("accessToken: "+accessToken);

    }
}
