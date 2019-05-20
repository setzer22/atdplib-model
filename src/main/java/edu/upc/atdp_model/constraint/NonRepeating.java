package edu.upc.atdp_model.constraint;

        import edu.upc.atdp_model.fragment.Activity;

        import java.util.List;

public class NonRepeating extends TemporalConstraint {

    public NonRepeating(String id, List<Activity> activities) {
        super(id, activities);
    }

    @Override
    public boolean checkSignature() {
        return getActivities().size() == 1;
    }

    @Override
    public TemporalConstraintType getType() {
        return TemporalConstraintType.NonRepeating;
    }
}
