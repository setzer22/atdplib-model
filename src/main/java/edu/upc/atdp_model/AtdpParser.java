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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AtdpParser {


    public static Fragment parseFragment (JSONObject fragment) {
        return Fragment.makeFragment(
                FragmentType.valueOf(fragment.get("type").toString()),
                (String)fragment.get("id"),
                (String)fragment.get("text"),
                Integer.parseInt(fragment.get("start").toString()),
                Integer.parseInt(fragment.get("end").toString())
        );
    }

    public static TemporalConstraint parseTemporalConstraint (JSONObject tc, Map<String, Fragment> fragmentsById) {
        List<Activity> activities = Arrays.stream(((JSONArray) tc.get("activities")).toArray())
                .map(aId -> (Activity)fragmentsById.get(aId)).collect(Collectors.toList());
        return TemporalConstraint.makeTemporalConstraint(
                TemporalConstraintType.valueOf(tc.get("type").toString()),
                (String)tc.get("id"),
                activities
        );
    }

    public static FragmentRelation parseFragmentRelation (JSONObject fr, Map<String,Fragment> fragmentsById) {
        Fragment source = fragmentsById.get(fr.get("source").toString());
        Fragment destination = fragmentsById.get(fr.get("destination").toString());

        return FragmentRelation.makeFragmentRelation(
                FragmentRelationType.valueOf(fr.get("type").toString()),
                fr.get("id").toString(),
                source,
                destination
        );
    }

    public static Scope parseScopeTree(JSONObject scopeTreeNode, Map<String, Fragment> fragmentsById) {
        boolean leaf = scopeTreeNode.get("children") == null;
        String id = scopeTreeNode.get("id").toString();

        if (leaf) {
            List<Activity> activities = Arrays.stream(((JSONArray) scopeTreeNode.get("activities")).toArray())
                    .map(aId -> (Activity)fragmentsById.get(aId)).collect(Collectors.toList());
            return new LeafScope(id, null, activities);
        } else {
            JSONArray childrenArray = (JSONArray)scopeTreeNode.get("children");
            List<Scope> children = Arrays.stream(childrenArray.toArray())
                    .map(c -> parseScopeTree((JSONObject) c, fragmentsById)).collect(Collectors.toList());
            BranchingScope s = BranchingScope.makeBranchingScope(
                    ScopeType.valueOf(scopeTreeNode.get("type").toString()),
                    id,
                    null
            );
            for (Scope c : children) {
                s.addChild(c);
            }
            return s;
        }
    }

    public static Atdp parseFromJson(JSONObject json) {

        List<Interpretation> interpretations = new ArrayList<>();

        for (Object interp : (JSONArray)json.get("interpretations")) {
            JSONObject interpretation = (JSONObject)interp;

            // Fragments
            Map<String, Fragment> fragmentsById = new HashMap<>();
            for (Object frag : (JSONArray)interpretation.get("fragments")) {
                Fragment f = parseFragment((JSONObject) frag);
                fragmentsById.put(f.getId(), f);
            }

            // Temporal Constraints
            List<TemporalConstraint> temporalConstraintList = new ArrayList<>();
            for (Object tc : (JSONArray)interpretation.get("temporal_constraints")) {
                TemporalConstraint temporalConstraint = parseTemporalConstraint((JSONObject)tc, fragmentsById);
                temporalConstraintList.add(temporalConstraint);
            }

            List<FragmentRelation> fragmentRelationsList = new ArrayList<>();
            for (Object fr : (JSONArray)interpretation.get("fragment_relations")) {
                FragmentRelation fragmentRelation = parseFragmentRelation((JSONObject)fr, fragmentsById);
                fragmentRelationsList.add(fragmentRelation);
            }

            JSONObject scopeTree = (JSONObject)interpretation.get("scope_tree");
            BranchingScope scopeTreeRoot = (BranchingScope)parseScopeTree(scopeTree, fragmentsById);

            interpretations.add(
                    new Interpretation(new ArrayList<>(
                            fragmentsById.values()),
                            fragmentRelationsList,
                            temporalConstraintList,
                            scopeTreeRoot
                    )
            );
        }

        Atdp atdp = new Atdp(interpretations);

        return atdp;
    }

    public static Atdp parseFromJsonString(String jsonString) throws ParseException {
        return parseFromJson((JSONObject)new JSONParser().parse(jsonString));
    }

}
