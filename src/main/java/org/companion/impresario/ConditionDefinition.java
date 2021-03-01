package org.companion.impresario;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * The general definition of all conditions for {@link ConditionBuilder}.
 * Its property will corresponds to the configuration. This class is used to store all configuration data regardless
 * the real condition type.
 * </p>
 */
public class ConditionDefinition {

    private final String name;
    private final Map<String, List<Condition>> preConditions;
    private final Map<String, List<Function>> preFunctions;

    /**
     * Map between order of parameter (from meta data) and its name
     */
    private final Map<Integer, String> metaParameters;

    /**
     * Map between parameter name and its value
     */
    private final Map<String, String> parameters;

    private ConditionDefinition(Builder builder) {
        this.name = Objects.requireNonNull(builder.name);

        Set<String> paramConditions = new HashSet<>(builder.preConditions.keySet());
        Set<String> paramFunctions = new HashSet<>(builder.preFunctions.keySet());
        Set<String> paramAttributes = new HashSet<>(builder.parameters.keySet());
        Set<String> duplicateCondition = Util.intersect(paramConditions, paramAttributes);
        Set<String> duplicateFunction = Util.intersect(paramFunctions, paramAttributes);
        Set<String> duplicate = Util.union(duplicateCondition, duplicateFunction);
        if (!duplicate.isEmpty()) {
            StringBuilder sb = new StringBuilder(128)
                    .append("Ambiguous parameter name(s) of condition ")
                    .append(name)
                    .append(". Please check the following parameter(s) ")
                    .append(duplicate);
            throw new InvalidConfigurationException(sb.toString());
        }

        this.preConditions = Collections.unmodifiableMap(new HashMap<>(builder.preConditions));
        this.preFunctions = Collections.unmodifiableMap(new HashMap<>(builder.preFunctions));
        this.parameters = Collections.unmodifiableMap(new HashMap<>(builder.parameters));
        this.metaParameters = Collections.unmodifiableMap(new LinkedHashMap<>(builder.metaParameters));
    }

    public String getName() {
        return name;
    }

    public Map<String, List<Condition>> getPreConditions() {
        return preConditions;
    }

    public Map<String, List<Function>> getPreFunctions() {
        return preFunctions;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<Integer, String> getMetaParameters() {
        return this.metaParameters;
    }

    static final class Builder {

        private String name;
        private Map<String, List<Condition>> preConditions = Collections.emptyMap();
        private Map<String, List<Function>> preFunctions = Collections.emptyMap();
        private Map<String, String> parameters;
        private Map<Integer, String> metaParameters;

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder setPreConditions(Map<String, List<Condition>> preConditions) {
            this.preConditions = new HashMap<>(preConditions);
            return this;
        }

        Builder setPreFunctions(Map<String, List<Function>> preFunctions) {
            this.preFunctions = new HashMap<>(preFunctions);
            return this;
        }

        Builder setParameters(Map<String, String> parameters) {
            this.parameters = new HashMap<>(parameters);
            return this;
        }

        Builder setMetaParameters(Map<Integer, String> metaParameters) {
            this.metaParameters = new LinkedHashMap<>(metaParameters);
            return this;
        }

        ConditionDefinition build() {
            return new ConditionDefinition(this);
        }

    }
}
