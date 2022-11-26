package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.ApartmentType;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "apartment_type", nullable = false)
    private ApartmentType apartmentType;

    @Column(nullable = false)
    private Double area;

    @ManyToOne
    private Town town;

    @OneToMany(targetEntity = Offer.class, mappedBy = "apartment")
    private Set<Offer> offers;

    public Apartment() {
    }

    public Apartment(ApartmentType apartmentType, Double area, Town town, Set<Offer> offers) {
        this.apartmentType = apartmentType;
        this.area = area;
        this.town = town;
        this.offers = offers;
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return apartmentType == apartment.apartmentType && Objects.equals(area, apartment.area) && Objects.equals(town, apartment.town) && Objects.equals(offers, apartment.offers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartmentType, area, town, offers);
    }
}
