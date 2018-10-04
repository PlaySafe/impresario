package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Define the meta data compile the function for the builder.
 * this class store data from XML configuration.
 */
class FunctionDefinition {

    private final String target;
    private final String param;
    private final Condition preCondition;
    private final List<Function> preFunctions;
    private final String logic;

    private FunctionDefinition(Builder builder) {
        this.target = builder.target;
        this.param = builder.param;
        this.preCondition = builder.preCondition;
        this.preFunctions = Collections.unmodifiableList(new ArrayList<>(builder.preFunctions));
        this.logic = builder.logic;
    }

    String getTarget() {
        return target;
    }

    String getParam() {
        return param;
    }

    Condition getPreCondition() {
        return preCondition;
    }

    List<Function> getPreFunctions() {
        return preFunctions;
    }

    String getLogic() {
        return logic;
    }

    static final class Builder {

        private String target;
        private String param;
        private Condition preCondition;
        private List<Function> preFunctions = new ArrayList<>();
        private String logic;

        Builder setTarget(String target) {
            this.target = target;
            return this;
        }

        Builder setParam(String param) {
            this.param = param;
            return this;
        }

        Builder setPreCondition(Condition preCondition) {
            this.preCondition = preCondition;
            return this;
        }

        Builder addPreFunction(Function preFunction) {
            this.preFunctions.add(preFunction);
            return this;
        }

        Builder setLogic(String logic) {
            this.logic = logic;
            return this;
        }

        FunctionDefinition build() {
            return new FunctionDefinition(this);
        }
    }

}
