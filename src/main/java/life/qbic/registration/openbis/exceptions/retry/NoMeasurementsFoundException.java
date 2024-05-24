package life.qbic.registration.openbis.exceptions.retry;

import life.qbic.registration.openbis.OpenBisDropboxETL.WithRetryOption;

/**
 * Thrown whenever a measurement was not found even if it is assumed to be there.
 */
public class NoMeasurementsFoundException extends RuntimeException implements
    WithRetryOption {
  public NoMeasurementsFoundException(String message) {
    super(message);
  }
}
