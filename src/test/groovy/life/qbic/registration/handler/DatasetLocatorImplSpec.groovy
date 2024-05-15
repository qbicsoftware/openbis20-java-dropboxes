package life.qbic.registration.handler

import life.qbic.registration.Provenance
import spock.lang.Shared
import spock.lang.Specification

class DatasetLocatorImplSpec extends Specification {

    @Shared
    String multiFileDataStructureExample

    @Shared
    String relativePathExample

    @Shared
    String flatDataStructureExample

    @Shared
    String multiFileContainer

    @Shared
    String singleDatasetFile

    def setup() {
        setupMultiFileDataset()
        setupRelativePathExample()
        setupFlatDatasetExample()
    }

    def "Given a relative path, when a DatasetLocator is created then an IllegalArgumentException is thrown"() {
        when:
        DatasetLocator locator = DatasetLocatorImpl.of(relativePathExample,
                new Provenance("origin","user","id",new ArrayList<String>(),
                        new ArrayList<String>()))

        then:
        thrown(IllegalArgumentException.class)
    }

    def "Given an absolute path, when a DatasetLocator is created then an instance of this class is returned"() {
        when:
        DatasetLocator locator = DatasetLocatorImpl.of(flatDataStructureExample,
                new Provenance("origin","user","id", new ArrayList<String>(),
                        new ArrayList<String>()))

        then:
        noExceptionThrown()
    }

    def "Given a structure with more than one file, when the path to the dataset folder is requested, return the absolute path to the parent folder"() {
        given:
        DatasetLocator locator = DatasetLocatorImpl.of(multiFileDataStructureExample,
                new Provenance("origin","user","NGSQABCD006AO-25838529214608",
                        new ArrayList<String>(Arrays.asList("NGSQABCD006AO_left.fastq",
                                "NGSQABCD006AO_right.fastq")),new ArrayList<String>()))

        when:
        String datasetPath = locator.getPathToDataset()

        then:
        new File(datasetPath).toString().equalsIgnoreCase(multiFileContainer)
    }

    def "Given a flat structure, when the path to the dataset folder is requested, return the given path"() {
        given:
        DatasetLocator locator = DatasetLocatorImpl.of(flatDataStructureExample,
                new Provenance("origin","user","NGSQABCD006AO-25838529214608",
                        new ArrayList<String>(Arrays.asList("NGSQABCD006AO-25838529214608.fastq")),
                        new ArrayList<String>()))

        when:
        String datasetPath = locator.getPathToDataset()

        then:
        datasetPath == singleDatasetFile
    }

    def setupMultiFileDataset() {
        File tempDir = File.createTempDir()

        multiFileContainer = tempDir.getAbsolutePath() + File.separator + "data"

                new File(tempDir.getAbsolutePath() + File.separator + "NGSQABCD006AO_left.fastq").createNewFile()
        new File(tempDir.getAbsolutePath() + File.separator + "NGSQABCD006AO_right.fastq").createNewFile()
        new File(tempDir.getAbsolutePath() + File.separator + "provenance.json").createNewFile()

        multiFileDataStructureExample = tempDir.getAbsolutePath()
    }

    def setupRelativePathExample() {
        File file = File.createTempDir()
        relativePathExample = file.getName()
    }

    def setupFlatDatasetExample() {
        File tempDir = File.createTempDir()

        File singleFile = new File(tempDir.getAbsolutePath() + File.separator + "NGSQABCD006AO-25838529214608.fastq")
        singleFile.createNewFile()
        singleDatasetFile = singleFile.getAbsolutePath()

        new File(tempDir.getAbsolutePath() + File.separator + "provenance.json").createNewFile()

        flatDataStructureExample = tempDir.getAbsolutePath()
    }

}
