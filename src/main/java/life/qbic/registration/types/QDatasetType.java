package life.qbic.registration.types;

import java.util.Arrays;
import java.util.Optional;

/**
 * Controlled vocabulary for openBiS dataset types
 * @since 1.0.0
 */
public enum QDatasetType {

  Q_NGS_RAW_DATA,
  Q_PROTEOMICS_RAW_DATA;

  public static QDatasetType fromQSampleType(QSampleType qSampleType) {
    return switch (qSampleType) {
      case Q_NGS_MEASUREMENT -> Q_NGS_RAW_DATA;
      case Q_PROTEOMICS_MEASUREMENT -> Q_PROTEOMICS_RAW_DATA;
    };
  }
}
