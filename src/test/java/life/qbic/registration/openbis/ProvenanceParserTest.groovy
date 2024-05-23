package life.qbic.registration.openbis

import spock.lang.Specification

/**
 * TODO!
 * <b>short description</b>
 *
 * <p>detailed description</p>
 *
 * @since <version tag>
 */
class ProvenanceParserTest extends Specification {

    final File validFile = new File(ProvenanceParserTest.class.getClassLoader().getResource("valid-provenance.json").toURI());

    def "parsing just works"() {
        when:
        var resultingProvenanceObject = new ProvenanceParser().parseProvenanceJson(validFile)
        then:
        resultingProvenanceObject.measurementId() == "NGSQTEST001AE-1234512312"
        resultingProvenanceObject.origin() == "/Users/myuser/registration"
        resultingProvenanceObject.datasetFiles() == ["file1.fastq.gz", "file2.fastq.gz"]
        resultingProvenanceObject.user() == "/Users/myuser"
        resultingProvenanceObject.taskId() == "ce36775e-0d06-471e-baa7-1e3b63de871f"
        resultingProvenanceObject.history() == ["/some/dir", "/some/other/dir"]
    }
}
