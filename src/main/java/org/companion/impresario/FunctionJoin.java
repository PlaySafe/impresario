package org.companion.impresario;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Concatenate each string result of all functions with delimeter
 */
class FunctionJoin implements Function {

    private final List<Function> preFunctions;
    private final Condition preCondition;
    private final String delimiter;

    public FunctionJoin(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        this.preFunctions = definition.getPreFunctions();
        String delimiter = definition.getParam();
        if (delimiter == null) {
            throw new IllegalArgumentException("There is no delimiter specify");
        }
        this.delimiter = delimiter;
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute 'join' due to the pre-condition does not match");
        }
        StringJoiner stringJoiner = new StringJoiner(delimiter);
        for (Function function : preFunctions) {
            stringJoiner.add(function.perform(input, definitions));
        }
        return stringJoiner.toString();
    }
}
