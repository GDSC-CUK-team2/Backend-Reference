package gdsc.team2.matna.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Restaurant extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private Float rating = 0f;

    @Embedded
    private Address address;

    // 메뉴 추후 추가

    private String open;

    private String close;

    private Integer view = 0;

    private Integer review = 0;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantFoodType> restaurantFoodTypes = new ArrayList<>();

    // 이미지 추가

    // 연관관계 메서드 ..? 이게 맞나..?
    public void addRestaurantFoodTypes(RestaurantFoodType restaurantFoodType) {
        restaurantFoodTypes.add(restaurantFoodType);
        restaurantFoodType.setRestaurant(this);
    }
}