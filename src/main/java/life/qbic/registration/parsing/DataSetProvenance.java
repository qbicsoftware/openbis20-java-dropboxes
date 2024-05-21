package life.qbic.registration.parsing;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO!
 * <b>short description</b>
 *
 * <p>detailed description</p>
 *
 * @since <version tag>
 */
public record DataSetProvenance(
    @JsonProperty("origin") String origin,
    @JsonProperty("user") String user,
    @JsonProperty("measurementId") String measurementId,
    @JsonProperty("datasetFiles") String[] datasetFiles,
    @JsonProperty("taskId") String taskId,
    @JsonProperty("history") String[] history
) {

}
