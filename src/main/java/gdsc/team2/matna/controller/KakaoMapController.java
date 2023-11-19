package gdsc.team2.matna.controller;

import gdsc.team2.matna.entity.Address;
import gdsc.team2.matna.entity.Restaurant;
import gdsc.team2.matna.service.KakaoMapService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class KakaoMapController {
    private static final String KeywordEndPoint = "/v2/local/search/keyword.json";

    private final KakaoMapService kakaoMapService;
    @GetMapping("/search")
    public Map<?, ?> search(@RequestParam(required = false) String page,
                                         @RequestParam(required = false) String x,
                                         @RequestParam(required = false) String y,
                                         @RequestParam(required = false) String radius,
                                         @RequestParam String keyword) {
        String query = keyword;

        if (query == null || query.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "검색어를 입력하세요");
        }
        try {
            Map<?, ?> map = kakaoMapService.get(query, page, x, y, radius);
            return map;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "검색 실패");
        }
    }

}
