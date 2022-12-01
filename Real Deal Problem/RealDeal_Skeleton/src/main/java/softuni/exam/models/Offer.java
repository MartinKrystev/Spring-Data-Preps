package softuni.exam.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "offers",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"description", "added_on"})})
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "has_gold_status")
    private Boolean hasGoldStatus;

    @Column(nullable = false, name = "added_on")
    private LocalDateTime addedOn;

    @ManyToOne
    private Car car;

    @ManyToOne
    private Seller seller;

    @ManyToMany
    private Set<Picture> pictures;

    public Offer() {
    }

    public Offer(Long id, BigDecimal price, String description, Boolean hasGoldStatus, LocalDateTime addedOn, Car car, Seller seller) {
        this.id = id;
        this.price = price;
        this.description = description;
        this.hasGoldStatus = hasGoldStatus;
        this.addedOn = addedOn;
        this.car = car;
        this.seller = seller;
        this.pictures = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasGoldStatus() {
        return hasGoldStatus;
    }

    public void setHasGoldStatus(Boolean hasGoldStatus) {
        this.hasGoldStatus = hasGoldStatus;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) &&
                Objects.equals(price, offer.price) &&
                Objects.equals(description, offer.description) &&
                Objects.equals(hasGoldStatus, offer.hasGoldStatus) &&
                Objects.equals(addedOn, offer.addedOn) &&
                Objects.equals(car, offer.car) &&
                Objects.equals(seller, offer.seller) &&
                Objects.equals(pictures, offer.pictures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, description, hasGoldStatus, addedOn, car, seller, pictures);
    }
}
