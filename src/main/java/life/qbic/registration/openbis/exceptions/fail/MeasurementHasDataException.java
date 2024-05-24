package life.qbic.registration.openbis.exceptions.fail;

/**
 * Thrown when a measurement has data already attached and this breaks an assumption.
 */
public class MeasurementHasDataException extends RuntimeException {
  public MeasurementHasDataException(String message) {
    super(message);
  }
}
