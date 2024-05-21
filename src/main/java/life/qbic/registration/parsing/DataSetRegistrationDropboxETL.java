package life.qbic.registration.parsing;

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
import java.util.List;
import life.qbic.registration.types.QDatasetType;
import life.qbic.registration.types.QSampleType;

/**
 * TODO!
 * <b>short description</b>
 *
 * <p>detailed description</p>
 *
 * @since <version tag>
 */
public class DataSetRegistrationDropboxETL extends AbstractJavaDataSetRegistrationDropboxV2 {

  @Override
  public void process(IDataSetRegistrationTransactionV2 transaction) {
    String measurementId = null; //TODO

    ISearchService searchService = transaction.getSearchService();
    SearchCriteria measurementSearchCriteria = new SearchCriteria();
    measurementSearchCriteria.addMatchClause(
        MatchClause.createAttributeMatch(MatchClauseAttribute.CODE, measurementId));
    List<ISampleImmutable> immutableSamples = searchService.searchForSamples(measurementSearchCriteria);

    if (immutableSamples.isEmpty()) {
      //measurement not found
      throw new RuntimeException("Measurement not found");
    }
    if (immutableSamples.size() > 1) {
      throw new RuntimeException("Multiple samples found");
    }
    ISampleImmutable immutableSample = immutableSamples.get(0);
    QSampleType qSampleType = QSampleType.lookup(immutableSample.getSampleType()).orElseThrow(
        () -> new RuntimeException("Unknown sample type: " + immutableSample.getSampleType()));
    QDatasetType qDatasetType = QDatasetType.fromQSampleType(qSampleType);

    if (doesMeasurementHaveData(immutableSample, searchService)) {
      throw new RuntimeException("Measurement %s already has data attached.".formatted(measurementId));
    }

    IDataSet newDataSet = transaction.createNewDataSet();
    newDataSet.setDataSetType(qDatasetType.name());
    newDataSet.setSample(immutableSample);
    newDataSet.setPropertyValue("user", ""); //TODO
    newDataSet.setPropertyValue("taskId", ""); //TODO
    newDataSet.setPropertyValue("history", ""); //TODO

    //TODO move files
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
}
