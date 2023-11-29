package gdsc.team2.matna.service;

import gdsc.team2.matna.entity.Shop;
import gdsc.team2.matna.exception.ResourceNotFoundException;
import gdsc.team2.matna.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop findByUid(Long shopUid) {
        Optional<Shop> shop = shopRepository.findByShopUid(shopUid);
        if (shop.isPresent()) {
            return shop.get();
        } else {
            throw new ResourceNotFoundException("음식점을 찾을 수 없습니다.");
        }
    }
}
