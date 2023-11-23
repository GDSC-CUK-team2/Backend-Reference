package gdsc.team2.matna.repository;

import gdsc.team2.matna.entity.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    public Optional<Shop> findByShopUid(Long shopUid);
}
