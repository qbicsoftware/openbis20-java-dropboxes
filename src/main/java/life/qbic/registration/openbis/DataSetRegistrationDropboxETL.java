package life.qbic.registration.openbis;

import ch.systemsx.cisd.common.exceptions.NotImplementedException;
import ch.systemsx.cisd.etlserver.registrator.DataSetRegistrationContext;
import ch.systemsx.cisd.etlserver.registrator.api.v2.AbstractJavaDataSetRegistrationDropboxV2;
import ch.systemsx.cisd.etlserver.registrator.api.v2.IDataSet;
import ch.systemsx.cisd.etlserver.registrator.api.v2.IDataSetRegistrationTransactionV2;
import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.IDataSetImmutable;
import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.ISampleImmutable;
import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.ISearchService;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchCriteria;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchCriteria.MatchClause;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchCriteria.MatchClauseAttribute;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchSubCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import life.qbic.registration.openbis.types.QDatasetType;
import life.qbic.registration.openbis.types.QPropertyType;
import life.qbic.registration.openbis.types.QSampleType;

/**
 * TODO!
 * <b>short description</b>
 *
 * <p>detailed description</p>
 *
 * @since <version tag>
 */
public class DataSetRegistrationDropboxETL extends AbstractJavaDataSetRegistrationDropboxV2 {

  private final String provenanceFileName = "provenance.json";


  public interface WithRetryOption {
  }

  @Override
  public boolean shouldRetryProcessing(DataSetRegistrationContext context, Exception problem)
      throws NotImplementedException {
    return problem instanceof WithRetryOption || super.shouldRetryProcessing(context, problem);
  }

  @Override
  public void process(IDataSetRegistrationTransactionV2 transaction) {
    DataSetProvenance dataSetProvenance = parseProvenanceJson(
        new File(transaction.getIncoming(), provenanceFileName));

    String measurementId = dataSetProvenance.measurementId();

    ISearchService searchService = transaction.getSearchService();

    ISampleImmutable measurementSample = findMeasurementSample(measurementId, searchService);

    if (doesMeasurementHaveData(measurementSample, searchService)) {
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

  private static ISampleImmutable findMeasurementSample(String measurementId,
      ISearchService searchService) {
    SearchCriteria measurementSearchCriteria = new SearchCriteria();
    measurementSearchCriteria.addMatchClause(
        MatchClause.createAttributeMatch(MatchClauseAttribute.CODE, measurementId));
    List<ISampleImmutable> immutableSamples = searchService.searchForSamples(measurementSearchCriteria);

    if (immutableSamples.isEmpty()) {
      //measurement not found
      throw new NoMeasurementsFoundException("Measurement '" + measurementId + "' not found");
    }
    if (immutableSamples.size() > 1) {
      throw new ToManyMeasurementsException(
          "Multiple measurement with id '" + measurementId + "' found");
    }
    return immutableSamples.get(0);
  }

  private boolean doesMeasurementHaveData(ISampleImmutable sample, ISearchService searchService) {
    SearchCriteria parentSampleSearchCriteria = new SearchCriteria();
    parentSampleSearchCriteria.addMatchClause(
        MatchClause.createAttributeMatch(MatchClauseAttribute.PERM_ID, sample.getPermId()));

    SearchCriteria dataSetSearchCriteria = new SearchCriteria();
    dataSetSearchCriteria.addSubCriteria(
        SearchSubCriteria.createDataSetContainerCriteria(parentSampleSearchCriteria));
    List<IDataSetImmutable> existingDataSets = searchService.searchForDataSets(
        dataSetSearchCriteria);
    return !existingDataSets.isEmpty();

  }

  private DataSetProvenance parseProvenanceJson(File provenanceFile) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(provenanceFile,
          DataSetProvenance.class);
    } catch (IOException e) {
      throw new ProvenanceParseException(
          "Could not parse '" + provenanceFile.getAbsolutePath() + "'", e);
    }
  }

  public static class NoMeasurementsFoundException extends RuntimeException implements
      WithRetryOption {
    public NoMeasurementsFoundException(String message) {
      super(message);
    }
  }

  public static class ProvenanceParseException extends RuntimeException {

    public ProvenanceParseException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  public static class UnknownSampleTypeException extends RuntimeException {
    public UnknownSampleTypeException(String message) {
      super(message);
    }
  }

  public static class ToManyMeasurementsException extends RuntimeException {
    public ToManyMeasurementsException(String message) {
      super(message);
    }
  }

  public static class MeasurementHasDataException extends RuntimeException {
    public MeasurementHasDataException(String message) {
      super(message);
    }
  }

}
