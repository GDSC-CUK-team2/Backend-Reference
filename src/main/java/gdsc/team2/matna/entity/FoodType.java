package gdsc.team2.matna.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class FoodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "foodType", cascade = CascadeType.ALL)
    private List<RestaurantFoodType> restaurantFoodTypes = new ArrayList<>();

}
