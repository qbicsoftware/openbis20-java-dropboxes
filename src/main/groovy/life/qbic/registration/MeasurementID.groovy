package life.qbic.registration

import groovy.transform.EqualsAndHashCode
import life.qbic.registration.types.QDatasetType

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * A measurement ID references an openBIS object code used for measurements
 *
 * @since 1.0.0
 */
@EqualsAndHashCode
class MeasurementID {

    /**
     * <p>The regular expression of a valid QBiC measurement code.</p>
     */
    static final Pattern QBIC_MEASUREMENT_CODE_SCHEMA = ~/N*[GM]SQ[A-X0-9]{4}[0-9]{3}[A-X0-9]{2}-[0-9]{14}/
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
     * <p>The timestamp of the measurement object, making measurements from the same sample unique.</p>
     */
    final long timestamp

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
        this.runningDigit = withoutPrefix[8]
        this.checksum = withoutPrefix[9]
        this.timestamp = Long.parseLong(withoutPrefix[11..-1])
    }

    private void findMeasurementType(String id) {
        for(String prefix : QDatasetType.BY_PREFIX.keySet()) {
            if(id.startsWith(prefix)) {
                this.prefix = prefix
                this.datasetType = QDatasetType.BY_PREFIX.get(prefix)
                return
            }
        }
        throw new RuntimeException("Measurement ID with unknown prefix: "+id);
    }

    static void main(String[] args) {
        String ngs = "NGSQ678G006AO-25838529214608"
        String ms = "MSQ678G006AO-11166089468305"
        println(MeasurementID.from(ngs).toString())
        println(MeasurementID.from(ms).toString())
        println(ms)
        println(ngs)
        MeasurementID x = MeasurementID.from("wrong").toString()
    }

    /**
     * <p>Provides a formatted String representation of a sample identifier.</p>
     *
     * <p>For example an object with project = 'QTEST', a running number = 1 and a
     * checksum of 'AE', the String representation is:</p>
     *
     * <p align=center><strong>QTEST001AE</strong><p>
     *
     * @return
     */
    @Override
    String toString() {
        def runningNumber = runningNumber.toString().padLeft(3, '0')
        return "${prefix}${projectCode}${runningNumber}${runningDigit}${checksum}"+"-"+"${timestamp}"
    }

}
