package tws.org.in.Spring.MongoDB.Exceptions.custom;

public class CustomErrorException extends RuntimeException {
  public CustomErrorException(String message) {
    super(message);
  }
}
