package edu.upc.atdp_model.scope;

public class ConflictingScope extends BranchingScope {
    public ConflictingScope(String id, Scope parent) {
        super(id, parent);
    }

    public ScopeType getType() {
        return ScopeType.Conflicting;
    }
}
