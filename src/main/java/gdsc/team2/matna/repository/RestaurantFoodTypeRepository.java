package gdsc.team2.matna.repository;

import gdsc.team2.matna.entity.Restaurant;
import gdsc.team2.matna.entity.RestaurantFoodType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantFoodTypeRepository {

    private final EntityManager em;

    public void save(RestaurantFoodType restaurantFoodType) {
        em.persist(restaurantFoodType);
    }
}
