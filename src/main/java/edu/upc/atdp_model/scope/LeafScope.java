package edu.upc.atdp_model.scope;

import edu.upc.atdp_model.Jsonizable;
import edu.upc.atdp_model.fragment.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class LeafScope extends Scope implements Iterable<Activity>, Jsonizable {

    private List<Activity> activities;
    private BranchingScope parent;

    public LeafScope(String id, List<Activity> activities) {
        super(id);
        this.activities = activities;
    }

    public LeafScope(String id) {
        super(id);
        this.activities = new ArrayList<>();
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    @Override
    public ScopeType getType() {
        return ScopeType.Leaf;
    }

    @Override
    public Iterator<Activity> iterator() {
        return activities.iterator();
    }

    public void addActivity(Activity a) {
        activities.add(a);
    }

    public void removeActivity(Activity a) {
        activities.remove(a);
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put("id", this.getId());
        obj.put("type", this.getType().toString());
        JSONArray activitiesArray = new JSONArray();
        activitiesArray.addAll(this.activities.stream().map((x) -> x.getId()).collect(Collectors.toList()));
        obj.put("activities", activitiesArray);

        return obj;
    }

    @Override
    public LeafScope findScopeWithActivity(Activity a) {
        if (activities.contains(a)) return this;
        else return null;
    }

    @Override
    public Scope findScope(String id) {
        if (getId().equals(id)) return this;
        else return null;
    }
}
