package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns {@code true} if parameter is not null, otherwise {@code false}
 * </p>
 */
class ConditionIsNotNull implements Condition {

    private final Function function;

    public ConditionIsNotNull(ConditionDefinition definition) {
        String parameter = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> functions = definition.getPreFunctions().getOrDefault(parameter, Collections.emptyList());
        if (functions.isEmpty()) {
            throw new InvalidConfigurationException("No such pre-function of ConditionIsNotNull or not define 'result-as' attribute");
        }
        if (functions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreFunction(1, functions.size(), getClass()));
        }
        this.function = definition.getPreFunctions().get(parameter).get(0);
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result = function.perform(input, definitions);
        return result != null;
    }
}
