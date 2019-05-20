package edu.upc.atdp_model;

import edu.upc.atdp_model.constraint.TemporalConstraint;
import edu.upc.atdp_model.constraint.TemporalConstraintType;
import edu.upc.atdp_model.fragment.Activity;
import edu.upc.atdp_model.fragment.Fragment;
import edu.upc.atdp_model.fragment.FragmentType;
import edu.upc.atdp_model.relation.FragmentRelation;
import edu.upc.atdp_model.relation.FragmentRelationType;
import edu.upc.atdp_model.scope.BranchingScope;
import edu.upc.atdp_model.scope.LeafScope;
import edu.upc.atdp_model.scope.Scope;
import edu.upc.atdp_model.scope.ScopeType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Fragment getFragmentById(String id) {
        return fragmentsById.get(id);
    }

    public Scope getScopeById(String id) { return this.scopesById.get(id); }

    public TemporalConstraint getTemporalConstraintById(String id) { return this.temporalConstraintsById.get(id); }

    public FragmentRelation getFragmentRelationById(String id) { return this.fragmentRelationsById.get(id); }

    public Stream<FragmentRelation> getOutgoingRelations (Fragment fragment) {
        return fragmentRelationsById.values().stream().filter(rel -> rel.getSource().equals(fragment));
    }

    public Stream<FragmentRelation> getIncomingRelations(Fragment fragment) {
        return fragmentRelationsById.values().stream().filter(rel -> rel.getDestination().equals(fragment));
    }

    public List<TemporalConstraint> getTemporalConstraints() {
        return new ArrayList<>(temporalConstraintsById.values());
    }

    public List<Fragment> getFragments() {
        return new ArrayList<>(fragmentsById.values());
    }

    public List<Fragment> getFragmentsWithType(FragmentType ft) {
        return fragmentsByType.get(ft);
    }

    public List<TemporalConstraint> getTemporalConstraintsWithType(TemporalConstraintType tt) {
        return temporalConstraintsByType.get(tt);
    }

    public List<FragmentRelation> getFragmentRelationsWithType(FragmentRelationType frt) {
        return fragmentRelationsByType.get(frt);
    }

    public List<Scope> getScopesWithType(ScopeType st) {
        return this.scopesByType.get(st);
    }

    public Scope getScopeTreeRoot() {
        return this.scopeTreeRoot;
    }


    /** Adds activity {f} to the current interpretation. Activity {f} will belong to
     * leaf scope represented by {scopeId}. If {f} already existed inside this interpretation
     * it will be moved.
     * **/
    public void addOrMoveActivity(Activity f, String scopeId) {

        LeafScope leafScope = (LeafScope)scopeTreeRoot.findScope(scopeId);

        if (fragmentsById.get(f.getId()) == null) { // Fragment already in I
            fragmentsById.put(f.getId(), f);
            fragmentsByType.get(f.getType()).add(f);
        } else {
            LeafScope originalScope = (LeafScope)scopeTreeRoot.findScopeWithActivity(f);
            originalScope.removeActivity(f);
        }

        leafScope.addActivity(f);
    }

    /** Adds non-activity fragment {f} to the current interpretation. Use {addOrMoveActivity}
     * to add fragment activities instead.
     * **/
    public void addFragment(Fragment f) {
        assert (!f.getType().equals(FragmentType.Activity));
        fragmentsById.put(f.getId(), f);
        fragmentsByType.get(f.getType()).add(f);
    }

    public void addTemporalConstraint (TemporalConstraint f) {
        temporalConstraintsById.put(f.getId(), f);
        temporalConstraintsByType.get(f.getType()).add(f);
    }

    public void addFragmentRelation(FragmentRelation f) {
        fragmentRelationsById.put(f.getId(), f);
        fragmentRelationsByType.get(f.getType()).add(f);
    }

    public void removeActivity (Activity a) {
        LeafScope leafScope = scopeTreeRoot.findScopeWithActivity(a);
        leafScope.removeActivity(a);
        fragmentsById.remove(a.getId());
        fragmentsByType.get(FragmentType.Activity).remove(a);
    }

    public void removeFragment (Fragment f) {
        assert (!f.getType().equals(FragmentType.Activity));
        fragmentsById.remove(f.getId());
        fragmentsByType.get(f.getType()).remove(f);
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
