package org.companion.impresario;

import data.Address;
import data.DefaultAddress;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ComplexConfigurationTest {

    private final LabelGeneratorFactory labelGeneratorFactory;

    public ComplexConfigurationTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        this.labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
    }

    @Test
    public void expectFullBelgiumAddressFormat() throws IOException, ConditionNotMatchException {
        File configResource = new File("src/test/resources/complex/belgium_address_format.xml");
        LabelGenerator labelGenerator = labelGeneratorFactory.compile(configResource).get("BE");
        Assert.assertNotNull(labelGenerator);

        Address address = new DefaultAddress.Builder()
                .setStreet("Zone industrielle Centre Résidence Avenue Boulevard")
                .setCity("Brussels")
                .setCountry("Belgium")
                .build();
        String expect = new StringBuilder()
                .append("Z.I. Ctre Rés. Av. Bd").append("\n")
                .append("Brussels").append("\n")
                .append("BELGIUM")
                .toString();
        Assert.assertEquals(expect, labelGenerator.labelOf(address));
    }

    @Test
    public void expectPartialBelgiumAddressFormat() throws IOException, ConditionNotMatchException {
        File configResource = new File("src/test/resources/complex/belgium_address_format.xml");
        LabelGenerator labelGenerator = labelGeneratorFactory.compile(configResource).get("BE");
        Assert.assertNotNull(labelGenerator);
        Address address = new DefaultAddress.Builder()
                .setStreet("Zone industrielle Centre Chaussée Route Boulevard")
                .setPostalCode("7010")
                .setCity("Brussels")
                .setCountry("Belgium")
                .build();
        String expect = new StringBuilder()
                .append("Zone industrielle Centre Chaussée Route Boulevard").append("\n")
                .append("7010").append(" ").append("Brussels").append("\n")
                .append("BELGIUM")
                .toString();
        Assert.assertEquals(expect, labelGenerator.labelOf(address));
    }
}
