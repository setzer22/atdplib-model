package edu.upc.atdp_model.scope;

public class InclusiveScope extends BranchingScope {

    public InclusiveScope(String id) {
        super(id);
    }

    public ScopeType getType() {
        return ScopeType.Inclusive;
    }

}
