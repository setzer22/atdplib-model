package edu.upc.atdp_model.scope;

public class InterleavingScope extends BranchingScope {

    public InterleavingScope(String id, Scope parent) {
        super(id, parent);
    }

    public ScopeType getType() {
        return ScopeType.Interleaving;
    }

}
