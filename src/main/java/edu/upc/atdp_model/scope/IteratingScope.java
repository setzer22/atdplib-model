package edu.upc.atdp_model.scope;

public class IteratingScope extends BranchingScope {

    public IteratingScope(String id, Scope parent) {
        super(id, parent);
    }

    public ScopeType getType() {
        return ScopeType.Iterating;
    }

}
