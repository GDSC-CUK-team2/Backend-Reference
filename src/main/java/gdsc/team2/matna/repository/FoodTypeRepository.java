package gdsc.team2.matna.repository;


import gdsc.team2.matna.entity.FoodType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodTypeRepository {

    private final EntityManager em;

    public List<FoodType> findByName(String name) {
        return em.createQuery("select f from FoodType f where  f.name = :name", FoodType.class)
                .setParameter("name", name)
                .getResultList();
    }

}
