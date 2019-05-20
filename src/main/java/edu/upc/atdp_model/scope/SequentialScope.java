package edu.upc.atdp_model.scope;

public class SequentialScope extends BranchingScope {
    public SequentialScope(String id) {
        super(id);
    }

    public ScopeType getType() {
        return ScopeType.Sequential;
    }
}
