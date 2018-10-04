package org.companion.impresario;

import java.util.List;
import java.util.Map;

class FunctionLength implements Function {

    private final Function preFunction;
    private final Condition preCondition;

    public FunctionLength(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'length': Allow only 1 pre-function");
        }
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute function 'length' due to the pre-condition does not match");
        }
        String value = preFunction.perform(input, definitions);
        int length = value.length();
        return String.valueOf(length);
    }
}
