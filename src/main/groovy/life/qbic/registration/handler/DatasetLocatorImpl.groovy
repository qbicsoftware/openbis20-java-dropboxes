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
        return findOrCreateDataPath().orElseThrow()
    }

    private Optional<String> findOrCreateDataPath() {
        File rootDir = new File(incomingPath)
        if (!rootDir.isDirectory())
            return Optional.empty()
        List<String> fileNames = provenance.datasetFiles;
        if(fileNames.size() == 0)
            return Optional.empty()
        if(fileNames.size() == 1)
            return Optional.of(rootDir.getAbsolutePath() + "/" + fileNames[0])
        // more than one file
        var newDataFolder = new File(rootDir.getAbsolutePath(), "data");
        newDataFolder.mkdir();
        for(String fileName : fileNames) {
            String targetPath = newDataFolder.getAbsolutePath()+"/"+fileName
            new File(rootDir.getAbsolutePath()+"/"+fileName).renameTo(targetPath)
        }
        return Optional.of(newDataFolder.getAbsolutePath())
    }

    private static boolean isAbsolutePath(String path) {
        return new File(path).isAbsolute()
    }
}
