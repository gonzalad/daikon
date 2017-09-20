package org.talend.daikon.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.talend.daikon.properties.ValidationResult.Result;

/**
 * Contains and process validation result for every property
 */
public class ValidationResults {

    private Map<String, ValidationResult> validationResults;

    public ValidationResults() {
        this.validationResults = new LinkedHashMap<>();
    }

    /**
     * @return list of all properties warnings (ValidationResult equals Result.Warning and message should be specified)
     */
    public Map<String, ValidationResult> getWarnings() {
        Map<String, ValidationResult> warnings = new HashMap<>();
        for (Entry<String, ValidationResult> vr : validationResults.entrySet()) {
            if (ValidationResult.Result.WARNING == vr.getValue().getStatus()) {
                warnings.put(vr.getKey(), vr.getValue());
            }
        }
        return warnings;
    }

    /**
     * @return list of all properties errors (ValidationResult equals Result.Error and error message should be
     * specified)
     */
    public Map<String, ValidationResult> getErrors() {
        Map<String, ValidationResult> errors = new HashMap<>();
        for (Entry<String, ValidationResult> vr : validationResults.entrySet()) {
            if (ValidationResult.Result.ERROR == vr.getValue().getStatus()) {
                errors.put(vr.getKey(), vr.getValue());
            }
        }
        return errors;
    }

    public void addValidationResults(ValidationResults value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        Map<String, ValidationResult> allValidationResults = value.getAllValidationResults();
        if (allValidationResults != null) {
            for (Entry<String, ValidationResult> validationResult : allValidationResults.entrySet()) {
                addValidationResult(validationResult.getKey(), validationResult.getValue());
            }
        }
    }

    public void addValidationResult(String propName, ValidationResult value) {
        if (value == null) {
            return;
        }
        ValidationResult containing = validationResults.get(propName);
        if (containing == null || shouldRewrite(containing, value)) {
            validationResults.put(propName, value);
        }
    }

    /**
     * Check whether newer validation result status of processed property is more critical than the containing one. If so, we
     * should update the value.
     */
    private boolean shouldRewrite(ValidationResult containing, ValidationResult newValue) {
        if ((containing.getStatus() == Result.OK && newValue.getStatus() != Result.OK)
                || (containing.getStatus() == Result.WARNING && newValue.getStatus() == Result.ERROR))
            return true;
        return false;
    }

    /**
     * @return unmodifiable Map where keys are problemKeys and values are all ValidationResults
     */
    public Map<String, ValidationResult> getAllValidationResults() {
        return Collections.unmodifiableMap(validationResults);
    }

    /**
     * @return actual ValidationResult.Result depending on available errors and warning
     */
    public ValidationResult.Result calculateValidationStatus() {
        ValidationResult.Result status = ValidationResult.Result.OK;
        if (!getErrors().isEmpty()) {
            status = ValidationResult.Result.ERROR;
        } else if (!getWarnings().isEmpty()) {
            status = ValidationResult.Result.WARNING;
        }
        return status;
    }

    /**
     * @return empty String when no problems in maps or full errors and warnings message
     */
    private String getGeneralProblemsMessage() {
        StringBuilder message = new StringBuilder();

        for (Object error : getErrors().values()) {
            message.append(error.toString()).append("\n");
        }
        for (Object warning : getWarnings().values()) {
            message.append(warning.toString()).append("\n");
        }
        return message.substring(0, message.length() - 1);
    }

    /**
     * @return "OK" when there are no problems in maps or {@link #getGeneralProblemsMessage()} when they are present
     */
    @Override
    public String toString() {
        if (validationResults.isEmpty())
            return "OK";
        else
            return getGeneralProblemsMessage();
    }

    public boolean isEmpty() {
        return validationResults.isEmpty();
    }
}
