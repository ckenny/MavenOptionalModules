package com.study.feature;

import org.springframework.stereotype.Component;

@Component
public class FeatureTwoImplementation implements Feature {

    private String featureName;

    public FeatureTwoImplementation() {
        featureName = "feature2";
    }

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public String execute() {
        return "Feature Two executed";
    }
}
