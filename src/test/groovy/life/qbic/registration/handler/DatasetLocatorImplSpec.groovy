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
                new Provenance("origin","user","id",new ArrayList<String>()))

        then:
        thrown(IllegalArgumentException.class)
    }

    def "Given an absolute path, when a DatasetLocator is created then an instance of this class is returned"() {
        when:
        DatasetLocator locator = DatasetLocatorImpl.of(flatDataStructureExample,
                new Provenance("origin","user","id", new ArrayList<String>()))

        then:
        noExceptionThrown()
    }

    def "Given a structure with more than one file, when the path to the dataset folder is requested, return the absolute path to the parent folder"() {
        given:
        DatasetLocator locator = DatasetLocatorImpl.of(multiFileDataStructureExample,
                new Provenance("origin","user","NGSQABCD006AO-25838529214608",new ArrayList<String>()))

        when:
        String datasetPath = locator.getPathToDataset()

        then:
        new File(datasetPath).toString().equalsIgnoreCase(multiFileContainer)
    }

    def "Given a flat structure, when the path to the dataset folder is requested, return the given path"() {
        given:
        DatasetLocator locator = DatasetLocatorImpl.of(flatDataStructureExample,
                new Provenance("origin","user","NGSQABCD006AO-25838529214608",new ArrayList<String>()))

        when:
        String datasetPath = locator.getPathToDataset()

        then:
        datasetPath == singleDatasetFile
    }

    def setupMultiFileDataset() {
        File file = File.createTempDir()

        File dataFolders = new File(file.getAbsolutePath()+
                '/NGSQABCD006AO-25838529214608/NGSQABCD006AO-25838529214608_dataset')
        dataFolders.mkdirs()

        multiFileContainer = dataFolders.getAbsolutePath()

        new File(dataFolders.getAbsolutePath() + File.separator + "NGSQABCD006AO_left.fastq").createNewFile()
        new File(dataFolders.getAbsolutePath() + File.separator + "NGSQABCD006AO_right.fastq").createNewFile()
        new File(file.getAbsolutePath() + File.separator + "provenance.json").createNewFile()

        multiFileDataStructureExample = file.getAbsolutePath()
    }

    def setupRelativePathExample() {
        File file = File.createTempDir()
        relativePathExample = file.getName()
    }

    def setupFlatDatasetExample() {
        File file = File.createTempDir()

        File dataFolders = new File(file.getAbsolutePath()+'/NGSQABCD006AO-25838529214608.fastq_dataset')
        dataFolders.mkdirs()

        File singleFile = new File(dataFolders.getAbsolutePath() + File.separator + "NGSQABCD006AO-25838529214608.fastq")
        singleFile.createNewFile()
        singleDatasetFile = singleFile.getAbsolutePath()

        new File(file.getAbsolutePath() + File.separator + "provenance.json").createNewFile()

        flatDataStructureExample = file.getAbsolutePath()
    }

}
