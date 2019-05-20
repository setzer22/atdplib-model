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

public abstract class BranchingScope extends Scope implements Iterable<Scope>, Jsonizable {

    private Scope parent;
    private List<Scope> children;

    public BranchingScope(String id) {
        super(id);
        this.children = new ArrayList<Scope>();
    }

    /** Returns the list of child scopes from this scope.
     * NOTE: The list is not modifiable
     * **/
    public List<Scope> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public Scope getParent() {
        return parent;
    }

    public boolean isRoot () {
        return parent == null;
    }

    public void addChild(Scope child) {
        children.add(child);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public Iterator<Scope> iterator() {
        return Collections.unmodifiableList(children).iterator();
    }

    public List<Scope> getAllChildren() {
        List<Scope> allChildren = new ArrayList<Scope>();
        for (Scope s : children) {
            if (s.getType().equals(ScopeType.Leaf)) {
                allChildren.add(s);
            }
            else {
                allChildren.addAll(((BranchingScope)s).getAllChildren());
            }
        }
        return allChildren;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put("type", this.getType().toString());
        obj.put("id", this.getId());

        JSONArray childrenArray =  new JSONArray();
        childrenArray.addAll(children.stream().map((x) -> x.toJson()).collect(Collectors.toList()));
        obj.put("children", childrenArray);

        return obj;
    }

    public static BranchingScope makeBranchingScope(ScopeType type, String id) {

        switch(type) {
            case Inclusive:
            case Iterating: return new InclusiveScope(id);
            case Sequential: return new SequentialScope(id);
            case Conflicting: return new ConflictingScope(id);
            case Interleaving: return new InterleavingScope(id);
            default: throw new IllegalArgumentException("Cannot create scope of type %s".format(type.toString()));
        }

    }

    @Override
    public Scope findScope (String id) {
        if (getId().equals(id)) {
            return this;
        } else {
            for (Scope c : getChildren()) {
                Scope found = c.findScope(id);
                if (found != null) return found;
            }
        }
        return null;
    }

    @Override
    public LeafScope findScopeWithActivity(Activity a) {
        for (Scope c : getChildren()) {
            LeafScope found = c.findScopeWithActivity(a);
            if (found != null) return found;
        }
        return null;
    }

}
