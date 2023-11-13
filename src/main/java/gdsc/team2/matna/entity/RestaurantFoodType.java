package gdsc.team2.matna.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class RestaurantFoodType {

    @Id
    @GeneratedValue
    @Column(name = "reastaurant_type_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_id")
    private FoodType foodType;


    // 생성자 메소드
    public static RestaurantFoodType createRestaurantFoodType(Restaurant restaurant, FoodType foodType) {
        RestaurantFoodType restaurantFoodType = new RestaurantFoodType();
        restaurantFoodType.setFoodType(foodType);
        restaurantFoodType.setRestaurant(restaurant);
        // 연관 관계
        restaurant.addRestaurantFoodTypes(restaurantFoodType);
        foodType.addRestaurantFoodTypes(restaurantFoodType);

        return restaurantFoodType;
    }

}
