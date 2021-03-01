package org.companion.impresario;

import java.util.Map;

/**
 * <p>
 * Returns value from the specific definition, properties, specific field,
 * value of map, or the value itself corresponds to the configuration.
 * </p>
 */
class FunctionGet implements Function {

    private final Function delegateFunction;
    private final Condition preCondition;

    public FunctionGet(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        String param = definition.getParameters().get(parameterName);
        if (VariableReflector.isProperties(param)) {
            delegateFunction = new FunctionGetProperties(param);
        }
        else if (VariableReflector.isField(param)) {
            delegateFunction = new FunctionGetValue(param);
        }
        else if (VariableReflector.isDefinition(param)) {
            delegateFunction = new FunctionGetDefinition(param);
        }
        else {
            delegateFunction = new FunctionReturn(param);
        }
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionGet due to the pre-condition does not match");
        }
        return delegateFunction.perform(input, definitions);
    }

    private class FunctionGetDefinition implements Function {

        private final String definitionKey;
        private final String definitionName;

        FunctionGetDefinition(String definition) {
            this.definitionKey = VariableReflector.reflectDefinitionKeyOf(definition);
            this.definitionName = VariableReflector.reflectDefinitionNameOf(definition);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            Map<String, Object> items = definitions.get(definitionName);
            if (items != null) {
                Object value = items.get(definitionKey);
                return String.valueOf(value);
            }
            return null;
        }
    }

    private class FunctionGetValue implements Function {

        private final Function getValueFromField;
        private final Function getValueFromMap;

        FunctionGetValue(String field) {
            getValueFromField = new FunctionGetMethodField(field);
            getValueFromMap = new FunctionGetFromMap(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            return (input instanceof Map) ? getValueFromMap.perform(input, definitions) :
                    getValueFromField.perform(input, definitions);
        }
    }

    private class FunctionGetMethodField implements Function {

        private final String methodName;

        FunctionGetMethodField(String field) {
            this.methodName = VariableReflector.reflectFieldMethodOf(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            Object result = VariableReflector.invoke(input, methodName);
            return (result == null) ? null : String.valueOf(result);
        }
    }

    private class FunctionGetFromMap implements Function {

        private final String key;

        FunctionGetFromMap(String key) {
            this.key = VariableReflector.reflectFieldOf(key);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            Object data = ((Map<String, ?>) input).get(key);
            return (data == null) ? null : String.valueOf(data);
        }
    }

    private class FunctionGetProperties implements Function {

        private final String propertiesKey;

        FunctionGetProperties(String propertiesKey) {
            this.propertiesKey = VariableReflector.reflectPropertiesOf(propertiesKey);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            return System.getProperty(propertiesKey);
        }
    }

}
