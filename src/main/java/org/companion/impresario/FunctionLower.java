package org.companion.impresario;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Returns value after converts string to lower case
 */
class FunctionLower implements Function {

    private final Function preFunction;
    private final Condition preCondition;

    public FunctionLower(FunctionDefinition definition) {
        List<Function> preFunctions = Objects.requireNonNull(definition.getPreFunctions());
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function of FunctionLower: Allow only 1 pre-function");
        }
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionLower due to the pre-condition does not match");
        }
        String value = preFunction.perform(input, definitions);
        return value.toLowerCase();
    }

}
