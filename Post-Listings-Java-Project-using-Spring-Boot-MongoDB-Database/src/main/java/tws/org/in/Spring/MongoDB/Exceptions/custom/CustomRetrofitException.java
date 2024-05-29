package tws.org.in.Spring.MongoDB.Exceptions.custom;

public class CustomRetrofitException extends RuntimeException {
  public CustomRetrofitException() {
    super("Error en servicio retrofit.");
  }

  public CustomRetrofitException(String message) {
    super(message);
  }
}
