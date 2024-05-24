package life.qbic.registration.openbis

import spock.lang.Specification

class ProvenanceParserTest extends Specification {

    final File validFile = new File(ProvenanceParserTest.class.getClassLoader().getResource("valid-provenance.json").toURI());

    def "parsing a valid file works"() {
        when:
        var resultingProvenanceObject = new ProvenanceParser().parseProvenanceJson(validFile)
        then:
        resultingProvenanceObject.measurementId() == "NGSQTEST001AE-1234512312"
        and:
        resultingProvenanceObject.origin() == "/Users/myuser/registration"
        and:
        resultingProvenanceObject.datasetFiles() == ["file1.fastq.gz", "file2.fastq.gz"]
        and:
        resultingProvenanceObject.user() == "/Users/myuser"
        and:
        resultingProvenanceObject.taskId() == "ce36775e-0d06-471e-baa7-1e3b63de871f"
        and:
        resultingProvenanceObject.history() == ["/some/dir", "/some/other/dir"]
    }
}
