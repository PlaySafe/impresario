package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * <p>The negative index (-X): return last X character e.g 9876543 cut off -3 = 543</p><br/>
 * <p>The positive index (+X): return first X character e.g. 123456 cut off 2 = 12</p>
 */
class FunctionCutOff implements Function {

    private final Function preFunction;
    private final Condition preCondition;
    private final int position;

    public FunctionCutOff(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function of FunctionCutOff: Allow only 1 pre-function");
        }
        this.preCondition = definition.getPreCondition();
        this.position = Integer.valueOf(definition.getParameter1());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionCutOff due to the pre-condition does not match");
        }
        String value = preFunction.perform(input, definitions);
        int length = value.length();
        return (position >= 0) ? value.substring(0, position) : value.substring(length + position, length);
    }
}
