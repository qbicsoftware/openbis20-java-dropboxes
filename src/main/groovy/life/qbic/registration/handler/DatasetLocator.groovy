package life.qbic.registration.handler

/**
 * Returns the actual dataset folder in cases of complex datastructures,
 * that have been created upon data transfer and has been pre-processed
 * by the dropboxhandler.
 *
 * The Dropbhoxhandler wraps the actual dataset in another folder and provides additional
 * data, like checksums and the original incoming dropbox aka the client institution that has transferred
 * the dataset in the first place.
 *
 * Example:
 *
 *  |- mydataset_folder
 *         |- data1.txt
 *         |- data2.txt
 *
 *  becomes:
 *
 *  |- mydataset_folder
 *      |- source_dropbox.txt
 *      |- mydataset_folder
 *            |- data1.txt
 *            |- data2.txt
 *
 * @since 1.3.0
 */
interface DatasetLocator {

    /**
     * Returns the top level absolute path of the dataset.
     * @return the absolute dataset path
     * @since 1.3.0
     */
    String getPathToDataset()
}
