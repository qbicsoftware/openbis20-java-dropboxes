package life.qbic.registration.openbis.types;

/**
 * Controlled vocabulary for openBiS dataset types
 * @since 1.0.0
 */
public enum QDatasetType {

  Q_NGS_RAW_DATA("Q_NGS_RAW_DATA"),
  Q_PROTEOMICS_RAW_DATA("Q_PROTEOMICS_RAW_DATA"),;

  private final String openBisPropertyName;

  QDatasetType(String openBisPropertyName) {
    this.openBisPropertyName = openBisPropertyName;
  }

  public String getOpenBisPropertyName() {
    return openBisPropertyName;
  }

  public static QDatasetType fromQSampleType(QSampleType qSampleType) {
    return switch (qSampleType) {
      case Q_NGS_MEASUREMENT -> Q_NGS_RAW_DATA;
      case Q_PROTEOMICS_MEASUREMENT -> Q_PROTEOMICS_RAW_DATA;
    };
  }
}
