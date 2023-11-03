package gdsc.team2.matna.repository;

import gdsc.team2.matna.entity.Restaurant;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    public List<Restaurant> findAll() {
        return em.createQuery("select r from Restaurant r", Restaurant.class)
                .getResultList();
    }

    public List<Restaurant> findByName(String name) {
        return em.createQuery("select r from Restaurant r where r.name = :name", Restaurant.class)
                .setParameter("name", name)
                .getResultList();
    }

    // TODO: 11/3/23 (이름 or 지역) and 카테고리로 검색되는 기능
//    public List<Restaurant> findAll(RestaurantSearch restaurantSearch) {
//    }

}
