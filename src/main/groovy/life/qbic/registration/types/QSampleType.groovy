package life.qbic.registration.types

/**
 * <p>Controlled vocabulary for openBIS sample types</p>
 *
 * @since 1.0.0
 */
enum QSampleType {

    Q_NGS_MEASUREMENT("NGS"),
    Q_PROTEOMICS_MEASUREMENT("MS")

    public final String prefix

    private QSampleType(String prefix) {
        this.prefix = prefix
    }

    public static final Map<String, QSampleType> BY_PREFIX = new HashMap<>();

    static {
        for (QSampleType e: values()) {
            BY_PREFIX.put(e.prefix, e);
        }
    }
}
