package life.qbic.registration

import groovy.json.JsonSlurper

/**
 * Example of a provenance file:
 * {
 "origin": "/Users/myuser/registration",
 "user": "/Users/myuser",
 "measurementId": "NGSQTEST001AE-1234512312",
 "datasetFiles" : [ "file1_1.fastq.gz", "file1_2.fastq.gz" ],
 "taskId": "74c5d26f-b756-42c3-b6f4-2b4825670a2d",
 "history": [
 "/opt/scanner-app/scanner-processing-dir/74c5d26f-b756-42c3-b6f4-2b4825670a2d"
 ]
 }
 */

class Provenance {

    final String origin
    final String user
    final String measurementID
    final List<String> datasetFiles
    final List<String> history

    Provenance(String origin, String user, String measurementID, List<String> datasetFiles,
               List<String> history) {
        this.origin = origin
        this.user = user
        this.measurementID = measurementID
        this.datasetFiles = datasetFiles
        this.history = history
    }

    static Provenance fromJson(String jsonString) {
        def jsonSlurper = new JsonSlurper();
        def object = jsonSlurper.parseText(jsonString)
        assert object instanceof Map
        assert object.history instanceof List
        assert object.datasetFiles instanceof List
        return new Provenance(object.origin as String, object.user as String,
                object.measurementId as String, object.datasetFiles as List<String>,
                object.history as List<String>)
    }
}
