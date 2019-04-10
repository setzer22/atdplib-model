package edu.upc.atdp_model.fragment;

import edu.upc.atdp_model.Jsonizable;
import org.json.simple.JSONObject;

import java.util.Objects;

public abstract class Fragment implements Jsonizable {
    private String id;
    private String text;
    private int start;
    private int end;

    public Fragment (String id, String text, int start, int end) {
        this.id = id;
        this.text = text;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }

    public abstract FragmentType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fragment fragment = (Fragment) o;
        return start == fragment.start &&
                end == fragment.end &&
                id.equals(fragment.id) &&
                text.equals(fragment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, start, end);
    }

    @Override
    public JSONObject toJson () {
        JSONObject obj = new JSONObject();
        obj.put("type", this.getType().toString());
        obj.put("text", this.getText());
        obj.put("id", this.getId());
        obj.put("start", this.getStart());
        obj.put("end", this.getEnd());
        return obj;
    }

    public static Fragment makeFragment (FragmentType type, String id, String text, int start, int end) {
        switch (type) {
            case Role: return new Role(id, text, start, end);
            case Activity: return new Activity(id, text, start, end);
            case BusinessObject: return new BusinessObject(id, text, start, end);
            default: throw new IllegalArgumentException("Cannot create fragment of type %s".format(type.toString()));
        }
    }
}
