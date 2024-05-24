package life.qbic.registration.openbis.exceptions.fail;

/**
 * Thrown when the dataset type cannot be determined.
 */
public class DatasetTypeMappingException extends RuntimeException {
  public DatasetTypeMappingException(String message) {
    super(message);
  }
}
