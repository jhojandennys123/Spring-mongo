package tws.org.in.Spring.MongoDB.Exceptions.custom;

public class CustomValidationException extends RuntimeException {
  public CustomValidationException() {
    super("Realizar validaciones correspondientes.");
  }

  public CustomValidationException(String message) {
    super(message);
  }
}
