package gdsc.team2.matna.repository;

import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Long> {
    List<ReviewImageEntity> findAllByReviewId(Long reviewId);


}
