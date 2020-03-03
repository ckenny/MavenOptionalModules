package com.study.feature;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FeatureFactory {

    private Map<String, Feature> featuresMap = new HashMap<>();

    public FeatureFactory(List<Feature> myServices) {
        myServices.forEach(service -> {
            featuresMap.put( service.getFeatureName(), service);
        });
    }

    public Feature getFeatureInstance(String featureName) {
        return featuresMap.get(featureName);
    }

}
