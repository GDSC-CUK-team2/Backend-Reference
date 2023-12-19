package gdsc.team2.matna.controller;

import gdsc.team2.matna.entity.Shop;
import gdsc.team2.matna.exception.ResourceNotFoundException;
import gdsc.team2.matna.exception.ShopSearchFailException;
import gdsc.team2.matna.repository.ShopRepository;
import gdsc.team2.matna.service.ShopService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import gdsc.team2.matna.service.KakaoMapService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class ShopController {

    private final KakaoMapService kakaoMapService;
    private final ShopService shopService;
    private final ShopRepository shopRepository;

    @GetMapping("")
    public GetListResponse search(@RequestParam(required = false) String page,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String x,
                                   @RequestParam(required = false) String y,
                                   @RequestParam(required = false) String radius) {
        if (keyword == null || keyword.isEmpty()) {
            throw new ResourceNotFoundException("keyword는 필수 입력 필드입니다.");
        }
        try {
            Map<?, ?> kakaoMapData = kakaoMapService.get(keyword, page, x, y, radius);
            List<?> document = Objects.requireNonNull(kakaoMapData).get("documents") != null ? (List<?>) kakaoMapData.get("documents") : null;
            Map<String, ?> meta = Objects.requireNonNull(kakaoMapData).get("meta") != null ? (Map<String, ?>) kakaoMapData.get("meta") : null;
            Integer page_count = (Integer) meta.get("pageable_count");
            Integer count = (Integer) meta.get("total_count");

            List<KakaoShop> results = new ArrayList<>();
            if (document != null) {
                for (Object o : document) {
                    Map<String, String> map = (Map<String, String>) o;
                    Long uid = Long.parseLong(map.get("id"));
                    Shop shop = shopService.findByUid(uid);
                    KakaoShop kakaoShop = new KakaoShop(map, shop);
                    results.add(kakaoShop);
                }
            }
            return new GetListResponse(page_count, count, results);

        } catch (Exception e) {
            throw new ShopSearchFailException("검색에 실패했습니다.");
        }
    }

    @GetMapping("/{shopId}")
    public KakaoShop searchOne(@PathVariable Long shopId){
        try {
            Shop shop = shopService.findByUid(shopId);
            shop.addView();
            shopRepository.save(shop);
            return new KakaoShop(shop);
        } catch (Exception e) {
            throw new ShopSearchFailException("검색에 실패했습니다.");
        }
    }

    @Data
    @AllArgsConstructor
    static class GetListResponse {
        private Integer page_count;
        private Integer count;
        private List<KakaoShop> results;
    }

    @Data
    static class KakaoShop {
        private Long id;
        private String name;
        private String rating;
        private String address;
        private String x;
        private String y;
        private String food_type;
        private Integer view;
        private Integer review;

        public KakaoShop(Map<String, String> map, Shop shop) {
            this.id = shop.getShopUid();
            this.name = map.get("place_name");
            if (shop.getReview() == 0) {
                this.rating = "0.0";
            } else {
                this.rating = String.format("%.1f",(float) shop.getSumRating() / (float) shop.getReview());
            }
            this.x = map.get("x");
            this.y = map.get("y");
            this.address = map.get("address_name");
            this.food_type = map.get("category_name");
            this.view = shop.getView();
            this.review = shop.getReview();
        }
        public KakaoShop(Shop shop) {
            this.id = shop.getShopUid();
            this.name = shop.getName();
            if (shop.getReview() == 0) {
                this.rating = "0.0";
            } else {
                this.rating = String.format("%.1f",(float) shop.getSumRating() / (float) shop.getReview());
            }
            this.address = shop.getAddress();
            this.x = shop.getX();
            this.y = shop.getY();
            this.food_type = shop.getFoodType();
            this.view = shop.getView();
            this.review = shop.getReview();
        }
    }
}
