package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns the result of the first executable value among functions. This function requires at least 2 pre-functions.
 * It will try to execute the function in the list one by one until find the one that match condition.
 * However, if no function matches the condition, the {@link ConditionNotMatchException} will be thrown.
 * </p>
 */
class FunctionChoose implements Function {

    private final Condition preCondition;
    private final List<Function> preFunctions;

    public FunctionChoose(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameterName, Collections.emptyList());
        if (preFunctions.size() < 2) {
            throw new InvalidConfigurationException("FunctionChoose requires at least 2 pre-functions");
        }
        this.preCondition = definition.getPreCondition();
        this.preFunctions = preFunctions;
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionChoose due to the pre-condition does not match");
        }
        for (Function function : preFunctions) {
            try {
                return function.perform(input, definitions);
            }
            catch (ConditionNotMatchException e) {
                //Do nothing because there might be able to execute other function
            }
        }
        throw new ConditionNotMatchException("Cannot execute FunctionChoose due to no function to be executed");
    }
}
