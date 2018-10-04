package org.companion.impresario;

import java.util.Map;

/**
 * Retrieve the value from the definition, properties, specific field, or the value itself corresponds to the configuration.
 */
class FunctionGet implements Function {

    private Function delegateFunction;
    private final Condition preCondition;

    public FunctionGet(FunctionDefinition definition) {
        String param = definition.getParam();
        if (VariableReflector.isProperties(param)) {
            delegateFunction = new FunctionGetProperties(param);
        }
        else if (VariableReflector.isField(param)) {
            delegateFunction = new FunctionGetField(param);
        }
        else if (VariableReflector.isDefinition(param)) {
            delegateFunction = new FunctionGetDefinition(param);
        }
        else {
            delegateFunction = new FunctionGetValue(param);
        }
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute function 'get' due to the pre-condition does not match");
        }
        return delegateFunction.perform(input, definitions);
    }

    /**
     * Retrieve value from the specific definitions corresponds to the definition pattern
     */
    private class FunctionGetDefinition implements Function {

        private String definitionKey;
        private String definitionName;

        FunctionGetDefinition(String definition) {
            this.definitionKey = VariableReflector.reflectDefinitionKeyOf(definition);
            this.definitionName = VariableReflector.reflectDefinitionNameOf(definition);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            Object value = definitions.get(definitionName).get(definitionKey);
            return String.valueOf(value);
        }
    }

    /**
     * Retrieve value from calling the method name corresponds to the specific field
     */
    private class FunctionGetField implements Function {

        private String methodName;

        FunctionGetField(String field) {
            this.methodName = VariableReflector.reflectFieldMethodOf(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            String value = VariableReflector.invoke(input, methodName);
            return (value == null) ? null : String.valueOf(value);
        }
    }

    /**
     * Retrieve value from the System properties corresponds to the specific key
     */
    private class FunctionGetProperties implements Function {

        private String propertiesKey;

        FunctionGetProperties(String properties) {
            this.propertiesKey = VariableReflector.reflectPropertiesOf(properties);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            return System.getProperty(propertiesKey);
        }
    }

    /**
     * Returns the specific value directly
     */
    private class FunctionGetValue implements Function {

        private final String value;

        FunctionGetValue(String value) {
            this.value = value;
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            return value;
        }
    }


}
