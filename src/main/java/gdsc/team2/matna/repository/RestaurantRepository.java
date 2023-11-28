package gdsc.team2.matna.repository;

import org.springframework.stereotype.Repository;

import gdsc.team2.matna.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity,Long> {


    @Override
    boolean existsById(Long restaurantId);

}
