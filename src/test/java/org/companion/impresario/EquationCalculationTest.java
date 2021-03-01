package org.companion.impresario;

import data.GeneralProduct;
import data.Product;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class EquationCalculationTest {

    private final Map<String, Equation> equations;

    public EquationCalculationTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/equation_calculation.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        EquationFactory equationFactory = new SingleEquationFactory(metaData);
        Map<String, Equation> equationMap = equationFactory.compile(configResource);
        Assert.assertNotNull(equationMap);

        this.equations = equationMap;
    }

    @Test
    public void expectPreciseDigitOfResult() throws ConditionNotMatchException {
        Equation equation = equations.get("(7/3)_15_DECIMAL_DIGIT");
        Assert.assertNotNull(equation);

        Map<String, Object> param = new HashMap<>();
        param.put("val1", 7);
        param.put("val2", 3);

        BigDecimal result = equation.perform(param);
        Assert.assertEquals("2.333333333333333", result.toString());
    }

    @Test
    public void expectRoundup() throws ConditionNotMatchException {
        Equation equation = equations.get("((5x4)+(7/3)+(0-2))_ROUND_UP_2_DECIMAL");
        Assert.assertNotNull(equation);

        Map<String, Object> param = new HashMap<>();
        param.put("five", 5);
        param.put("four", 4);
        param.put("seven", 7);
        param.put("three", 3);
        param.put("zero", 0);
        param.put("two", 2);

        BigDecimal result = equation.perform(param);
        Assert.assertEquals("20.34", result.toString());
    }

    @Test
    public void expectValidAmountIncludeVat() throws ConditionNotMatchException {
        Equation equation = equations.get("PRODUCT_AMOUNT_INCLUDE_VAT");
        Assert.assertNotNull(equation);

        Product product = new GeneralProduct("13.33", "7.0");
        BigDecimal grossAmount = equation.perform(product);
        Assert.assertEquals("14.26", grossAmount.toString());
    }

    @Test
    public void expectValidAmountAfterDiscount() throws ConditionNotMatchException {
        Equation equation = equations.get("GROSS_AMOUNT_AFTER_DISCOUNT_100_EVERY_1000");
        Assert.assertNotNull(equation);

        Product product = new GeneralProduct("2725", "7.0");
        BigDecimal grossAmount = equation.perform(product);
        Assert.assertEquals("2701.75", grossAmount.toString());
    }

    @Test
    public void expectTotalAmountIncludeTip() throws ConditionNotMatchException {
        Equation equation = equations.get("MUL_GROUP_PAY_INCLUDE_TIP");
        Assert.assertNotNull(equation);

        Product product999 = new GeneralProduct("999", "7.0");
        Product product1000 = new GeneralProduct("1000", "7.0");
        Product product10000 = new GeneralProduct("10000", "7.0");

        BigDecimal totalPayFor999 = equation.perform(product999);
        BigDecimal totalPayFor1000 = equation.perform(product1000);
        BigDecimal totalPayFor10000 = equation.perform(product10000);

        Assert.assertEquals("1009", totalPayFor999.toString());
        Assert.assertEquals("1100", totalPayFor1000.toString());
        Assert.assertEquals("10200", totalPayFor10000.toString());
    }

    @Test
    public void expectCorrectAmountOfBacteriaAfter3Seconds() throws ConditionNotMatchException {
        Equation equation = equations.get("AMOUNT_OF_BACTERIA_POWER_BY_2_EVERY_SECOND");
        Assert.assertNotNull(equation);

        Map<String, Integer> param = new HashMap<>();
        param.put("second", 3);
        param.put("amount", 3);

        BigDecimal totalAmount = equation.perform(param);
        Assert.assertEquals("6561", totalAmount.toString());
    }

    @Test
    public void expectCorrectValueAfterModuloPropertiesAndDefinitionWorksTogether() throws ConditionNotMatchException {
        System.setProperty("multiplier", "5");
        Equation equation = equations.get("MULTIPLY_VALUE_WITH_PROPERTIES_THEN_MOD_BY_DEFINITION");
        Assert.assertNotNull(equation);

        BigDecimal result = equation.perform(null);
        Assert.assertEquals("55.6117890", result.toString());
    }
}
