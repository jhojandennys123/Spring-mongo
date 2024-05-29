package tws.org.in.Spring.MongoDB.Exceptions.custom;

public class CustomAuthorizationException extends RuntimeException {
  public CustomAuthorizationException() {
    super("No cuenta con autorización.");
  }

  public CustomAuthorizationException(String message) {
    super(message);
  }
}