package generatedata;

import org.apache.commons.lang3.RandomStringUtils;

public class GenerateData {
  private GenerateData(){}

  public static String getUserName(){
    return RandomStringUtils.randomAlphabetic(10);
  }

  public static String getPassword(){
    return RandomStringUtils.randomAlphabetic(2).toUpperCase() +
        RandomStringUtils.randomAlphabetic(3).toLowerCase() +
        RandomStringUtils.randomNumeric(2) +
        RandomStringUtils.randomAlphabetic(1) +
        "!";
  }
}
