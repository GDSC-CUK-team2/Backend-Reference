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

    @Column(name = "shop_rating", nullable = false)
    private Float rating = 0f;

    @Column(name = "shop_view", nullable = false)
    private Integer view = 0;

    @Column(name = "review", nullable = false)
    private Integer review = 0;

    public static Shop createShop(Long shopUid) {
        Shop shop = new Shop();
        shop.setShopUid(shopUid);
        return shop;
    }

    public void addReview() { this.view ++; }

    public void addView() { this.view ++; }
}
