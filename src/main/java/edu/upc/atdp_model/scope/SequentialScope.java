package edu.upc.atdp_model.scope;

import edu.upc.atdp_model.scope.BranchingScope;
import edu.upc.atdp_model.scope.Scope;
import edu.upc.atdp_model.scope.ScopeType;

public class SequentialScope extends BranchingScope {
    public SequentialScope(String id, Scope parent) {
        super(id, parent);
    }

    public ScopeType getType() {
        return ScopeType.Sequential;
    }
}
