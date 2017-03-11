package transferamount;

import java.math.BigDecimal;

public class Account {
    /**
     * Account ID.
     */
    private Integer id;

    private BigDecimal money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
                ", money=" + getMoney() +
                '}';
    }
}
