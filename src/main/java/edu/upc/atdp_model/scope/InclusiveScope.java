package edu.upc.atdp_model.scope;

public class InclusiveScope extends BranchingScope {

    public InclusiveScope(String id, Scope parent) {
        super(id, parent);
    }

    public ScopeType getType() {
        return ScopeType.Inclusive;
    }

}
