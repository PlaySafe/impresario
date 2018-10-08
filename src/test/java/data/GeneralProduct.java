package data;

import java.math.BigDecimal;

public class GeneralProduct implements Product {

    private BigDecimal amount;

    public GeneralProduct(String amount) {
        this.amount = new BigDecimal(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
