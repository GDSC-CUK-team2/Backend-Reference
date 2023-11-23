package gdsc.team2.matna.dto;

import lombok.Data;

import java.awt.print.Pageable;
import java.util.List;

@Data
public class RestaurantSearchDto {
    private Integer page;
    private String keyword;         // 검색어
    private List<String> foodTypes; // 카테고리

    public RestaurantSearchDto(Integer page, String keyword, List<String> foodTypes) {
        this.page = page;
        this.keyword = keyword;
        this.foodTypes = foodTypes;
    }
}
