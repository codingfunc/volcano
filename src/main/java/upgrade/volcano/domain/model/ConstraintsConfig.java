package upgrade.volcano.domain.model;

public class ConstraintsConfig {

    private final Integer maxDuration;

    private final Integer minDaysInAdvance;

    private final Integer maxDaysInAdvance;

    public ConstraintsConfig(final Integer maxDuration, final Integer minDaysInAdvance, final Integer maxDaysInAdvance){
        this.maxDuration = maxDuration;
        this.minDaysInAdvance = minDaysInAdvance;
        this.maxDaysInAdvance = maxDaysInAdvance;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public Integer getMinDaysInAdvance() {
        return minDaysInAdvance;
    }

    public Integer getMaxDaysInAdvance() {
        return maxDaysInAdvance;
    }
}
