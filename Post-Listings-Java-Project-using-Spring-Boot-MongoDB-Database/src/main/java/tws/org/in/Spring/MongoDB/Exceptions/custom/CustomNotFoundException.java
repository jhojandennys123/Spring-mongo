package tws.org.in.Spring.MongoDB.Exceptions.custom;

public class CustomNotFoundException extends RuntimeException {
  public CustomNotFoundException() {
    super("No se encontraron datos.");
  }

  public CustomNotFoundException(String message) {
    super(message);
  }
}
