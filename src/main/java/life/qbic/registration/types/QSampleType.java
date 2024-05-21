package life.qbic.registration.types;

import java.util.Arrays;
import java.util.Optional;

/**
 * Controlled vocabulary for openBIS sample types
 *
 * @since 1.0.0
 */
public enum QSampleType {
  Q_NGS_MEASUREMENT,
  Q_PROTEOMICS_MEASUREMENT;

  /**
   * Looks up the enum of a given name. If none match, an empty optional is returned.
   * @param name the name of the enum
   * @return an optional containing the matching enum if any.
   */
  public static Optional<QSampleType> lookup(String name) {
    try {
      return Optional.of(valueOf(name));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
