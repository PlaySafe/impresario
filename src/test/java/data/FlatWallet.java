package data;

import java.math.BigDecimal;

public class FlatWallet implements Wallet {

    private final BigDecimal money;

    public FlatWallet(String money) {
        this.money = new BigDecimal(money);
    }

    @Override
    public BigDecimal getAmount() {
        return money;
    }
}
