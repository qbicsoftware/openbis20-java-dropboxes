package life.qbic.registration.openbis.exceptions.fail;

/**
 * Thrown when the provenance file does not match the expected format.
 */
public class ProvenanceParseException extends RuntimeException {
  public ProvenanceParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
