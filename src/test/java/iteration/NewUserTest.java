package iteration;

import generatedata.GenerateData;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import models.LoginUserRequest;
import models.UserRequest;
import models.UserResponse;
import models.UserRole;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequest;
import requests.CreateAccountRequester;
import requests.LoginUserRequester;
import specifications.RequestSpec;
import specifications.ResponseSpec;

public class NewUserTest extends BaseTest {

  @Test
  public void adminCanCreateUserTest() {
    UserRequest userRequest = UserRequest.builder()
        .username(GenerateData.getUserName())
        .password(GenerateData.getPassword())
        .role(UserRole.USER.toString())
        .build();

    UserResponse userResponse = new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasCreated()
    )
        .post(userRequest).extract().as(UserResponse.class);

    softly.assertThat(userResponse.getUsername()).isEqualTo(userRequest.getUsername());
    softly.assertThat(userResponse.getRole()).isEqualTo(userRequest.getRole());
    softly.assertThat(userResponse.getPassword()).isNotEqualTo(userRequest.getPassword());
  }

  @Test
  public void adminCantCreateUserTest() {
    UserRequest userRequest = UserRequest.builder()
        .username("invalid username!")
        .password("122222222")
        .role(UserRole.USER.toString())
        .build();

    new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasBadRequest()
    )
        .post(userRequest)
        .body("password",
            Matchers.equalTo(
                "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 " +
                    "characters long")
        )
        .body("username", Matchers.equalTo("Username must contain only letters, digits, dashes, underscores, and dots"));
  }

  @Test
  public void adminCanCheckList() {
    UserRequest userRequest = UserRequest.builder()
        .username(GenerateData.getUserName())
        .password(GenerateData.getPassword())
        .role(UserRole.USER.toString())
        .build();

    new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasCreated()
    )
        .post(userRequest);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization", "Basic YWRtaW46YWRtaW4=")
        .get("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.not(Matchers.equalTo("TestUser")));
  }

  @Test
  public void userCanCreateAccountTest() {
    UserRequest userRequest = UserRequest.builder()
        .username(GenerateData.getUserName())
        .password(GenerateData.getPassword())
        .role(UserRole.USER.toString())
        .build();

    new AdminCreateUserRequest(
        RequestSpec.adminSpec(),
        ResponseSpec.entityWasCreated()
    )
        .post(userRequest);

    new CreateAccountRequester(RequestSpec.loginUserSpec(userRequest.getUsername(), userRequest.getPassword()),
        ResponseSpec.entityWasCreated())
        .post(null);

  }
}
