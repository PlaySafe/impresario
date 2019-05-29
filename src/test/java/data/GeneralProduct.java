package data;

import java.math.BigDecimal;

public class GeneralProduct implements Product {

    private final BigDecimal amount;
    private final BigDecimal vat;

    public GeneralProduct(String amount, String vat) {
        this.amount = new BigDecimal(amount);
        this.vat = new BigDecimal(vat);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public BigDecimal getVatRate() {
        return vat;
    }

}
