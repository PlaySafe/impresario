package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * <p>the negative index (-x): return since first character until the last x character exclude the last character e.g 9876543 substring -3 = 9876</p><br/>
 * <p>the positive index (+x): return since character x to the last character e.g. 123456 substring 2 = 3456</p>
 */
class FunctionSubstring implements Function {

    private final Function preFunction;
    private final Condition preCondition;
    private final int position;

    public FunctionSubstring(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'substring': Allow only 1 pre-function");
        }
        this.preCondition = definition.getPreCondition();
        this.position = Integer.valueOf(definition.getParam());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute function 'substring' due to the pre-condition does not match");
        }
        String value = preFunction.perform(input, definitions);
        int length = value.length();
        return (position > 0) ? value.substring(position, length) : value.substring(0, length + position);
    }
}
