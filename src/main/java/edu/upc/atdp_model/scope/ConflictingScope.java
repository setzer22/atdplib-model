package edu.upc.atdp_model.scope;

public class ConflictingScope extends BranchingScope {
    public ConflictingScope(String id) {
        super(id);
    }

    public ScopeType getType() {
        return ScopeType.Conflicting;
    }
}
