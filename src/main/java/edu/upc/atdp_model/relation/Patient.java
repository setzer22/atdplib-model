package edu.upc.atdp_model.relation;

import edu.upc.atdp_model.fragment.FragmentType;
import edu.upc.atdp_model.fragment.Fragment;

public class Patient extends FragmentRelation {
    public Patient(String id, Fragment source, Fragment destination) {
        super(id, source, destination);
    }

    public boolean checkSignature() {
        return getSource().getType().equals(FragmentType.Activity) &&
                (getDestination().getType().equals(FragmentType.BusinessObject) ||
                        getDestination().getType().equals(FragmentType.Role));
    }

    public FragmentRelationType getType() {
        return FragmentRelationType.Patient;
    }
}
