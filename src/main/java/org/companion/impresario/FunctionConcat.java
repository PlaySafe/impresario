package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * Returns value after concatenate string from all functions together
 */
class FunctionConcat implements Function {

    private final List<Function> preFunctions;
    private final Condition preCondition;

    public FunctionConcat(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        this.preFunctions = definition.getPreFunctions();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionConcat due to the pre-condition does not match");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Function function : preFunctions) {
            stringBuilder.append(function.perform(input, definitions));
        }
        return stringBuilder.toString();
    }
}
