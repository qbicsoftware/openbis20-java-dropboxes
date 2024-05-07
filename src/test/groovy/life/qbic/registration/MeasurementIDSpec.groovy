package life.qbic.registration

import life.qbic.registration.types.QDatasetType
import spock.lang.Specification

/**
 * Tests for the {@link MeasurementID} class.
 *
 * @since 1.0.0
 */
class MeasurementIDSpec extends Specification {

    def "Creating a measurement id object from a text containing a valid id must create a valid MeasurementID object"() {
        given: "A text with one valid measurement id"
        String ngs = "NGSQ678G006AO-25838529214608"

        when: "We parse a sample id from the text"
        Optional<MeasurementID> sampleId = MeasurementID.from(ngs)

        then: "The id must not be empty"
        sampleId.isPresent()
        MeasurementID id = sampleId.get()
        id.runningNumber == 6
        id.projectCode.toString() == "Q678G"
        id.datasetType == QDatasetType.Q_NGS_RAW_DATA
    }

    def "Creating a measurement id object from a text containing two valid ids must return the first valid measurementID"() {
        given: "A text with two valid sample ids"
        String ms = "MSQ678G077AO-11166089468305"
        String ngs = "NGSQ678G006AO-25838529214608"

        def validId = " "+ms+" or maybe "+ngs

        when: "We parse a measurement id from the text"
        Optional<MeasurementID> sampleId = MeasurementID.from(validId)

        then: "The id must be the first valid occurrence of a measurement id in the text"
        MeasurementID id = sampleId.get()
        id.runningNumber == 77
        id.projectCode.toString() == "Q678G"
        id.datasetType == QDatasetType.Q_PROTEOMICS_RAW_DATA
    }

    def "Creating a measurement id object from a text containing no valid ids must return an empty object"() {
        given: "A text with two invalid sample ids"
        def invalidIds = " MSQ678G077AO or maybe NGSQ678G006AO"

        when: "We parse a sample id from the text"
        Optional<MeasurementID> sampleId = MeasurementID.from(invalidIds)

        then: "The id must be empty"
        !sampleId.isPresent()
    }

    def "Ensure equality check works"() {
        given: "Two sample Ids with the same content, and one with different content"
        def oneSampleId = MeasurementID.from("NGSQ678G006AO-25838529214608")
        def sameSampleId = MeasurementID.from("NGSQ678G006AO-25838529214608")
        def otherSampleId = MeasurementID.from("NGSQ678G006AO-25123123529214")

        when: "we compare them"
        boolean sameSamplesEqual = oneSampleId.equals(sameSampleId)
        boolean differentSamplesEqual = otherSampleId.equals(oneSampleId)

        then: "must evaluate as such during comparison"
        sameSamplesEqual
        !differentSamplesEqual
    }

    def "Ensure toString method produces expected format"() {
        given: "A sample id"
        def sampleId = MeasurementID.from("NGSQ678G006AO-25838529214608")

        when: "we call the toString() method"
        String result = sampleId.get().toString()

        then: "we verify the format to be as expected"
        result == "NGSQ678G006AO-25838529214608"
    }
}
