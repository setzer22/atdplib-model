package edu.upc.atdp_model.constraint;

import edu.upc.atdp_model.fragment.Activity;

import java.util.List;

public class Response extends TemporalConstraint {
    public Response(String id, List<Activity> activities) {
        super(id, activities);
    }

    @Override
    public boolean checkSignature() {
        return getActivities().size() >= 2;
    }

    @Override
    public TemporalConstraintType getType() {
        return TemporalConstraintType.Response;
    }
}
