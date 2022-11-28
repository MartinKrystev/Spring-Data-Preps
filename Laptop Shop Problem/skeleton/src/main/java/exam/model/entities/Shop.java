package exam.model.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "shops")
public class Shop extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal income;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, name = "employee_count")
    private Integer employeeCount;

    @Column(nullable = false, name = "shop_area")
    private Integer shopArea;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Town town;

    @OneToMany(targetEntity = Laptop.class, mappedBy = "shop", cascade = CascadeType.ALL)
    private Set<Laptop> laptops;

    public Shop() {
    }

    public Shop(String name, BigDecimal income, String address, Integer employeeCount, Integer shopArea, Town town) {
        this.name = name;
        this.income = income;
        this.address = address;
        this.employeeCount = employeeCount;
        this.shopArea = shopArea;
        this.town = town;
        this.laptops = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Integer getShopArea() {
        return shopArea;
    }

    public void setShopArea(Integer shopArea) {
        this.shopArea = shopArea;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Set<Laptop> getLaptops() {
        return laptops;
    }

    public void setLaptops(Set<Laptop> laptops) {
        this.laptops = laptops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return Objects.equals(name, shop.name) &&
                Objects.equals(income, shop.income) &&
                Objects.equals(address, shop.address) &&
                Objects.equals(employeeCount, shop.employeeCount) &&
                Objects.equals(shopArea, shop.shopArea) &&
                Objects.equals(town, shop.town) &&
                Objects.equals(laptops, shop.laptops);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, income, address, employeeCount, shopArea, town, laptops);
    }
}
