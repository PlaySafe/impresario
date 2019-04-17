package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * Returns the result of the first executable value among functions
 */
class FunctionChoose implements Function {

    private final List<Function> preFunctions;
    private final Condition preCondition;

    public FunctionChoose(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        this.preFunctions = definition.getPreFunctions();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute 'choose' due to the pre-condition does not match");
        }
        for (Function function : preFunctions) {
            try {
                return function.perform(input, definitions);
            }
            catch (ConditionNotMatchException e) {
                //Do nothing because there might be able to execute other function
            }
        }
        throw new ConditionNotMatchException("Cannot execute 'choose' due to cannot execute any function");
    }
}
