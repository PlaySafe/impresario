package org.companion.impresario;

/**
 * <p>
 * General interface of all label generators. The implementation can arbitrary decorate function, and condition.
 * </p>
 */
public interface LabelGenerator {

    /**
     * Generates the label corresponds to the data from input
     *
     * @param input the arbitrary object for retrieving data
     *
     * @return a string result
     *
     * @throws ConditionNotMatchException if cannot generate due to the generate condition doesn't match
     */
    String labelOf(Object input) throws ConditionNotMatchException;

}
