package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Due to one context can be valid from various set of inputs.
 * This class encapsulate the set of validation rules
 * and let user focus on only context rather than particular rule of each context.
 * Only 1 rule valid will be considered as valid.
 * <p>
 * For example, the name can consider as valid if either
 * </p>
 *
 * <ul>
 * <li>first name, and last name provided</li>
 * <li>first name, middle name, and last name provided</li>
 * </ul>
 *
 * <p>but neither</p>
 * <ul>
 * <li>first name, and middle name provided</li>
 * <li>middle name, and last name provided</li>
 * </ul>
 */
class GroupValidationRule implements ValidationRule {

    private final List<ValidationRule> validationRules;

    GroupValidationRule(List<ValidationRule> validationRules) {
        this.validationRules = Collections.unmodifiableList(new ArrayList<>(validationRules));
    }

    @Override
    public boolean validate(Object input) throws ConditionNotMatchException {
        for (ValidationRule validationRule : validationRules) {
            if (validationRule.validate(input)) {
                return true;
            }
        }
        return false;
    }
}
