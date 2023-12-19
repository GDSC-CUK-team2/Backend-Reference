package gdsc.team2.matna.service;

import gdsc.team2.matna.entity.Shop;
import gdsc.team2.matna.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class KakaoMapService {
    @Value("${kakao.api-url}")
    private String kakaoApiUrl;


    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    private final ShopRepository shopRepository;
    private final WebClient webClient;

    public KakaoMapService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
        this.webClient = WebClient.create();
    }

    public Map<?, ?> get(String query, String page, String x, String y, String radius) {
        // api request
        String url = kakaoApiUrl + "/v2/local/search/keyword.json?";
        url += "category_group_code=FD6&"; // 음식점 필터
        if (page != null && !page.isEmpty()) {
            url += "page=" + page + "&";
        }
        if (x != null && !x.isEmpty()) {
            url += "x=" + x + "&";
        }
        if (y != null && !y.isEmpty()) {
            url += "y=" + y + "&";
        }
        if (radius != null && !radius.isEmpty()) {
            url += "radius=" + radius + "&";
        }


        Map<?, ?> response =
                webClient
                        .get()
                        .uri(url + "query=" + query)
                        .header("Authorization", "KakaoAK " + kakaoApiKey)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

        List<?> document = Objects.requireNonNull(response).get("documents") != null ? (List<?>) response.get("documents") : null;

        if (document != null) {
            for (Object o : document) {
                Map<String, String> map = (Map<String, String>) o;
                Long shopUid = Long.parseLong(map.get("id"));
                String address = map.get("address_name");
                String name = map.get("place_name");
                String foodType = map.get("category_name");
                String outX = map.get("x");
                String outY = map.get("y");
                if (shopRepository.findByShopUid(shopUid).isPresent()) {
                    continue;
                }
                Shop shop = Shop.createShop(shopUid, address, outX, outY, name, foodType);
                shopRepository.save(shop);
            }
        }

        return response;
    }

}
