package edu.upc.atdp_model;

import edu.upc.atdp_model.constraint.TemporalConstraint;
import edu.upc.atdp_model.constraint.TemporalConstraintType;
import edu.upc.atdp_model.fragment.Fragment;
import edu.upc.atdp_model.fragment.FragmentType;
import edu.upc.atdp_model.relation.FragmentRelation;
import edu.upc.atdp_model.relation.FragmentRelationType;
import edu.upc.atdp_model.scope.BranchingScope;
import edu.upc.atdp_model.scope.Scope;
import edu.upc.atdp_model.scope.ScopeType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class Interpretation implements Jsonizable {

    private Map<TemporalConstraintType, List<TemporalConstraint>> temporalConstraintsByType;
    private Map<FragmentRelationType, List<FragmentRelation>> fragmentRelationsByType;
    private Map<ScopeType, List<Scope>> scopesByType;
    private Map<FragmentType, List<Fragment>> fragmentsByType;

    private Map<String, Fragment> fragmentsById;
    private Map<String, Scope> scopesById;
    private Map<String, TemporalConstraint> temporalConstraintsById;
    private Map<String, FragmentRelation> fragmentRelationsById;

    private BranchingScope scopeTreeRoot;

    public Interpretation(List<Fragment> fragments, List<FragmentRelation> fragmentRelations,
                          List<TemporalConstraint> temporalConstraints, BranchingScope scopeTreeRoot) {

        temporalConstraintsByType = new HashMap<>();
        fragmentRelationsByType = new HashMap<>();
        scopesByType = new HashMap<>();
        fragmentsByType = new HashMap<>();

        fragmentsById = new HashMap<>();
        scopesById = new HashMap<>();
        temporalConstraintsById = new HashMap<>();
        fragmentRelationsById = new HashMap<>();

        // Fragments
        for (FragmentType t : FragmentType.values()) {
            fragmentsByType.put(t, new ArrayList<Fragment>());
        }
        for (Fragment f : fragments) {
            fragmentsById.put(f.getId(), f);
            fragmentsByType.get(f.getType()).add(f);
        }

        // Fragment Relations
        for (FragmentRelationType t : FragmentRelationType.values()) {
            fragmentRelationsByType.put(t, new ArrayList<FragmentRelation>());
        }
        for (FragmentRelation r : fragmentRelations) {
            fragmentRelationsById.put(r.getId(), r);
            fragmentRelationsByType.get(r.getType()).add(r);
        }

        // Scopes
        for (ScopeType t : ScopeType.values()) {
            scopesByType.put(t, new ArrayList<Scope>());
        }
        for (Scope s : scopeTreeRoot.getAllChildren()) {
            scopesById.put(s.getId(), s);
            scopesByType.get(s.getType()).add(s);
        }
        this.scopeTreeRoot = scopeTreeRoot;

        // Temporal Constraints
        for (TemporalConstraintType t : TemporalConstraintType.values()) {
            temporalConstraintsByType.put(t, new ArrayList<TemporalConstraint>());
        }
        for (TemporalConstraint t : temporalConstraints) {
            temporalConstraintsById.put(t.getId(), t);
            temporalConstraintsByType.get(t.getType()).add(t);
        }


    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        JSONArray fragmentArray = new JSONArray();
        JSONArray temporalConstraintsArray = new JSONArray();
        JSONArray fragmentRelationsArray = new JSONArray();

        fragmentArray.addAll(fragmentsById.values().stream().map((x) -> x.toJson()).collect(Collectors.toList()));
        temporalConstraintsArray.addAll(temporalConstraintsById.values().stream().map((x) -> x.toJson()).collect(Collectors.toList()));
        fragmentRelationsArray.addAll(fragmentRelationsById.values().stream().map((x) -> x.toJson()).collect(Collectors.toList()));

        obj.put("fragments", fragmentArray);
        obj.put("temporal_constraints", temporalConstraintsArray);
        obj.put("fragment_relations", fragmentRelationsArray);
        obj.put("scope_tree", scopeTreeRoot.toJson());

        return obj;

    }
}
