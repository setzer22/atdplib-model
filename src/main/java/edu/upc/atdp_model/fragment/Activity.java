package edu.upc.atdp_model.fragment;

public class Activity extends Fragment {

    public Activity(String id, String text, int start, int end) {
        super(id, text, start, end);
    }

    public FragmentType getType() {
        return FragmentType.Activity;
    }

}
