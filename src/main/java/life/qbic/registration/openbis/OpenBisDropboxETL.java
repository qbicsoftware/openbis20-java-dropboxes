package life.qbic.registration.openbis;

import static java.util.Objects.requireNonNull;

import ch.systemsx.cisd.common.exceptions.NotImplementedException;
import ch.systemsx.cisd.etlserver.registrator.DataSetRegistrationContext;
import ch.systemsx.cisd.etlserver.registrator.api.v2.AbstractJavaDataSetRegistrationDropboxV2;
import ch.systemsx.cisd.etlserver.registrator.api.v2.IDataSet;
import ch.systemsx.cisd.etlserver.registrator.api.v2.IDataSetRegistrationTransactionV2;
import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.ISampleImmutable;
import java.io.File;
import life.qbic.registration.openbis.exceptions.fail.MeasurementHasDataException;
import life.qbic.registration.openbis.exceptions.fail.UnknownSampleTypeException;
import life.qbic.registration.openbis.types.QDatasetType;
import life.qbic.registration.openbis.types.QPropertyType;
import life.qbic.registration.openbis.types.QSampleType;

/**
 * The Dropbox ETL process.
 * <p>
 * This process is responsible for
 *  <ol>
 *    <li>fetching the measurement sample from openbis
 *    <li>creating a data set linked to the sample
 *    <li>moving files into the dataset
 *  </ol>
 *  Some constraints are taken care of during this process. These constraints being:
 *  <ul>
 *    <li>the measurement sample already exists in openbis
 *    <li>only one measurement sample exists with the provided identifier
 *    <li>the measurement sample has no data set linked as one measurement can only have one dataset.
 *  </ul>
 *
 * @version 1.0.0
 */
public class OpenBisDropboxETL extends AbstractJavaDataSetRegistrationDropboxV2 {

  private static final String PROVENANCE_FILE_NAME = "provenance.json";

  public OpenBisDropboxETL(){
  }


  public interface WithRetryOption {
  }

  @Override
  public boolean shouldRetryProcessing(DataSetRegistrationContext context, Exception problem)
      throws NotImplementedException {
    return problem instanceof WithRetryOption || super.shouldRetryProcessing(context, problem);
  }

  @Override
  public void process(IDataSetRegistrationTransactionV2 transaction) {
    DataSetProvenance dataSetProvenance = ProvenanceParser.parseProvenanceJson(
        new File(transaction.getIncoming(), PROVENANCE_FILE_NAME));

    String measurementId = dataSetProvenance.measurementId();

    OpenBisSearchImpl openBisSearch = new OpenBisSearchImpl(transaction.getSearchService());

    ISampleImmutable measurementSample = openBisSearch.getMeasurementSample(measurementId);

    if (openBisSearch.doesMeasurementHaveData(measurementSample)) {
      throw new MeasurementHasDataException("Measurement " + measurementId + " has data attached.");
    }
    IDataSet newDataSet = transaction.createNewDataSet();
    newDataSet.setSample(measurementSample);
    newDataSet.setPropertyValue(QPropertyType.Q_SUBMITTER.getOpenBisPropertyName(), dataSetProvenance.user());
    newDataSet.setPropertyValue(QPropertyType.Q_TASK_ID.getOpenBisPropertyName(), dataSetProvenance.taskId());
    QDatasetType qDatasetType = getDatasetType(measurementSample);
    newDataSet.setDataSetType(qDatasetType.name());

    transaction.moveFile(transaction.getIncoming().getAbsolutePath(), newDataSet);
  }

  private static QDatasetType getDatasetType(ISampleImmutable measurementSample) {
    QSampleType qSampleType = QSampleType.lookup(measurementSample.getSampleType())
        .orElseThrow(() -> new UnknownSampleTypeException(
            "Unknown sample type: " + measurementSample.getSampleType()));
    return QDatasetType.fromQSampleType(qSampleType);
  }
}
