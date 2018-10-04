package org.companion.impresario;

import java.util.Map;

/**
 * Retrieve the value from the definition, properties, specific field, or the value itself corresponds to the configuration.
 */
class FunctionGet implements Function {

    private Function delegateFunction;
    private final Condition preCondition;

    public FunctionGet(FunctionDefinition definition) {
        String target = definition.getTarget();
        if (ReflectMethodGenerator.isProperties(target)) {
            delegateFunction = new FunctionGetProperties(target);
        }
        else if (ReflectMethodGenerator.isField(target)) {
            delegateFunction = new FunctionGetField(target);
        }
        else if (ReflectMethodGenerator.isDefinition(target)) {
            delegateFunction = new FunctionGetDefinition(target);
        }
        else {
            delegateFunction = new FunctionGetValue(target);
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
     * Retrieve value from the definitions corresponds to the definition pattern
     */
    private class FunctionGetDefinition implements Function {

        private String definitionKey;
        private String definitionName;

        FunctionGetDefinition(String definition) {
            this.definitionKey = ReflectMethodGenerator.reflectDefinitionKeyOf(definition);
            this.definitionName = ReflectMethodGenerator.reflectDefinitionNameOf(definition);
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
            this.methodName = ReflectMethodGenerator.reflectFieldMethodOf(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) {
            String value = ReflectMethodGenerator.invoke(input, methodName);
            return (value == null) ? null : String.valueOf(value);
        }
    }

    /**
     * Retrieve value from the System properties corresponds to the specific key
     */
    private class FunctionGetProperties implements Function {

        private String propertiesKey;

        FunctionGetProperties(String properties) {
            this.propertiesKey = ReflectMethodGenerator.reflectPropertiesOf(properties);
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
