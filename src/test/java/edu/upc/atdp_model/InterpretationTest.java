package edu.upc.atdp_model;

import edu.upc.atdp_model.constraint.*;
import edu.upc.atdp_model.fragment.Activity;
import edu.upc.atdp_model.fragment.Role;
import edu.upc.atdp_model.relation.Agent;
import edu.upc.atdp_model.scope.*;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.ArrayList;
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

        SequentialScope s0 = new SequentialScope("S0");

        LeafScope s1 = new LeafScope("S1", Arrays.asList(a));

        BranchingScope s2 = new ConflictingScope("S2");
        s2.addChild(new LeafScope("S21", Arrays.asList(b, d)));
        s2.addChild(new LeafScope("S22", Arrays.asList(c, e)));

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

    @Test
    public void testAddition() {

        BranchingScope root = new SequentialScope("S_root");
        LeafScope leaf = new LeafScope("S_leaf", new ArrayList<>());
        root.addChild(leaf);
        Interpretation interp = new Interpretation(Arrays.asList(), Arrays.asList(), Arrays.asList(), root);

        interp.addOrMoveActivity(new Activity("A0", "decide", 10, 10), "S_leaf");
        interp.addOrMoveActivity(new Activity("A1", "send", 10, 10), "S_leaf");
        interp.addFragment(new Role("R0", "customer", 10, 10));
        interp.addFragmentRelation(new Agent("G0", interp.getFragmentById("A0"), interp.getFragmentById("R0")));
        interp.addTemporalConstraint(new Precedence("Prec_0",
                Arrays.asList(
                        (Activity)interp.getFragmentById("A0"),
                        (Activity)interp.getFragmentById("A1"))));


        System.out.println(interp.toJson().toString());

    }

    @Test
    public void hospitalModel () {
        BranchingScope root = new SequentialScope("S_root");

        LeafScope examination = new LeafScope("S_exam");

        BranchingScope iter = new IteratingScope("S_iter");
        LeafScope sampling = new LeafScope("S_sampl");
        iter.addChild(sampling);

        LeafScope diagnostic = new LeafScope("S_diag");

        root.addChild(examination);
        root.addChild(sampling);
        root.addChild(diagnostic);

        Interpretation interpretation = new Interpretation(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), root);

        interpretation.addOrMoveActivity(new Activity("A_exam", "exam", 0,0), "S_exam");
        interpretation.addOrMoveActivity(new Activity("A_go", "go", 0,0), "S_exam");
        interpretation.addOrMoveActivity(new Activity("A_pre_diag", "pre diag", 0,0), "S_exam");
        interpretation.addOrMoveActivity(new Activity("A_cont", "cont", 0,0), "S_exam");

        interpretation.addOrMoveActivity(new Activity("A_prepare", "prepare", 0,0), "S_sampl");
        interpretation.addOrMoveActivity(new Activity("A_take", "take", 0,0), "S_sampl");
        interpretation.addOrMoveActivity(new Activity("A_ok", "ok", 0,0), "S_sampl");
        interpretation.addOrMoveActivity(new Activity("A_nok", "nok", 0,0), "S_sampl");

        interpretation.addOrMoveActivity(new Activity("A_diag", "diag", 0,0), "S_diag");
        interpretation.addOrMoveActivity(new Activity("A_therap", "therap", 0,0), "S_diag");


        interpretation.addTemporalConstraint(new Precedence("TR0", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_exam"),
                (Activity)interpretation.getFragmentById("A_pre_diag") )));

        interpretation.addTemporalConstraint(new Response("TR1", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_exam"),
                (Activity)interpretation.getFragmentById("A_pre_diag") )));

        interpretation.addTemporalConstraint(new Precedence("TR2", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_pre_diag"),
                (Activity)interpretation.getFragmentById("A_go") )));

        interpretation.addTemporalConstraint(new Precedence("TR3", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_pre_diag"),
                (Activity)interpretation.getFragmentById("A_cont") )));

        interpretation.addTemporalConstraint(new Response("TR4", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_pre_diag"),
                (Activity)interpretation.getFragmentById("A_go"),
                (Activity)interpretation.getFragmentById("A_cont") )));

        interpretation.addTemporalConstraint(new Precedence("TR5", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_prepare"),
                (Activity)interpretation.getFragmentById("A_take"))));

        interpretation.addTemporalConstraint(new Response("TR6", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_prepare"),
                (Activity)interpretation.getFragmentById("A_take"))));

        interpretation.addTemporalConstraint(new Precedence("TR7", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_take"),
                (Activity)interpretation.getFragmentById("A_ok"))));

        interpretation.addTemporalConstraint(new Precedence("TR8", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_take"),
                (Activity)interpretation.getFragmentById("A_nok"))));

        interpretation.addTemporalConstraint(new Response("TR9", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_take"),
                (Activity)interpretation.getFragmentById("A_ok"),
                (Activity)interpretation.getFragmentById("A_nok") )));

        interpretation.addTemporalConstraint(new Terminating("TR10", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_ok"))));

        interpretation.addTemporalConstraint(new AlternateResponse("TR11", Arrays.asList(
                (Activity)interpretation.getFragmentById("A_take"),
                (Activity)interpretation.getFragmentById("A_nok"))));

        System.out.println(interpretation.toJson().toString());


    }
}