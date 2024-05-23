package life.qbic.registration.openbis.exceptions.fail;

/**
 * Thrown when multiple measurements exist where only one is expected.
 */
public class ToManyMeasurementsException extends RuntimeException {
  public ToManyMeasurementsException(String message) {
    super(message);
  }
}
