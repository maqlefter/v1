package iteration;

import generatedata.GenerateData;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import java.util.List;
import models.UserRequest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequest;
import specifications.RequestSpec;
import specifications.ResponseSpec;

public class NewUserTest {
  @BeforeAll
  public static void setupRestAssured() {  // Добавлено static
    RestAssured.filters(
        List.of(
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        )
    );
  }
  @Test
  public void adminCanCreateUserTest() {
    UserRequest userRequest = UserRequest.builder()
        .username(GenerateData.getUserName())
        .password(GenerateData.getPassword())
        .role("USER")
        .build();

    new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasCreated())
        .post(userRequest)
        .assertThat()
        .body("username", Matchers.equalTo(userRequest.getUsername()))
        .body("password", Matchers.not(Matchers.equalTo(userRequest.getPassword())))
        .body("role", Matchers.equalTo(userRequest.getRole()))
        .body("password", Matchers.startsWith("$2a$"));
  }
  @Test
  public void adminCantCreateUserTest() {
    UserRequest userRequest = UserRequest.builder()
        .username("invalid username!")
        .password("122222222")
        .role("USER")
        .build();

    new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasBadRequest())
        .post(userRequest)
        .body("password", Matchers.equalTo("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"))
        .body("username",Matchers.equalTo("Username must contain only letters, digits, dashes, underscores, and dots"));
  }

  @Test
  public void adminCanCheckList(){
    UserRequest userRequest = UserRequest.builder()
        .username(GenerateData.getUserName())
        .password(GenerateData.getPassword())
        .role("USER")
        .build();

    new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasCreated());

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization","Basic YWRtaW46YWRtaW4=")
        .get("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.not(Matchers.equalTo("TestUser")));
  }
}
