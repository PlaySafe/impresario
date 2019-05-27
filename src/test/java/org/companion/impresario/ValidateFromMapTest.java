package org.companion.impresario;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ValidateFromMapTest {

    private final ValidationRule validationRule;

    public ValidateFromMapTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/validate_from_map.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        ValidationRuleFactory validatorFactory = new SingleValidationRuleFactory(metaData);
        Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
        Assert.assertNotNull(validators);

        validationRule = validators.get("POSTAL_CODE_LENGTH");
    }

    @Test
    public void canValidateValueFromMap() throws ConditionNotMatchException {
        Map<String, Object> data = new HashMap<String, Object>() {{
            put("postalCode", "12345");
        }};

        Assert.assertTrue(validationRule.validate(data));
    }

    @Test
    public void failIfMapHasNoDataButExpectData() throws ConditionNotMatchException {
        Assert.assertFalse(validationRule.validate(new HashMap<String, Object>()));
    }
}
