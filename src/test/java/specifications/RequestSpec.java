package specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import models.LoginUserRequest;
import requests.LoginUserRequester;

public class RequestSpec {
  private RequestSpec(){}

  private static RequestSpecBuilder defaultSpecBuild(){
    return new RequestSpecBuilder()
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .addFilters(List.of(new RequestLoggingFilter(),
            new ResponseLoggingFilter()))
        .setBaseUri("http://localhost:4111");
  }

  public static RequestSpecification unAuthSpec(){
    return defaultSpecBuild().build();
  }

  public static RequestSpecification adminSpec(){
    return defaultSpecBuild()
        .addHeader("authorization", "Basic YWRtaW46YWRtaW4=")
        .build();
  }

  public static RequestSpecification loginUserSpec(String username, String password){
    String authToken = new LoginUserRequester(
        RequestSpec.unAuthSpec(),
        ResponseSpec.requestWasOk())
        .post(LoginUserRequest.builder().username(username).password(password).build())
        .extract()
        .header("authorization");

    return defaultSpecBuild()
        .addHeader("authorization", authToken)
        .build();
  }
}
