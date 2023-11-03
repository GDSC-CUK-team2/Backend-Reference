package gdsc.team2.matna.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class FoodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "foodType", cascade = CascadeType.ALL)
    private List<RestaurantFoodType> restaurantFoodTypes = new ArrayList<>();

    // 생성자 메소드
    public static FoodType createFoodType (String name) {
        FoodType foodType = new FoodType();
        foodType.setName(name);
        return foodType;
    }
}
