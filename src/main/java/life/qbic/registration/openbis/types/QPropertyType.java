package life.qbic.registration.openbis.types;

import java.util.Optional;

/**
 * Controlled vocabulary for openBIS property types
 *
 * @since 1.0.0
 */
public enum QPropertyType {
  Q_SUBMITTER("Q_SUBMITTER"),
  Q_TASK_ID("Q_TASK_ID"),
  ;

  private final String openBisPropertyName;

  QPropertyType(String openBisPropertyName) {
    this.openBisPropertyName = openBisPropertyName;
  }

  public String getOpenBisPropertyName() {
    return openBisPropertyName;
  }

  /**
   * Looks up the enum of a given name. If none match, an empty optional is returned.
   * @param name the name of the enum
   * @return an optional containing the matching enum if any.
   */
  public static Optional<QPropertyType> lookup(String name) {
    try {
      return Optional.of(valueOf(name));
    } catch (IllegalArgumentException e) {
      for (var value : values()) {
        if (value.openBisPropertyName.equals(name)) {
          return Optional.of(value);
        }
      }
    }
    return Optional.empty();
  }
}
