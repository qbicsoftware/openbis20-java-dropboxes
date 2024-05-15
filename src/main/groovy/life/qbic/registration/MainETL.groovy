package life.qbic.registration

import ch.systemsx.cisd.etlserver.registrator.api.v2.AbstractJavaDataSetRegistrationDropboxV2
import ch.systemsx.cisd.etlserver.registrator.api.v2.IDataSetRegistrationTransactionV2
import groovy.util.logging.Log4j2
import life.qbic.registration.handler.*
import life.qbic.registration.registries.DefaultResultRegistry

import java.nio.file.Paths

@Log4j2
class MainETL extends AbstractJavaDataSetRegistrationDropboxV2 {

    @Override
    void process(IDataSetRegistrationTransactionV2 transaction) {
        String incomingPath = transaction.getIncoming().getAbsolutePath()
        String provenancePath = new File(incomingPath, "provenance.json").getAbsolutePath()
        String jsonProvenance = new File(provenancePath).getText('UTF-8')
        Provenance provenance = Provenance.fromJson(jsonProvenance);

        DatasetLocator locator = DatasetLocatorImpl.of(incomingPath, provenance)
        String pathToDataset = locator.getPathToDataset()

        Registry registry = new DefaultResultRegistry(provenance.measurementID)
        try {
            registry.executeRegistration(transaction, Paths.get(pathToDataset))
        } catch (RegistrationException e) {
            log.error(e.getMessage())
            throw new RegistrationException("Could not register data! Manual intervention is needed.")
        }
    }

}
