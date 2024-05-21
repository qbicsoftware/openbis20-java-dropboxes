package life.qbic.registration.parsing;

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.DataSet;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.fetchoptions.DataSetFetchOptions;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.search.DataSetSearchCriteria;

/**
 * TODO!
 * <b>short description</b>
 *
 * <p>detailed description</p>
 *
 * @since <version tag>
 */
public class FindMeasurement {

  public Measurement findMeasurement(String measurementId,
      IApplicationServerApi applicationServerApi) {
    String sessionToken;
    SearchResult<DataSet> dataSetSearchResult = applicationServerApi.searchDataSets(sessionToken,
        new DataSetSearchCriteria(),
        new DataSetFetchOptions());
  }
}
