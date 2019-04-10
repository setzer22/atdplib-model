package edu.upc.atdp_model;

import edu.upc.atdp_model.constraint.Precedence;
import edu.upc.atdp_model.constraint.Response;
import edu.upc.atdp_model.constraint.TemporalConstraint;
import edu.upc.atdp_model.fragment.Activity;
import edu.upc.atdp_model.fragment.Role;
import edu.upc.atdp_model.relation.Agent;
import edu.upc.atdp_model.relation.FragmentRelation;
import edu.upc.atdp_model.scope.BranchingScope;
import edu.upc.atdp_model.scope.ConflictingScope;
import edu.upc.atdp_model.scope.LeafScope;
import edu.upc.atdp_model.scope.SequentialScope;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class InterpretationTest {
    @Test
    public void testToJson() throws ParseException {
        Activity a = new Activity("A0", "decide", 10, 20);
        Activity b = new Activity("A1", "individual", 10, 20);
        Activity c = new Activity("A2", "family", 10, 20);
        Activity d = new Activity("A3", "prepare ind", 10, 20);
        Activity e = new Activity("A4", "prepare fam", 10, 20);

        SequentialScope s0 = new SequentialScope("S0", null);

        LeafScope s1 = new LeafScope("S1", s0, Arrays.asList(a));

        BranchingScope s2 = new ConflictingScope("S2", s0);
        s2.addChild(new LeafScope("S21", s2, Arrays.asList(b, d)));
        s2.addChild(new LeafScope("S22", s2, Arrays.asList(c, e)));

        s0.addChild(s1);
        s0.addChild(s2);

        TemporalConstraint t1 = new Precedence("T1", Arrays.asList(b, d));
        TemporalConstraint t2 = new Response("T2", Arrays.asList(b, d));
        TemporalConstraint t3 = new Precedence("T3", Arrays.asList(c, e));
        TemporalConstraint t4 = new Response("T4", Arrays.asList(c, e));

        Role r1 = new Role("R0", "Customer", 10, 20);

        Agent g1 = new Agent("G1", a, r1);
        Agent g2 = new Agent("G1", b, r1);
        Agent g3 = new Agent("G1", c, r1);
        Agent g4 = new Agent("G1", d, r1);
        Agent g5 = new Agent("G1", e, r1);

        Interpretation interp = new Interpretation(
                Arrays.asList(a, b, c, d, e, r1),
                Arrays.asList(g1, g2, g3, g4, g5),
                Arrays.asList(t1, t2, t3, t4),
                s0);

        System.out.println(interp.toJson().toString());

        String jsonTestString = "{\"temporal_constraints\":[{\"activities\":[\"A2\",\"A4\"],\"id\":\"T4\",\"type\":\"Response\"},{\"activities\":[\"A1\",\"A3\"],\"id\":\"T1\",\"type\":\"Precedence\"},{\"activities\":[\"A1\",\"A3\"],\"id\":\"T2\",\"type\":\"Response\"},{\"activities\":[\"A2\",\"A4\"],\"id\":\"T3\",\"type\":\"Precedence\"}],\"scope_tree\":{\"children\":[{\"activities\":[\"A0\"],\"id\":\"S1\",\"type\":\"Leaf\"},{\"children\":[{\"activities\":[\"A1\",\"A3\"],\"id\":\"S21\",\"type\":\"Leaf\"},{\"activities\":[\"A2\",\"A4\"],\"id\":\"S22\",\"type\":\"Leaf\"}],\"id\":\"S2\",\"type\":\"Conflicting\"}],\"id\":\"S0\",\"type\":\"Sequential\"},\"fragments\":[{\"start\":10,\"end\":20,\"text\":\"individual\",\"id\":\"A1\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"family\",\"id\":\"A2\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"prepare ind\",\"id\":\"A3\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"prepare fam\",\"id\":\"A4\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"Customer\",\"id\":\"R0\",\"type\":\"Role\"},{\"start\":10,\"end\":20,\"text\":\"decide\",\"id\":\"A0\",\"type\":\"Activity\"}],\"fragment_relations\":[{\"destination\":\"R0\",\"id\":\"G1\",\"source\":\"A4\",\"type\":\"Agent\"}]}";

        //assertEquals(testJson, interp.toJson().toJSONString());
        assertEquals(jsonTestString, interp.toJson().toJSONString());
    }
}