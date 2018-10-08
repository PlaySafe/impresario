package org.companion.impresario;

import data.FlatWallet;
import data.GeneralProduct;
import data.Order;
import java.io.File;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class BuyingProduct {

    private Map<String, LabelGenerator> labelGenerators;

    public BuyingProduct() {
        File metaResource = new File("src/test/resources/meta_label.xml");
        File configResource = new File("src/test/resources/order_product.xml");
        MetaData metaData = new MetaLabelFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test(expected = ConditionNotMatchException.class)
    public void expectExceptionIfNotEnoughMoney() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("20.50"), new FlatWallet("20.49"));
        LabelGenerator labelGenerator = labelGenerators.get("ORDER_PRODUCT");
        Assert.assertNotNull(labelGenerator);

        labelGenerator.labelOf(order);
    }

    @Test
    public void expectMessageIfEnoughMoney() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("20.50"), new FlatWallet("20.51"));
        LabelGenerator labelGenerator = labelGenerators.get("ORDER_PRODUCT");
        Assert.assertNotNull(labelGenerator);

        String message = labelGenerator.labelOf(order);
        Assert.assertNotNull(message);
    }

    @Test(expected = ConditionNotMatchException.class)
    public void expectExceptionIfAgeUnderMinimum() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("20.50"), new FlatWallet("20.51"), 17);
        LabelGenerator labelGenerator = labelGenerators.get("OVER_17_PRODUCT_ONLY");
        Assert.assertNotNull(labelGenerator);

        labelGenerator.labelOf(order);
    }

    @Test
    public void expectMessageIfAgeNotUnderMinimum() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("10.50"), new FlatWallet("15.00"), 18);
        LabelGenerator labelGenerator = labelGenerators.get("OVER_17_PRODUCT_ONLY");
        Assert.assertNotNull(labelGenerator);

        String message = labelGenerator.labelOf(order);
        Assert.assertNotNull(message);
    }

    @Test
    public void expectHalfPriceForElder() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("40.50"), new FlatWallet("25.00"), 61);
        LabelGenerator labelGenerator = labelGenerators.get("HALF_PRICE_FOR_CHILDREN_OR_ELDER");
        Assert.assertNotNull(labelGenerator);

        String message = labelGenerator.labelOf(order);
        Assert.assertNotNull("HALF_PRICE", message);
    }

    @Test
    public void expectHalfPriceForChildren() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("99.00"), new FlatWallet("50.00"), 11);
        LabelGenerator labelGenerator = labelGenerators.get("HALF_PRICE_FOR_CHILDREN_OR_ELDER");
        Assert.assertNotNull(labelGenerator);

        String message = labelGenerator.labelOf(order);
        Assert.assertEquals("HALF_PRICE", message);
    }

    @Test
    public void expectFullPriceForNeitherChildrenNorElder() throws ConditionNotMatchException {
        Order order = new Order(new GeneralProduct("20.50"), new FlatWallet("20.51"), 18);
        LabelGenerator labelGenerator = labelGenerators.get("HALF_PRICE_FOR_CHILDREN_OR_ELDER");
        Assert.assertNotNull(labelGenerator);

        String message = labelGenerator.labelOf(order);
        Assert.assertEquals("FULL_PRICE", message);
    }
}
