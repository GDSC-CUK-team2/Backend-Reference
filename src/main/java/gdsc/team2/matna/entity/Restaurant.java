package gdsc.team2.matna.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Restaurant extends BaseTime {

    protected Restaurant(){ // 기본 생성자 사용방지 createRestaurant 메소드 사용할 것
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private Float rating = 0f;

    @Embedded
    private Address address;

    // TODO: 11/2/23 메뉴추가

    private String open;

    private String close;

    private Integer view = 0;

    private Integer review = 0;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantFoodType> restaurantFoodTypes = new ArrayList<>();

    // TODO: 11/2/23 이미지 추가

    // 생성자 메서드
    public static Restaurant createRestaurant(String name, Address address, List<RestaurantFoodType> list){
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setRestaurantFoodTypes(list);
        return restaurant;
    }

    // 연관관계 메서드
    public void addRestaurantFoodTypes(RestaurantFoodType restaurantFoodType) {
        this.restaurantFoodTypes.add(restaurantFoodType);
    }

    // 비즈니스 로직
    /**
     * 조회수 증가
     */
    public void addView() {
        this.view ++;
    }

    /**
     *  리뷰 개수 증가
     */
    public void addReview() {
        this.review ++;
    }

}