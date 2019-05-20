package edu.upc.atdp_model.scope;

public class IteratingScope extends BranchingScope {

    public IteratingScope(String id) {
        super(id);
    }

    public ScopeType getType() {
        return ScopeType.Iterating;
    }

}
