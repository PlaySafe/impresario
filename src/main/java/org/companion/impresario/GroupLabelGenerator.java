package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Facade of the label generators to simplify the API due to there might have many label generators for the same purpose
 * but different condition to perform.
 */
class GroupLabelGenerator implements LabelGenerator {

    private List<LabelGenerator> labelGenerators;

    GroupLabelGenerator(List<LabelGenerator> labelGenerators) {
        this.labelGenerators = Collections.unmodifiableList(new ArrayList<>(labelGenerators));
    }

    @Override
    public String labelOf(Object input) throws ConditionNotMatchException {
        for (LabelGenerator labelGenerator : labelGenerators) {
            try {
                return labelGenerator.labelOf(input);
            }
            catch (ConditionNotMatchException e) {
                //Intend doing nothing because some other generators might produce output
            }
        }
        throw new ConditionNotMatchException("Cannot produce any output because of no such condition matches");
    }
}
