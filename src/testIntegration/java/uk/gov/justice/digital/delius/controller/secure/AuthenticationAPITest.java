package uk.gov.justice.digital.delius.controller.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.justice.digital.delius.controller.advice.ErrorResponse;
import uk.gov.justice.digital.delius.data.api.AuthPassword;
import uk.gov.justice.digital.delius.data.api.AuthUser;
import uk.gov.justice.digital.delius.data.api.UserDetails;
import uk.gov.justice.digital.delius.data.api.UserRole;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev-seed")
public class AuthenticationAPITest {

    @LocalServerPort
    int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${test.token.auth}")
    private String validOauthToken;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/secure";
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (aClass, s) -> objectMapper
        ));
    }

    @Test
    public void authenticateReturnsOKWhenUsernamePasswordMatch() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("oliver.connolly").password("secret").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200);
    }

    @Test
    public void authenticateReturns400WhenNoPassword() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("oliver.connolly").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(400);
    }

    @Test
    public void authenticateReturns400WhenNoUsername() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().password("secret").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(400);
    }

    @Test
    public void authenticateReturns400WhenBodyEmpty() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/authenticate")
                .then()
                .statusCode(400);
    }

    @Test
    public void authenticateReturnsUNAUTHORIZEDWhenUsernamePasswordDoNotMatch() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("oliver.connolly").password("incorrectpassword").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(401);
    }

    @Test
    public void authenticateReturnsUNAUTHORIZEDWhenUsernameNotFound() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("not.exists").password("secret").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(401);
    }

    @Test
    public void notValid400ReturnedWhenPasswordEmpty() {

        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthPassword.builder().build())
                .when()
                .post("/users/oliver.connolly/password")
                .then()
                .statusCode(400);
    }

    @Test
    public void notValid400ReturnedWhenBodyEmpty() {

        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/users/oliver.connolly/password")
                .then()
                .statusCode(400);
    }

    @Test
    public void canChangeAUsersPassword() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("oliver.connolly").password("secret").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200);

        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthPassword.builder().password("newsecret").build())
                .when()
                .post("/users/oliver.connolly/password")
                .then()
                .statusCode(200);

        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("oliver.connolly").password("secret").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(401);

        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthUser.builder().username("oliver.connolly").password("newsecret").build())
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200);

        // restore to original password
        given()
                .auth().oauth2(validOauthToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(AuthPassword.builder().password("secret").build())
                .when()
                .post("/users/oliver.connolly/password")
                .then()
                .statusCode(200);
    }

    @Test
    public void usersDetails_success() {
        final var userDetails = given()
                .auth().oauth2(validOauthToken)
                .contentType("text/plain")
                .when()
                .get("/users/bernard.beaks/details")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserDetails.class);

        assertThat(userDetails.getFirstName()).isEqualTo("Bernard");
        assertThat(userDetails.getSurname()).isEqualTo("Beaks");
        assertThat(userDetails.getEmail()).isEqualTo("bernard.beaks@justice.gov.uk");
        assertThat(userDetails.getRoles()).hasSize(1).contains(UserRole.builder().name("UWBT060").build());
    }

    @Test
    public void usersDetails_returns404WhenUserNotFound() {
        given()
                .auth().oauth2(validOauthToken)
                .contentType("text/plain")
                .when()
                .get("/users/john.smith/details")
                .then()
                .statusCode(404);
    }

    @Test
    @DirtiesContext
    public void addRole() {

        given()
                .auth().oauth2(validOauthToken)
                .contentType("text/plain")
                .when()
                .put("/users/bernard.beaks/roles/CWBT001")
                .then()
                .statusCode(200);


        final var userDetails = given()
                .auth().oauth2(validOauthToken)
                .contentType("text/plain")
                .when()
                .get("/users/bernard.beaks/details")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserDetails.class);

        assertThat(userDetails.getFirstName()).isEqualTo("Bernard");
        assertThat(userDetails.getSurname()).isEqualTo("Beaks");
        assertThat(userDetails.getEmail()).isEqualTo("bernard.beaks@justice.gov.uk");
        assertThat(userDetails.getRoles()).hasSize(2).contains(
                UserRole.builder().name("CWBT001").build(),
                UserRole.builder().name("UWBT060").build());
    }

    @Test
    public void addRole_whenUserDoesNotExist() {

        ErrorResponse response = given()
                .auth().oauth2(validOauthToken)
                .contentType("text/plain")
                .when()
                .put("/users/usernoexist/roles/CWBT001")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);

        assertThat(response).isEqualTo(ErrorResponse.builder()
                .status(404)
                .developerMessage("Could not find user with username: 'usernoexist'")
                .build());;
    }
}