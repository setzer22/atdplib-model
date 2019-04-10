package edu.upc.atdp_model.constraint;

import edu.upc.atdp_model.fragment.Activity;

import java.util.List;

public class Precedence extends TemporalConstraint {
    public Precedence(String id, List<Activity> activities) {
        super(id, activities);
    }

    @Override
    public boolean checkSignature() {
        return getActivities().size() >= 2;
    }

    @Override
    public TemporalConstraintType getType() {
        return TemporalConstraintType.Precedence;
    }
}
