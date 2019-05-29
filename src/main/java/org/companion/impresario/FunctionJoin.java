package org.companion.impresario;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Returns value after concatenate each string from all functions with delimiter
 */
class FunctionJoin implements Function {

    private final List<Function> preFunctions;
    private final Condition preCondition;
    private final String delimiter;

    public FunctionJoin(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        this.preFunctions = Objects.requireNonNull(definition.getPreFunctions());
        this.delimiter = Objects.requireNonNull(definition.getParameter1(), "No such delimiter of FunctionJoin");
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionJoin due to the pre-condition does not match");
        }
        StringJoiner stringJoiner = new StringJoiner(delimiter);
        for (Function function : preFunctions) {
            stringJoiner.add(function.perform(input, definitions));
        }
        return stringJoiner.toString();
    }
}
