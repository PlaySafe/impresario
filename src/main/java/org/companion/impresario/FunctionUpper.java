package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns the upper-case of a string
 * </p>
 */
class FunctionUpper implements Function {

    private final Condition preCondition;
    private final Function valueFunction;

    public FunctionUpper(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameterName, Collections.emptyList());
        if (preFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreFunction(1, preFunctions.size(), getClass()));
        }
        this.valueFunction = preFunctions.get(0);
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionUpper due to the pre-condition does not match");
        }
        String value = valueFunction.perform(input, definitions);
        return value.toUpperCase();
    }

}
