package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a character at the specific index. The positive returns character from front, and negative from the back.
 * For example the input is "Hello World"
 * </p>
 * <ul>
 * <li>The index of 0 is 'H'</li>
 * <li>The index of 4 is 'o'</li>
 * <li>The index of -3 is 'r'</li>
 * </ul>
 */
class FunctionCharAt implements Function {

    private final Function preFunction;
    private final Condition preCondition;
    private final int position;

    public FunctionCharAt(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'char at': Allow only 1 pre-function");
        }
        this.preCondition = definition.getPreCondition();
        this.position = Integer.valueOf(definition.getParameter1());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute function 'char at' due to the pre-condition does not match");
        }
        String value = preFunction.perform(input, definitions);
        char c = (position >= 0) ? value.charAt(position) : value.charAt(value.length() + position);
        return String.valueOf(c);
    }
}
