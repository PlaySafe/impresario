package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a value after concatenate string from all pre-functions together. The "null" will be appended if any
 * pre-function returns {@code null}. This function requires at least 2 pre-functions.
 * </p>
 */
class FunctionConcat implements Function {

    private final Condition preCondition;
    private final List<Function> preFunctions;

    public FunctionConcat(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameterName, Collections.emptyList());
        if (preFunctions.size() < 2) {
            throw new InvalidConfigurationException("FunctionConcat requires at least 2 pre-functions");
        }
        this.preCondition = definition.getPreCondition();
        this.preFunctions = preFunctions;
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionConcat due to the pre-condition does not match");
        }
        StringBuilder stringBuilder = new StringBuilder(64);
        for (Function function : preFunctions) {
            stringBuilder.append(function.perform(input, definitions));
        }
        return stringBuilder.toString();
    }
}
