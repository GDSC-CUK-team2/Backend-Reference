package gdsc.team2.matna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gdsc.team2.matna.dto.RestaurantSearchDto;
import gdsc.team2.matna.entity.QFoodType;
import gdsc.team2.matna.entity.QRestaurant;
import gdsc.team2.matna.entity.QRestaurantFoodType;
import gdsc.team2.matna.entity.Restaurant;
import jakarta.persistence.EntityManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.naming.directory.SearchResult;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class RestaurantRepository {

    private final EntityManager em;

    public void save(Restaurant restaurant) {
        if (restaurant.getId() == null) { // 신규 등록
            em.persist(restaurant);
        } else { // 업데이트
            em.merge(restaurant);
        }
    }

    public Restaurant findOne(Long id) {
        return em.find(Restaurant.class, id);
    }

    // TODO: 11/3/23 (이름 or 지역) and 카테고리로 검색되는 기능
    public List<Restaurant> findAll(RestaurantSearchDto restaurantSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QRestaurant restaurant = QRestaurant.restaurant;
        QRestaurantFoodType restaurantFoodType = QRestaurantFoodType.restaurantFoodType;
        QFoodType foodType = QFoodType.foodType;

        Long count = query.from(restaurant).fetchCount();

        return query.select(restaurant)
                .from(restaurant)
                .where()
                .offset(restaurantSearch.getPage())
                .fetch();
    }

    public List<Restaurant> findByName(String name) {
        return em.createQuery("select r from Restaurant r where r.name = :name", Restaurant.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Restaurant> findAllFetchJoin(RestaurantSearchDto restaurantSearch) {
        return em.createQuery(
                "select r from Restaurant r",Restaurant.class
        ).getResultList();
    }

    @Data
    class PageLimit{
        private Integer offset;
        private Integer limit;
    }

}
