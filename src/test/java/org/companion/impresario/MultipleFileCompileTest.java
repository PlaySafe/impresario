package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MultipleFileCompileTest {

    private final MetaData metaData;

    public MultipleFileCompileTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        this.metaData = new MetaDataFactory().compile(metaResource);
    }

    @Test
    public void expectCompleteMultipleLabel() throws IOException {
        File multipleFileCompile = new File("src/test/resources/multiple_file_compile.xml");
        LabelGeneratorFactory labelGeneratorFactory = new MultipleLabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(multipleFileCompile);
        Assert.assertNotEquals(0, labelGenerators.size());
    }

    @Test
    public void expectCompileMultipleValidationRule() throws IOException {
        File multipleFileCompile = new File("src/test/resources/multiple_file_compile.xml");
        ValidationRuleFactory validationRuleFactory = new MultipleValidationRuleFactory(metaData);
        Map<String, ValidationRule> validationRules = validationRuleFactory.compile(multipleFileCompile);
        Assert.assertNotEquals(0, validationRules.size());
    }

    @Test
    public void expectCompileMultipleEquations() throws IOException {
        File multipleFileCompile = new File("src/test/resources/multiple_file_compile.xml");
        EquationFactory validationRuleFactory = new MultipleEquationFactory(metaData);
        Map<String, Equation> equationMap = validationRuleFactory.compile(multipleFileCompile);
        Assert.assertNotEquals(0, equationMap.size());
    }
}
