package life.qbic.registration.handler

import life.qbic.registration.Provenance

/**
 * <b>DatasetLocatorImpl Class</b>
 *
 * <p>Finds nested datasets.</p>
 *
 * @since 1.3.0
 */
class DatasetLocatorImpl implements DatasetLocator{

    private final String incomingPath
    private final Provenance provenance

    /**
     * Creates an instance of a {@link DatasetLocator}.
     * @param incomingPath - must be an absolute path to the top level directory of the data-structure under investigation
     * @param provenance - provenance information found in the top folder
     */
    static of(String incomingPath, Provenance provenance) {
        if (!incomingPath || !isAbsolutePath(incomingPath)) {
            throw new IllegalArgumentException("You must provide an absolute path. Provided: $incomingPath")
        }
        return new DatasetLocatorImpl(incomingPath, provenance)
    }

    private DatasetLocatorImpl(String incomingPath, Provenance provenance) {
        this.incomingPath = incomingPath
        this.provenance = provenance
    }

    private DatasetLocatorImpl(){}

    /**
     * @inheritDocs
     */
    @Override
    String getPathToDataset() {
        return findNestedDataset().orElseThrow()
    }

    private Optional<String> findNestedDataset() {
        File rootDir = new File(incomingPath)
        if (!rootDir.isDirectory())
            return Optional.empty()
        /*
        Now we iterate through the list of child elements in the folder, and search for the directory name appearances.
        If it is a child directory, that means it has been processed with the dropboxhandler and
        we can return the nested dataset path.
        */
        for (String child : rootDir.list()) {
            if (child.startsWith(provenance.measurementID)) {
                String innerFolderPath = rootDir.getAbsolutePath() + "/" + child
                File innerFolder = new File(innerFolderPath)
                if(innerFolder.list().size() == 1) {
                    String singleFilePath = innerFolderPath+"/"+innerFolder.list()[0]
                    return Optional.of(singleFilePath)
                } else {
                    return Optional.of(innerFolderPath)
                }
            }
        }
        return Optional.empty()
    }

    private static boolean isAbsolutePath(String path) {
        return new File(path).isAbsolute()
    }
}
