package gdsc.team2.matna.service;

import gdsc.team2.matna.entity.Shop;
import gdsc.team2.matna.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop getShop(Long shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            return shop.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "404: Not found");
        }
    }

    public Shop findByUid(Long shopUid) {
        Optional<Shop> shop = shopRepository.findByShopUid(shopUid);
        if (shop.isPresent()) {
            return shop.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "404: Not found");
        }
    }
}
