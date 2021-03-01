package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns the lower-case of a string
 * </p>
 */
class FunctionLower implements Function {

    private final Condition preCondition;
    private final Function valueFunction;

    public FunctionLower(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameterName, Collections.emptyList());
        if (preFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreFunction(1, preFunctions.size(), getClass()));
        }
        this.preCondition = definition.getPreCondition();
        this.valueFunction = preFunctions.get(0);
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionLower due to the pre-condition does not match");
        }
        String value = valueFunction.perform(input, definitions);
        return value.toLowerCase();
    }

}
