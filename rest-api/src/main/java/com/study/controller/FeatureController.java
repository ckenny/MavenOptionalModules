package com.study.controller;

import com.study.feature.Feature;
import com.study.feature.FeatureFactory;
import com.study.pojo.FeatureEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeatureController {

    @Value("${spring.application.name}")
    private String appName;

    private FeatureFactory featureFactory;

    public FeatureController(FeatureFactory featureFactory) {
        this.featureFactory = featureFactory;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);

        return "home";
    }

    @RequestMapping(
            value = "/feature/{featureName}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> dynamicProcessMapping(@PathVariable("featureName") String featureName, @PathVariable("id") String id) {

        FeatureEntity entity = new FeatureEntity();

        if(featureName == null || featureName.isEmpty()) {
            entity.setMessage("Error: Malformed feature request.");
        } else {

            entity.setId(Integer.valueOf(id));
            featureName = featureName.toLowerCase();
            entity.setFeatureName(featureName);

            String operationResult = performFeatureOperation(entity.getFeatureName());

            entity.setMessage(operationResult);
        }

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    /**
     * This method is requesting feature factory for an object of feature. If the feature implementation is available
     * then it will execute the feature.execute() method.
     *
     * @param featureName name of feature to be executed
     */
    private String performFeatureOperation(String featureName) {

        // 1. get an instance of feature from FeatureFactory
        Feature feature = featureFactory.getFeatureInstance(featureName);

        // 2. perform operation on feature implementation class object if available else return error message
        if(feature == null) {
            return "Error: Received request for (" + featureName + ") featureName. Feature is NOT available.";
        } else {
            return feature.execute();
        }
    }

}
