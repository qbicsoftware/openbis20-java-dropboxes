package life.qbic.registration

import groovy.json.JsonSlurper

class Provenance {

    final String origin
    final String user
    final String measurementID
    final List<String> history

    Provenance(String origin, String user, String measurementID, List<String> history) {
        this.origin = origin
        this.user = user
        this.measurementID = measurementID
        this.history = history
    }

    static Provenance fromJson(String jsonString) {
        def jsonSlurper = new JsonSlurper();
        def object = jsonSlurper.parseText(jsonString)
        assert object instanceof Map
        assert object.history instanceof List
        return new Provenance(object.origin as String, object.user as String,
                object.measurementId as String, object.history as List<String>)
    }
}
