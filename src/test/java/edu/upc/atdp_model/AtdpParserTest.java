package edu.upc.atdp_model;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtdpParserTest {

    @Test
    public void parseFromJsonStringTest() throws ParseException {
        String jsonString = "{\"interpretations\":[{\"temporal_constraints\":[{\"activities\":[\"A2\",\"A4\"],\"id\":\"T4\",\"type\":\"Response\"},{\"activities\":[\"A1\",\"A3\"],\"id\":\"T1\",\"type\":\"Precedence\"},{\"activities\":[\"A1\",\"A3\"],\"id\":\"T2\",\"type\":\"Response\"},{\"activities\":[\"A2\",\"A4\"],\"id\":\"T3\",\"type\":\"Precedence\"}],\"scope_tree\":{\"children\":[{\"activities\":[\"A0\"],\"id\":\"S1\",\"type\":\"Leaf\"},{\"children\":[{\"activities\":[\"A1\",\"A3\"],\"id\":\"S21\",\"type\":\"Leaf\"},{\"activities\":[\"A2\",\"A4\"],\"id\":\"S22\",\"type\":\"Leaf\"}],\"id\":\"S2\",\"type\":\"Conflicting\"}],\"id\":\"S0\",\"type\":\"Sequential\"},\"fragments\":[{\"start\":10,\"end\":20,\"text\":\"individual\",\"id\":\"A1\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"family\",\"id\":\"A2\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"prepare ind\",\"id\":\"A3\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"prepare fam\",\"id\":\"A4\",\"type\":\"Activity\"},{\"start\":10,\"end\":20,\"text\":\"Customer\",\"id\":\"R0\",\"type\":\"Role\"},{\"start\":10,\"end\":20,\"text\":\"decide\",\"id\":\"A0\",\"type\":\"Activity\"}],\"fragment_relations\":[{\"destination\":\"R0\",\"id\":\"G1\",\"source\":\"A4\",\"type\":\"Agent\"}]}]}";
        Atdp atdp = AtdpParser.parseFromJsonString(jsonString);

        assertEquals(jsonString, atdp.toJson().toJSONString());
    }
}