package life.qbic.registration.openbis;

import ch.systemsx.cisd.openbis.dss.generic.shared.api.internal.v2.ISampleImmutable;

/**
 * Interface that allows for searching openbis infrastructure
 */
public interface OpenBisSearch {

  /**
   * Searches for a sample with the measurement code in openbis. Assumes that there is one sample and only one; Fails otherwise.
   * @param measurementId the identifier of the measurement
   * @return the existing measurement
   */
  ISampleImmutable getMeasurementSample(String measurementId);

  /**
   * Checks for datasets attached to a measurement sample
   * @param measurementSample
   * @return true if a dataset is contained in the measurement; false otherwise.
   */
  boolean doesMeasurementHaveData(ISampleImmutable measurementSample);

}
