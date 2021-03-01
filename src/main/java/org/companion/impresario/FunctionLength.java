package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns length of the string
 * </p>
 */
class FunctionLength implements Function {

    private final Condition preCondition;
    private final Function valueFunction;

    public FunctionLength(FunctionDefinition definition) {
        String parameterText = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameterText, Collections.emptyList());
        if (preFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreFunction(1, preFunctions.size(), getClass()));
        }
        this.preCondition = definition.getPreCondition();
        this.valueFunction = preFunctions.get(0);
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionLength due to the pre-condition does not match");
        }
        String value = valueFunction.perform(input, definitions);
        int length = value.length();
        return String.valueOf(length);
    }
}
