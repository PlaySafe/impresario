package org.companion.impresario;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class DefaultMetaData implements MetaData {

    private final String tagDefinitionData;
    private final String attributeDefinitionName;
    private final String attributeDefinitionItemKey;
    private final String attributeDefinitionItemValue;

    private final String attributeFunctionName;
    private final String attributeConditionName;

    private final Map<String, Class<? extends Function>> metaFunctions;
    private final Map<String, Class<? extends Condition>> metaConditions;

    private final Map<String, Map<Integer, String>> metaFunctionParameters;
    private final Map<String, Map<Integer, String>> metaConditionParameters;

    private DefaultMetaData(Builder builder) {
        this.tagDefinitionData = builder.tagDefinitionData;
        this.attributeDefinitionName = builder.attributeDefinitionName;
        this.attributeDefinitionItemKey = builder.attributeDefinitionItemKey;
        this.attributeDefinitionItemValue = builder.attributeDefinitionItemValue;

        this.attributeFunctionName = builder.attributeFunctionName;
        this.attributeConditionName = builder.attributeConditionName;

        this.metaFunctions = Collections.unmodifiableMap(new HashMap<>(builder.metaFunctions));
        this.metaConditions = Collections.unmodifiableMap(new HashMap<>(builder.metaConditions));

        this.metaFunctionParameters = Collections.unmodifiableMap(new LinkedHashMap<>(builder.metaFunctionParameters));
        this.metaConditionParameters = Collections.unmodifiableMap(new LinkedHashMap<>(builder.metaConditionParameters));
    }

    @Override
    public String getDefinitionTagItem() {
        return tagDefinitionData;
    }

    @Override
    public String getAttributeDefinitionName() {
        return attributeDefinitionName;
    }

    @Override
    public String getAttributeDefinitionItemKey() {
        return attributeDefinitionItemKey;
    }

    @Override
    public String getAttributeDefinitionItemValue() {
        return attributeDefinitionItemValue;
    }

    @Override
    public String getAttributeFunctionName() {
        return attributeFunctionName;
    }

    @Override
    public String getAttributeConditionName() {
        return attributeConditionName;
    }

    @Override
    public Map<String, Class<? extends Function>> getMetaFunctions() {
        return metaFunctions;
    }

    @Override
    public Map<String, Map<Integer, String>> getMetaFunctionParameters() {
        return this.metaFunctionParameters;
    }

    @Override
    public Map<String, Class<? extends Condition>> getMetaConditions() {
        return metaConditions;
    }

    @Override
    public Map<String, Map<Integer, String>> getMetaConditionParameters() {
        return this.metaConditionParameters;
    }

    static final class Builder {

        private final Map<String, Class<? extends Function>> metaFunctions;
        private final Map<String, Class<? extends Condition>> metaConditions;
        private String tagDefinitionData;
        private String attributeDefinitionName;
        private String attributeDefinitionItemKey;
        private String attributeDefinitionItemValue;
        private String attributeFunctionName;
        private String attributeConditionName;
        private Map<String, Map<Integer, String>> metaFunctionParameters;
        private Map<String, Map<Integer, String>> metaConditionParameters;


        Builder() {
            this.metaFunctions = new HashMap<>();
            this.metaConditions = new HashMap<>();
        }

        Builder setDefinitionItemTag(String tagDefinitionData) {
            this.tagDefinitionData = tagDefinitionData;
            return this;
        }

        Builder setAttributeDefinitionName(String definitionName) {
            this.attributeDefinitionName = definitionName;
            return this;
        }

        Builder setAttributeDefinitionItemKey(String attributeItemKey) {
            this.attributeDefinitionItemKey = attributeItemKey;
            return this;
        }

        Builder setAttributeDefinitionItemValue(String attributeItemValue) {
            this.attributeDefinitionItemValue = attributeItemValue;
            return this;
        }

        Builder setAttributeFunctionName(String name) {
            this.attributeFunctionName = name;
            return this;
        }

        Builder setAttributeConditionName(String name) {
            this.attributeConditionName = name;
            return this;
        }

        Builder setMetaFunction(Map<String, String> metaFunctions) throws ClassNotFoundException {
            for (Map.Entry<String, String> eachMeta : metaFunctions.entrySet()) {
                String className = eachMeta.getValue();
                Class<? extends Function> functionClass = (Class<? extends Function>) Class.forName(className);
                this.metaFunctions.put(eachMeta.getKey(), functionClass);
            }
            return this;
        }

        Builder setMetaFunctionParameters(Map<String, Map<Integer, String>> metaFunctionParameters) {
            this.metaFunctionParameters = new LinkedHashMap<>(metaFunctionParameters);
            return this;
        }

        Builder setMetaCondition(Map<String, String> metaConditions) throws ClassNotFoundException {
            for (Map.Entry<String, String> eachMeta : metaConditions.entrySet()) {
                String className = eachMeta.getValue();
                Class<? extends Condition> functionClass = (Class<? extends Condition>) Class.forName(className);
                this.metaConditions.put(eachMeta.getKey(), functionClass);
            }
            return this;
        }

        Builder setMetaConditionParameters(Map<String, Map<Integer, String>> metaConditionParameters) {
            this.metaConditionParameters = new LinkedHashMap<>(metaConditionParameters);
            return this;
        }

        MetaData build() {
            return new DefaultMetaData(this);
        }
    }
}
