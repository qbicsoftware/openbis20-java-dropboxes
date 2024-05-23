package life.qbic.registration.openbis;

import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.IDataSetImmutable;
import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.ISampleImmutable;
import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.ISearchService;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchCriteria;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchCriteria.MatchClause;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchCriteria.MatchClauseAttribute;
import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.SearchSubCriteria;
import java.util.List;
import life.qbic.registration.openbis.exceptions.fail.ToManyMeasurementsException;
import life.qbic.registration.openbis.exceptions.retry.NoMeasurementsFoundException;

/**
 * Implements the Openbis access
 */
public class OpenBisSearchImpl implements OpenBisSearch {

  private final ISearchService searchService;

  public OpenBisSearchImpl(ISearchService searchService) {
    this.searchService = searchService;
  }

  @Override
  public ISampleImmutable getMeasurementSample(String measurementId) {
    return findMeasurementSample(measurementId, searchService);
  }

  private static ISampleImmutable findMeasurementSample(String measurementId,
      ISearchService searchService) {
    SearchCriteria measurementSearchCriteria = new SearchCriteria();
    measurementSearchCriteria.addMatchClause(
        MatchClause.createAttributeMatch(MatchClauseAttribute.CODE, measurementId));
    List<ISampleImmutable> immutableSamples = searchService.searchForSamples(
        measurementSearchCriteria);

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

  @Override
  public boolean doesMeasurementHaveData(ISampleImmutable sample) {
    return doesMeasurementHaveData(sample, searchService);
  }

  private static boolean doesMeasurementHaveData(ISampleImmutable sample,
      ISearchService searchService) {
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
