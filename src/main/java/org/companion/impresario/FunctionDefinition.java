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
 * Define the meta data compile the function for the builder.
 * this class store data from XML configuration.
 * </p>
 */
public class FunctionDefinition {

    private final String name;
    private final Condition preCondition;
    private final Map<String, List<Function>> preFunctions;

    /**
     * Map between order of parameter (from meta data) and its name
     */
    private final Map<Integer, String> metaParameters;

    /**
     * Map between parameter name and its value
     */
    private final Map<String, String> parameters;

    private FunctionDefinition(Builder builder) {
        this.name = Objects.requireNonNull(builder.name);

        Set<String> paramFunctions = new HashSet<>(builder.preFunctions.keySet());
        Set<String> paramAttributes = new HashSet<>(builder.parameters.keySet());
        Set<String> duplicate = Util.intersect(paramFunctions, paramAttributes);
        if (!duplicate.isEmpty()) {
            StringBuilder sb = new StringBuilder(128)
                    .append("Ambiguous parameter name(s) of function ")
                    .append(name)
                    .append(". Please check the following parameter(s) ")
                    .append(duplicate);
            throw new InvalidConfigurationException(sb.toString());
        }
        this.preCondition = builder.preCondition;
        this.preFunctions = Collections.unmodifiableMap(new HashMap<>(builder.preFunctions));
        this.parameters = Collections.unmodifiableMap(new HashMap<>(builder.parameters));
        this.metaParameters = Collections.unmodifiableMap(new LinkedHashMap<>(builder.metaParameters));
    }

    public String getName() {
        return name;
    }

    public Condition getPreCondition() {
        return preCondition;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, List<Function>> getPreFunctions() {
        return preFunctions;
    }

    public Map<Integer, String> getMetaParameters() {
        return this.metaParameters;
    }

    static final class Builder {

        private String name;
        private Condition preCondition;
        private Map<String, List<Function>> preFunctions = Collections.emptyMap();
        private Map<String, String> parameters;
        private Map<Integer, String> metaParameters;

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder setPreCondition(Condition preCondition) {
            this.preCondition = preCondition;
            return this;
        }

        Builder setPreFunction(Map<String, List<Function>> preFunctions) {
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

        FunctionDefinition build() {
            return new FunctionDefinition(this);
        }

    }

}
