package gdsc.team2.matna.repository;

import gdsc.team2.matna.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity,Long> {


    @Override
    boolean existsById(Long restaurantId);
}
