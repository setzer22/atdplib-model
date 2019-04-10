package edu.upc.atdp_model.fragment;

public class BusinessObject extends Fragment {

    public BusinessObject(String id, String text, int start, int end) {
        super(id, text, start, end);
    }

    public FragmentType getType() {
        return FragmentType.BusinessObject;
    }

}
