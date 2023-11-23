package gdsc.team2.matna.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
public class Address {

    private String city1;
    private String city2;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city1, String city2, String street, String zipcode) {
        this.city1 = city1;
        this.city2 = city2;
        this.street = street;
        this.zipcode = zipcode;
    }

}
