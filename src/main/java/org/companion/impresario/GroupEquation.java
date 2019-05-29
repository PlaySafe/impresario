package org.companion.impresario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GroupEquation implements Equation {

    private List<Equation> equations;

    GroupEquation(List<Equation> equations) {
        this.equations = Collections.unmodifiableList(new ArrayList<>(equations));
    }

    @Override
    public BigDecimal perform(Object input) throws ConditionNotMatchException {
        for (Equation equation : equations) {
            try {
                return equation.perform(input);
            }
            catch (ConditionNotMatchException e) {
                //Intend doing nothing because some other equations might produce output
            }
        }
        throw new ConditionNotMatchException("Cannot solve equation because of no such condition matches");
    }
}
