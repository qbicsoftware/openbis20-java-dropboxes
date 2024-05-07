package life.qbic.registration.registries

import ch.systemsx.cisd.etlserver.registrator.api.v2.IDataSetRegistrationTransactionV2
import ch.systemsx.cisd.etlserver.registrator.api.v2.ISample
import groovy.util.logging.Log4j2

import life.qbic.registration.Context
import life.qbic.registration.MeasurementID
import life.qbic.registration.handler.RegistrationException
import life.qbic.registration.handler.Registry
import life.qbic.registration.registries.shared.RegistrationContextHandler
import life.qbic.registration.registries.shared.SampleIdHandler

import java.nio.file.Path

/**
 * <p>Registration of raw data datasets.</p>
 *
 * <p>This registry is supposed to register a raw data dataset in openBIS,
 * based on a pre-defined model (please checkout the README for the detailed model).</p>
 *
 * @since 1.1.0
 */
@Log4j2
class DefaultResultRegistry implements Registry{

    private Path datasetRootPath

    private final String measurementID

    /**
     * @param measurementID the measurement ID of the dataset to register
     * @since 1.1.0
     */
    DefaultResultRegistry(String measurementID) {
        this.measurementID = measurementID
    }

    /**
     * {@inheritDocs}
     */
    @Override
    void executeRegistration(IDataSetRegistrationTransactionV2 transaction, Path datasetRootPath) throws RegistrationException {
        this.datasetRootPath = datasetRootPath
        try {
            register(transaction)
        } catch (RuntimeException e) {
            log.error("An exception occurred during the registration.")
            throw new RegistrationException(e.getMessage())
        }
    }

    private void register(IDataSetRegistrationTransactionV2 transaction) {
        // 1. Get the openBIS samples the dataset belongs to
        MeasurementID sampleCode = SampleIdHandler.parseMeasurementID(measurementID)

        Context context = RegistrationContextHandler.getContext(sampleCode, transaction.getSearchService()).orElseThrow({
            throw new RegistrationException(("Could not determine context for sample ${measurementID}"))
        })

        // We grep the openBIS samples, the analysis was based on
        ISample sample = transaction.getSampleForUpdate("/${context.getProjectSpace()}/$measurementID")

        // 8. Create new openBIS dataset
        def dataset = transaction.createNewDataSet(sampleCode.datasetType as String)
        dataset.setSample(sample)

        // 9. Attach result data to dataset
        transaction.moveFile(this.datasetRootPath.toString(), dataset)
    }
}
