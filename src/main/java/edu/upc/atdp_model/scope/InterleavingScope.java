package edu.upc.atdp_model.scope;

public class InterleavingScope extends BranchingScope {

    public InterleavingScope(String id) {
        super(id);
    }

    public ScopeType getType() {
        return ScopeType.Interleaving;
    }

}
