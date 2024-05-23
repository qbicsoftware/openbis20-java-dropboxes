package life.qbic.registration.openbis.exceptions.fail;

/**
 * Thrown when the ETL is not able to parse the sample type or no sample type was provided.
 */
public class UnknownSampleTypeException extends RuntimeException {
  public UnknownSampleTypeException(String message) {
    super(message);
  }
}
