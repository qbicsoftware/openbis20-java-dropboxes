package life.qbic.registration.openbis;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class holding provenance information provided by the user. This is a record of immutable data.
 *
 * The provenance file contains information about the
 *
 * @since <version tag>
 */
public class DataSetProvenance implements Serializable {

  private static final long serialVersionUID = -1597156104025439195L;

  @JsonProperty("origin")
  private String origin;
  @JsonProperty("user")
  private String user;
  @JsonProperty("measurementId")
  private String measurementId;
  @JsonProperty("datasetFiles")
  private String[] datasetFiles;
  @JsonProperty("taskId")
  private String taskId;
  @JsonProperty("history")
  private String[] history;


  private DataSetProvenance() {

  }
  public DataSetProvenance(String origin, String user, String measurementId, String[] datasetFiles,
      String taskId, String[] history) {
    this.origin = origin;
    this.user = user;
    this.measurementId = measurementId;
    this.datasetFiles = datasetFiles;
    this.taskId = taskId;
    this.history = history;
  }

  public String origin() {
    return origin;
  }

  public String user() {
    return user;
  }

  public String measurementId() {
    return measurementId;
  }

  public String[] datasetFiles() {
    return datasetFiles;
  }

  public String taskId() {
    return taskId;
  }

  public String[] history() {
    return history;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DataSetProvenance)) {
      return false;
    }

    DataSetProvenance that = (DataSetProvenance) o;
    return Objects.equals(origin, that.origin) && Objects.equals(user, that.user)
        && Objects.equals(measurementId, that.measurementId) && Arrays.equals(
        datasetFiles, that.datasetFiles) && Objects.equals(taskId, that.taskId)
        && Arrays.equals(history, that.history);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(origin);
    result = 31 * result + Objects.hashCode(user);
    result = 31 * result + Objects.hashCode(measurementId);
    result = 31 * result + Arrays.hashCode(datasetFiles);
    result = 31 * result + Objects.hashCode(taskId);
    result = 31 * result + Arrays.hashCode(history);
    return result;
  }
}
