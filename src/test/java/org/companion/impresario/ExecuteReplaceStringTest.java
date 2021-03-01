package org.companion.impresario;

import data.FlatWallet;
import data.Wallet;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExecuteReplaceStringTest {

    private final Map<String, LabelGenerator> labelGenerators;

    public ExecuteReplaceStringTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/execute_replace_string.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test
    public void expectReplaceStringBySpecificKey() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("BY_SPECIFIC_KEY");

        String result = labelGenerator.labelOf(null);
        Assert.assertEquals("Hello John!", result);
    }

    @Test
    public void expectReplaceStringDefinitionAtSpecificKeyByField() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_DEFINITION_AT_SPECIFIC_KEY_BY_FIELD");

        Wallet wallet = new FlatWallet("15.33");
        Assert.assertEquals("AMOUNT=15.33", labelGenerator.labelOf(wallet));
    }

    @Test
    public void expectReplaceStringVariableAtSpecificKeyByMap() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_DEFINITION_AT_SPECIFIC_KEY_BY_MAP");
        Map<String, Object> variable = new HashMap<String, Object>() {{
            put("target", "!@#$");
            put("amount", "99.99");
        }};
        Assert.assertEquals("AMOUNT=99.99", labelGenerator.labelOf(variable));
    }

    @Test
    public void expectReplaceAllBySpecificMethod() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_FIELD");
        Object data = new Object() {
            public Map<String, Object> getKeyValues() {
                return new HashMap<String, Object>() {{
                    put("[NAME]", "Sarah");
                    put("[GREET]", "I'm good");
                }};
            }
        };
        Assert.assertEquals("Hello Sarah! I'm good", labelGenerator.labelOf(data));
    }

    @Test
    public void expectExceptionReplaceAllByNonCollectionMethod() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_NON_COLLECTION_FIELD");
        Object data = new Object() {
            public Map<String, Object> getKeyValues() {
                return new HashMap<String, Object>() {{
                    put("[NAME]", "Sarah");
                    put("[GREET]", "I'm good");
                }};
            }
        };
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            labelGenerator.labelOf(data);
        });
    }

    @Test
    public void expectExceptionIfReplaceAllByInvalidReturnTypeMethod() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_FIELD");
        Object data = new Object() {
            public String getKeyValues() {
                return "";
            }
        };
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            labelGenerator.labelOf(data);
        });
    }

    @Test
    public void expectReplaceAllBySpecificMap() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_FIELD");
        Map<String, Object> replaceValues = new HashMap<String, Object>() {{
            put("[NAME]", "Tony");
            put("[GREET]", "I'm okay");
        }};
        Assert.assertEquals("Hello Tony! I'm okay", labelGenerator.labelOf(Collections.singletonMap("keyValues", replaceValues)));
    }

    @Test
    public void expectSameTextIfReplaceAllByNotMatchSpecificMap() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_FIELD");
        Map<String, Object> replaceValues = new HashMap<String, Object>() {{
            put("[NAME]", "Tony");
            put("[GREET]", "I'm okay");
        }};
        Assert.assertEquals("Hello [NAME]! [GREET]", labelGenerator.labelOf(Collections.singletonMap("something_else", replaceValues)));
    }

    @Test
    public void expectSameTextIfReplaceAllByEmptyMap() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_FIELD");
        Map<String, Object> replaceValues = Collections.emptyMap();
        Assert.assertEquals("Hello [NAME]! [GREET]", labelGenerator.labelOf(Collections.singletonMap("keyValues", replaceValues)));
    }

    @Test
    public void expectReplaceAllBySpecificDefinition() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_DEFINITION");
        Assert.assertEquals("Hello John! Never Better", labelGenerator.labelOf(null));
    }

    @Test
    public void expectExceptionIfReplaceAllByNonExistingDefinition() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_ALL_BY_NON_EXISTING_DEFINITION");
        Assert.assertThrows(NoSuchDefinitionException.class, () -> {
            labelGenerator.labelOf(null);
        });
    }
}
