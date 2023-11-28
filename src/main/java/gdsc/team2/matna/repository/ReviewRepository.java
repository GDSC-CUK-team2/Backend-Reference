package gdsc.team2.matna.repository;

import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {


    List<ReviewEntity> findAllByShopId(Long shopId);
}

