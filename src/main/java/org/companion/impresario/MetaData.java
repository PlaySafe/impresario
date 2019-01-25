package org.companion.impresario;

import java.util.Map;

/**
 * A global definition for label generator which will be used to during create the generator.
 * All meta data of this will be used as the main configuration to map between name and the implementation
 * such as between condition name and the implementation, or between function name and its relate implementation.
 * User can arbitrary define meta data in the way they want, for example user can define mapping between
 * <ul>
 * <li>"xyz" and FunctionX</li>
 * <li>"XYz" and FunctionY</li>
 * <li>"Z" and FunctionABC</li>
 * <li>"ZZZ" and ConditionZZZ</li>
 * <li>"XYZ_W" and ConditionXYZ</li>
 * </ul>
 */
public interface MetaData {

    String getDefinitionTagItem();


    String getAttributeDefinitionName();


    String getAttributeDefinitionItemKey();


    String getAttributeDefinitionItemValue();


    String getAttributeFunctionName();


    String getAttributeFunctionParameter1();


    String getAttributeFunctionParameter2();


    String getAttributeConditionName();


    String getAttributeConditionParameter1();


    String getAttributeConditionParameter2();


    /**
     * There are 2 important parts,
     * <ol>
     * <li>Function name : a human readable string represent the function class</li>
     * <li>Function class : a full package class name refer to the implementation</li>
     * </ol>
     *
     * @return a map that specify between the function name and function class (implementation).
     */
    Map<String, Class<? extends Function>> getMetaFunctions();

    /**
     * There are 2 important parts,
     * <ol>
     * <li>Condition name : a human readable string represent the condition class</li>
     * <li>Condition class : a full package class name refer to the implementation</li>
     * </ol>
     *
     * @return a map that specify between the condition name and condition class (implementation).
     */
    Map<String, Class<? extends Condition>> getMetaConditions();

}
