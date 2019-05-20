package edu.upc.atdp_model.scope;

import edu.upc.atdp_model.Jsonizable;
import edu.upc.atdp_model.fragment.Activity;

public abstract class Scope implements Jsonizable {

    private String id;

    public Scope(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public abstract ScopeType getType();

    public abstract Scope findScope (String id);
    public abstract LeafScope findScopeWithActivity (Activity a);

}
