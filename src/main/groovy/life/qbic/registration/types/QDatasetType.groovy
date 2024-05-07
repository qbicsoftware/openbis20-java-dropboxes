package life.qbic.registration.types

/**
 * <p>Controlled vocabulary for openBIS dataset types</p>
 *
 * @since 1.0.0
 */
enum QDatasetType {

    Q_NGS_RAW_DATA("NGS"),
    Q_PROTEOMICS_RAW_DATA("MS")

    public final String prefix

    private QDatasetType(String prefix) {
        this.prefix = prefix
    }

    public static final Map<String, QDatasetType> BY_PREFIX = new HashMap<>();

    static {
        for (QDatasetType e: values()) {
            BY_PREFIX.put(e.prefix, e);
        }
    }

}
