package anton.asmirko.app.cli.exception;

public class IllegalCommandException extends RuntimeException {
  public IllegalCommandException(String message) {
    super(message);
  }
}
