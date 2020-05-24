package uk.gov.justice.digital.delius.controller.secure;

import io.restassured.RestAssured;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.digital.delius.JwtAuthenticationHelper;
import uk.gov.justice.digital.delius.controller.wiremock.DeliusExtension;
import uk.gov.justice.digital.delius.controller.wiremock.DeliusMockServer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class CaseNoteAPITest {

    private static final DeliusMockServer deliusMockServer = new DeliusMockServer(8999);

    @RegisterExtension
    static DeliusExtension deliusExtension = new DeliusExtension(deliusMockServer);

    @LocalServerPort
    int port;

    @Autowired
    protected JwtAuthenticationHelper jwtAuthenticationHelper;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/secure";
    }

    @Test
    public void shouldReturnOKWhenSendCaseNoteToDelius() {

        deliusMockServer.stubPutCaseNoteToDeliusCreated("54321", 12345L);

        final var token = createJwt("bob", Collections.singletonList("ROLE_DELIUS_CASE_NOTES"));

        final var response = given()
                .when()
                .auth().oauth2(token)
                .contentType(String.valueOf(ContentType.APPLICATION_JSON))
                .body("{\"content\":\"Bob\"}")
                .put("/nomisCaseNotes/54321/12345")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .asString();

        assertThat(response).isEqualTo(" XXXX (crn) had a Contact created.");

    }

    @Test
    public void shouldReturnNoContentWhenSendUpdateCaseNoteToDelius() {

        deliusMockServer.stubPutCaseNoteToDeliusNoContent("54321", 12345L);

        final var token = createJwt("bob", Collections.singletonList("ROLE_DELIUS_CASE_NOTES"));

        given()
                .when()
                .auth().oauth2(token)
                .contentType(String.valueOf(ContentType.APPLICATION_JSON))
                .body("{\"content\":\"Bob\"}")
                .put("/nomisCaseNotes/54321/12345")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void shouldReturnBadRequestWhenSendCaseNoteToDelius() {

        deliusMockServer.stubPutCaseNoteToDeliusBadRequestError("54321", 12346L);

        final var token = createJwt("bob", Collections.singletonList("ROLE_DELIUS_CASE_NOTES"));

        given()
                .when()
                .auth().oauth2(token)
                .contentType(String.valueOf(ContentType.APPLICATION_JSON))
                .body("{\"content\":\"Bob\"}")
                .put("/nomisCaseNotes/54321/12346")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    private String createJwt(final String user, final List<String> roles) {
        return jwtAuthenticationHelper.createJwt(JwtAuthenticationHelper.JwtParameters.builder()
                .username(user)
                .roles(roles)
                .scope(Arrays.asList("read", "write"))
                .expiryTime(Duration.ofDays(1))
                .build());
    }
}