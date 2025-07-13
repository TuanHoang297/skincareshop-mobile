package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;
import java.util.List;

public class Routine implements Serializable {
    private String name;
    private String description;
    private String skinType;
    private List<Step> steps;
    private String timeOfDay;

    public String getTimeOfDay() { return timeOfDay; }
    public List<Step> getSteps() { return steps; }

    public static class Step {
        private int stepNumber;
        private String stepName;
        private String description;
        private String duration;

        public int getStepNumber() { return stepNumber; }
        public String getStepName() { return stepName; }
        public String getDescription() { return description; }
        public String getDuration() { return duration; }
    }
}
