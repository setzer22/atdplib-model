package edu.upc.atdp_model.scope;

import edu.upc.atdp_model.Jsonizable;

public abstract class Scope implements Jsonizable {

    private String id;

    public Scope(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public abstract ScopeType getType();

}
