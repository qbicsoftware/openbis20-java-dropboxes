package life.qbic.registration.openbis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import life.qbic.registration.openbis.exceptions.fail.ProvenanceParseException;

/**
 * Parses a provenance file and produces a java object from it.
 */
public class ProvenanceParser {


  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  DataSetProvenance parseProvenanceJson(File provenanceFile) {
    try {
      return OBJECT_MAPPER.readValue(provenanceFile,
          DataSetProvenance.class);
    } catch (IOException e) {
      throw new ProvenanceParseException(
          "Could not parse '" + provenanceFile.getAbsolutePath() + "'", e);
    }
  }

}
