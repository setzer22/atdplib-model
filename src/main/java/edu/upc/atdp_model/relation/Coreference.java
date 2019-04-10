package edu.upc.atdp_model.relation;

import edu.upc.atdp_model.fragment.FragmentType;
import edu.upc.atdp_model.fragment.Fragment;

public class Coreference extends FragmentRelation {
    public Coreference(String id, Fragment source, Fragment destination) {
        super(id, source, destination);
    }

    public boolean checkSignature() {
        return (getSource().getType().equals(FragmentType.Role) &&
                getDestination().getType().equals(FragmentType.Role)) ||
                (getDestination().getType().equals(FragmentType.BusinessObject) &&
                        getDestination().getType().equals(FragmentType.BusinessObject)) ;
    }

    public FragmentRelationType getType() {
        return FragmentRelationType.Coreference;
    }
}
