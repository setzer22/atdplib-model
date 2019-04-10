package edu.upc.atdp_model.relation;

import edu.upc.atdp_model.Jsonizable;
import edu.upc.atdp_model.fragment.Fragment;
import org.json.simple.JSONObject;

import java.util.Objects;

public abstract class FragmentRelation implements Jsonizable {

    private String id;
    private Fragment source;
    private Fragment destination;

    public FragmentRelation(String id, Fragment source, Fragment destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public Fragment getSource() {
        return source;
    }

    public Fragment getDestination() {
        return destination;
    }

    public abstract boolean checkSignature();

    public abstract FragmentRelationType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FragmentRelation that = (FragmentRelation) o;
        return id.equals(that.id) &&
                source.equals(that.source) &&
                destination.equals(that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, destination);
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put("type", this.getType().toString());
        obj.put("id", this.getId());
        obj.put("source", this.getSource().getId());
        obj.put("destination", this.getDestination().getId());

        return obj;
    }

    public static FragmentRelation makeFragmentRelation (FragmentRelationType type, String id, Fragment source, Fragment destination) {
        switch (type) {
            case Agent: return new Agent(id, source, destination);
            case Patient: return new Patient(id, source, destination);
            case Coreference: return new Coreference(id, source, destination);
            default: throw new IllegalArgumentException("Cannot create fragment relation of type %s".format(type.toString()));
        }

    }
}
