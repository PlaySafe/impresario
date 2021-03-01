package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns {@code true} if parameter1 is null, otherwise {@code false}
 * </p>
 */
class ConditionIsNull implements Condition {

    private final Function function;

    public ConditionIsNull(ConditionDefinition definition) {
        String parameter = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> functions = definition.getPreFunctions().getOrDefault(parameter, Collections.emptyList());
        if (functions.isEmpty()) {
            throw new InvalidConfigurationException("No such pre-function of ConditionIsNull or not define 'result-as' attribute");
        }
        if (functions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreFunction(1, functions.size(), getClass()));
        }
        this.function = definition.getPreFunctions().get(parameter).get(0);
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function.perform(input, definitions);
        return result1 == null;
    }
}
