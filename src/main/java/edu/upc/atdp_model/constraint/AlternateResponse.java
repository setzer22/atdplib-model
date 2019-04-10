package edu.upc.atdp_model.constraint;

import edu.upc.atdp_model.fragment.Activity;

import java.util.List;

public class AlternateResponse extends TemporalConstraint {
    public AlternateResponse(String id, List<Activity> activities) {
        super(id, activities);
    }

    @Override
    public boolean checkSignature() {
        return getActivities().size() >= 2;
    }

    @Override
    public TemporalConstraintType getType() {
        return TemporalConstraintType.AlternateResponse;
    }
}
