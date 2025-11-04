package requests;

import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.DefaultModel;

public class CreateAccountRequester extends Request{
  public CreateAccountRequester(
      RequestSpecification requestSpecification,
      ResponseSpecification responseSpecification
  ) {
    super(requestSpecification, responseSpecification);
  }

  @Override
  public ValidatableResponse post(DefaultModel model) {
    return given()
        .spec(requestSpecification)
        .post("/api/v1/accounts")
        .then()
        .assertThat()
        .spec(responseSpecification);
  }
}
