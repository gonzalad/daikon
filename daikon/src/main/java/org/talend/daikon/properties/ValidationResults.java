package org.talend.daikon.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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
        if (value == null) {
            return;
        }
        Map<String, ValidationResult> allValidationResults = value.getAllValidationResults();
        if (allValidationResults != null) {
            validationResults.putAll(allValidationResults);
        }
    }

    public void addValidationResult(String propName, ValidationResult value) {
        if (value == null) {
            return;
        }
        validationResults.put(propName, value);
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
}
