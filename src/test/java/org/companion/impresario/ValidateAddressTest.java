package org.companion.impresario;

import data.Address;
import data.DefaultAddress;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ValidateAddressTest {

    private final ValidationRule validationRule;

    public ValidateAddressTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/validator.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        ValidationRuleFactory validatorFactory = new SingleValidationRuleFactory(metaData);
        Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
        Assert.assertNotNull(validators);

        ValidationRule validationRule = validators.get("POSTAL_CODE_LENGTH");
        Assert.assertNotNull(validationRule);
        this.validationRule = validationRule;
    }

    @Test
    public void caseMissingPostalCode() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setCity("city0")
                .setState("state0")
                .setStreet("street0")
                .setPostalCode("25700")
                .setCountry("country0")
                .build();

        Assert.assertTrue(validationRule.validate(address));
    }

    @Test
    public void case6CharactersPostalCode() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setCity("city0")
                .setState("state0")
                .setStreet("street0")
                .setPostalCode("AB25700")
                .setCountry("country0")
                .build();

        Assert.assertTrue(validationRule.validate(address));
    }
}
