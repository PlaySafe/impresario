package data;

import java.math.BigDecimal;

public class Order {

    private Product product;
    private Wallet wallet;
    private int age;

    public Order(Product product, Wallet wallet) {
        this.product = product;
        this.wallet = wallet;
    }

    public Order(Product product, Wallet wallet, int age) {
        this(product, wallet);
        this.age = age;
    }

    public BigDecimal getProductPrice() {
        return product.getAmount();
    }

    public BigDecimal getWalletAmount() {
        return wallet.getAmount();
    }

    public int getAge() {
        return age;
    }
}
