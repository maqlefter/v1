package requests;

import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.UserRequest;

public class AdminCreateUserRequest extends Request<UserRequest>{
  public AdminCreateUserRequest(
      RequestSpecification requestSpecification,
      ResponseSpecification responseSpecification
  ) {
    super(requestSpecification, responseSpecification);
  }

  @Override
  public ValidatableResponse post(UserRequest model) {
    return given()
        .spec(requestSpecification)
        .body(model)
        .post("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .spec(responseSpecification);
  }
}
