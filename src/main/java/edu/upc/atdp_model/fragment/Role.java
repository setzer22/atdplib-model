package edu.upc.atdp_model.fragment;

public class Role extends Fragment {

    public Role(String id, String text, int start, int end) {
        super(id, text, start, end);
    }

    public FragmentType getType() {
        return FragmentType.Role;
    }

}
