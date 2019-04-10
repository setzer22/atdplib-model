package edu.upc.atdp_model.constraint;

import edu.upc.atdp_model.Jsonizable;
import edu.upc.atdp_model.fragment.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TemporalConstraint implements Iterable<Activity>, Jsonizable {

    private String id;
    private List<Activity> activities;

    public TemporalConstraint(String id, List<Activity> activities) {
        this.id = id;
        this.activities = activities;
    }

    public String getId () {
        return this.id;
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public Iterator<Activity> iterator() {
        return Collections.unmodifiableList(activities).iterator();
    }

    public abstract boolean checkSignature();

    public abstract TemporalConstraintType getType();

    @Override
    public JSONObject toJson() {

        JSONObject obj = new JSONObject();
        obj.put("id", this.getId());
        obj.put("type", this.getType().toString());
        JSONArray activitiesArray = new JSONArray();
        activitiesArray.addAll(activities.stream().map((x) -> x.getId()).collect(Collectors.toList()));
        obj.put("activities", activitiesArray);

        return obj;

    }

    public static TemporalConstraint makeTemporalConstraint(TemporalConstraintType type, String id, List<Activity> activities) {
        switch (type) {
            case Terminating: return new Terminating(id, activities);
            case Response: return new Response(id, activities);
            case Precedence: return new Precedence(id, activities);
            case Mandatory: return new Mandatory(id, activities);
            case NonCoOccurrence: return new NonCoOccurrence(id, activities);
            case AlternateResponse: return new AlternateResponse(id, activities);
            default: throw new IllegalArgumentException("Cannot create temporal constraint of type %s".format(type.toString()));
        }
    }


}
