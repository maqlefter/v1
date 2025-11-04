package iteration;


import generatedata.GenerateData;
import models.LoginUserRequest;
import models.UserRequest;
import models.UserRole;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequest;
import requests.LoginUserRequester;
import specifications.RequestSpec;
import specifications.ResponseSpec;

public class NewAuthUserTest {
  @Test
  public void adminCanGenerateAuthTokenTest() {
    LoginUserRequest userRequest = LoginUserRequest.builder()
        .username("admin")
        .password("admin")
        .build();

    new LoginUserRequester(
        RequestSpec.unAuthSpec(),
        ResponseSpec.requestWasOk()
    )
        .post(userRequest);
  }

  @Test
  public void userCanGenerateAuthTokenTest() {
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

    LoginUserRequest loginUserRequest = LoginUserRequest.builder()
        .username(userRequest.getUsername())
        .password(userRequest.getPassword())
        .build();

    new LoginUserRequester(
        RequestSpec.unAuthSpec(),
        ResponseSpec.requestWasOk()
    )
        .post(loginUserRequest)
        .assertThat()
        .header("authorization", Matchers.notNullValue());
  }
}

