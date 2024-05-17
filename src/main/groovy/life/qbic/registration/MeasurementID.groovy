package life.qbic.registration

import groovy.transform.EqualsAndHashCode
import life.qbic.registration.types.QDatasetType

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 * A measurement ID references an openBIS object code used for measurements
 *
 * @since 1.0.0
 */
@EqualsAndHashCode
class MeasurementID {

    /**
     * <p>The regular expression of a valid QBiC measurement code.</p>
     * Prefixes are checked later
     * Z and Y are not allowed, as barcode scanners can switch these depending on country settings.
     */
    static final Pattern QBIC_MEASUREMENT_CODE_SCHEMA = ~/[A-X]*Q[A-X0-9]{4}[0-9]{3}[A-X0-9]{2}-[0-9]{14}/
    /**
     * <p>Holds the project code, which is always part of the object code.</p>
     */
    final String projectCode

    /**
     * <p>technology-specific prefix of the measurement code</p>
     */
    String prefix

    /**
     * <p>the dataset type, for now determined by the prefix</p>
     */
    QDatasetType datasetType
    /**
     * <p>Holds the running sample number.</p>
     *
     * <p>For example if the measurement code is <code>NGSQABCD006AO-25838529214608</code> then the
     * running number of its sample (<code>QABCD006AO</code>) is <code>6</code>.</p>
     */
    final Integer runningNumber
    /**
     * <p>The running digit of the sample code. Necessary if more than 999 samples are needed in a project</p>
     */
    final char runningDigit
    /**
     * <p>The checksum digit of the sample identifier.</p>
     */
    final char checksum
    /**
     * <p>The unique suffix of the measurement.</p>
     */
    final long suffix

    /**
     * <p>Parses a sample identifier from a provided text.</p>
     *
     * <p>If the provided text provides more than one valid sample identifier, only the first one
     * will be returned.</p>
     *
     * <p>If no (valid) sample id was found, the returned {@link Optional} will be empty.</p>
     *
     * @param text the text to search for a valid sample identifier.
     * @return The sample id or empty.
     */
    static Optional<MeasurementID> from(String text) {
        Optional<MeasurementID> sampleId = Optional.empty()

        Matcher matcher = text =~ QBIC_MEASUREMENT_CODE_SCHEMA
        if (matcher.find()) {
            sampleId = Optional.of(new MeasurementID(matcher[0] as String))
        }
        return sampleId
    }

    private MeasurementID() {
    }

    private MeasurementID(String sampleId) {
        findMeasurementType(sampleId)
        String withoutPrefix = sampleId.replace(prefix, '')
        this.projectCode = withoutPrefix[0..4]
        this.runningNumber = Integer.parseInt(withoutPrefix[5..7])
        this.runningDigit = withoutPrefix[8] as char
        this.checksum = withoutPrefix[9] as char
        this.suffix = Long.parseLong(withoutPrefix[11..-1])
    }

    private void findMeasurementType(String id) {
        // we try to find the longest matching prefix, in case their names overlap
        List<String> prefixesByLength = QDatasetType.BY_PREFIX.keySet().stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .collect(Collectors.toList());
        for(String prefix : prefixesByLength) {
            if(id.startsWith(prefix)) {
                this.prefix = prefix
                this.datasetType = QDatasetType.BY_PREFIX.get(prefix)
                return
            }
        }
        throw new RuntimeException("Measurement ID with unknown prefix: "+id);
    }

    /**
     * <p>Provides a formatted String representation of a measurement identifier.
     * This is equivalent to the object code for measurements in openBIS</p>
     *
     * <p>For example, for a Proteomics measurement with project = 'QTEST', a running number = 1,
     * running digit 'A', a checksum of 'E', created with the unique suffix 25838529214608
     * the String representation is:</p>
     *
     * <p align=center><strong>MSQTEST001AE-25838529214608</strong><p>
     *
     * @return
     */
    @Override
    String toString() {
        def runningNumber = runningNumber.toString().padLeft(3, '0')
        return "${prefix}${projectCode}${runningNumber}${runningDigit}${checksum}"+"-"+"${suffix}"
    }

}
