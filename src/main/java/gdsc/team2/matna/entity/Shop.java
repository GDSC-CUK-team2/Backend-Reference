package gdsc.team2.matna.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_uid", nullable = false, unique = true)
    private Long shopUid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "foodType", nullable = false)
    private String foodType;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "sum_rating", nullable = false)
    private Integer sumRating = 0;

    @Column(name = "view", nullable = false)
    private Integer view = 0;

    @Column(name = "review", nullable = false)
    private Integer review = 0;

    public static Shop createShop(Long shopUid,
                                  String address,
                                  String name,
                                  String foodType) {
        Shop shop = new Shop();
        shop.setShopUid(shopUid);
        shop.setName(name);
        shop.setAddress(address);
        shop.setFoodType(foodType);
        return shop;
    }

    public void addReview() { this.review ++; }

    public void subReview() { this.review --; }

    public void addRating(Integer point) { this.sumRating += point; };

    public void subRating(Integer point) { this.sumRating -= point; };

    public void addView() { this.view ++; }
}
