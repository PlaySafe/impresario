package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * Returns the string before specific index
 * <p>The negative index (-X): return since first character until the position X from behind e.g 987654 substring -3 = 987</p><br/>
 * <p>The positive index (+X): return since character X from the beginning to the last character e.g. 123456 substring 2 = 12</p>
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
            throw new IllegalArgumentException("Ambiguous pre-function for 'cut off': Allow only 1 pre-function");
        }
        this.preCondition = definition.getPreCondition();
        this.position = Integer.valueOf(definition.getParam());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute function 'cut off' due to the pre-condition does not match");
        }
        String value = preFunction.perform(input, definitions);
        return (position > 0) ? value.substring(0, position) : value.substring(0, value.length() + position);
    }
}
