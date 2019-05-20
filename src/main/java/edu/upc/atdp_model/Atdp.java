package edu.upc.atdp_model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class Atdp implements Jsonizable {

    List<Interpretation> interpretations;

    public Atdp(List<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    public List<Interpretation> getInterpretations() {
        return interpretations;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        JSONArray interps = new JSONArray();
        interps.addAll(interpretations.stream().map((x) -> x.toJson()).collect(Collectors.toList()));
        obj.put("interpretations", interps);

        return obj;
    }
}
