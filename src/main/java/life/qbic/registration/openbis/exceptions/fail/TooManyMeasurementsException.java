package life.qbic.registration.openbis.exceptions.fail;

/**
 * Thrown when multiple measurements exist where only one is expected.
 */
public class TooManyMeasurementsException extends RuntimeException {
  public TooManyMeasurementsException(String message) {
    super(message);
  }
}
