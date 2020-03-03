package com.study.feature;

import org.springframework.stereotype.Component;

@Component
public class FeatureOneImplementation implements Feature {

    private String featureName;

    public FeatureOneImplementation() {
        featureName = "feature1";
    }

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public String execute() {
        return "Feature One executed";
    }

}
