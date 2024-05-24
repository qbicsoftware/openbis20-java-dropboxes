package life.qbic.registration.openbis.types;

import java.util.Objects;
import life.qbic.registration.openbis.exceptions.fail.DatasetTypeMappingException;

/**
 * Controlled vocabulary for openBiS dataset types
 *
 * @since 1.0.0
 */
public enum QDatasetType {

  Q_NGS_RAW_DATA("Q_NGS_RAW_DATA"),
  Q_PROTEOMICS_RAW_DATA("Q_PROTEOMICS_RAW_DATA"),
  ;

  private final String openBisPropertyName;

  QDatasetType(String openBisPropertyName) {
    this.openBisPropertyName = openBisPropertyName;
  }

  public String getOpenBisPropertyName() {
    return openBisPropertyName;
  }

  public static QDatasetType fromQSampleType(QSampleType qSampleType) {
//  If this was Java 17+ the following code would easily solve the issue.
    /*
    return switch (qSampleType) {
      case Q_NGS_MEASUREMENT -> Q_NGS_RAW_DATA;
      case Q_PROTEOMICS_MEASUREMENT -> Q_PROTEOMICS_RAW_DATA;
    };
    */

    if (Objects.requireNonNull(qSampleType) == QSampleType.Q_NGS_MEASUREMENT) {
      return Q_NGS_RAW_DATA;
    } else if (qSampleType == QSampleType.Q_PROTEOMICS_MEASUREMENT) {
      return Q_PROTEOMICS_RAW_DATA;
    }
    throw new DatasetTypeMappingException("Unknown sample type to dataset type mapping. Cannot map " + qSampleType);
  }

}
