//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gdsc.team2.matna.controller;

import gdsc.team2.matna.entity.Address;
import gdsc.team2.matna.entity.Restaurant;
import gdsc.team2.matna.dto.RestaurantSearchDto;
import gdsc.team2.matna.service.RestaurantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("")
    public CreateRestaurantResponse saveRestaurant(@RequestBody @Valid CreateRestaurantRequest request) {
        Long id = restaurantService.add(request.getName(),
                request.getAddress(),
                request.getFood_type());
        return new CreateRestaurantResponse(id);
    }

    @GetMapping("")
    public Result getListRestaurant(
            @RequestParam("page") Integer page,
            @RequestParam("keyword") String keyword,
            @RequestParam("foodTypes") List<String> foodTypes) {

        RestaurantSearchDto restaurantSearchDto = new RestaurantSearchDto(page, keyword, foodTypes);

        List<Restaurant> findRestaurants = restaurantService.findRestaurants(restaurantSearchDto);
        List<RestaurantDto> collect = findRestaurants.stream()
                .map(r -> new RestaurantDto(r))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    // TODO update 기능
//    @PutMapping("/api/restaurants/{id}")
//    public UpdateRestaurantResponse updateRestaurant(
//            @PathVariable("id") Long id,
//            @RequestBody @Valid UpdateRestaurantRequest request) {
//
//    }



    @Data
    static class CreateRestaurantRequest {
        @NotEmpty(message = "식당 이름은 필수항목 입니다")
        private String name;

        @NotEmpty(message = "식당 주소는 필수항목 입니다")
        private String address;

        private List<String> food_type;
    }

    @Data
    static class CreateRestaurantResponse {
        private Long id;

        public CreateRestaurantResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    static class UpdateRestaurantRequest {
        private String name;
        private String open;
        private String close;
    }

    @Data
    static class UpdateRestaurantResponse {
        private Long id;

        public UpdateRestaurantResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class GetListRestaurantRequest  {
        private String keyword;
        private Integer page;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T results;
    }

    @Data
    @AllArgsConstructor
    static class RestaurantDto {
        private Long id;
        private String name;
        private Float rating;
        private Address address;
        private List<String> food_type;
        private Integer view;
        private Integer review;
        private LocalDateTime created_data;
        private LocalDateTime modifed_date;

        public RestaurantDto(Restaurant restaurant){
            id = restaurant.getId();
            name = restaurant.getName();
            rating = restaurant.getRating();
            address = restaurant.getAddress();
            food_type = restaurant.getRestaurantFoodTypes().stream()
                    .map(restaurantFoodType -> restaurantFoodType.getFoodType().getName())
                    .collect(Collectors.toList());
            view = restaurant.getView();
            review = restaurant.getReview();
            created_data = restaurant.getCreatedDate();
            modifed_date = restaurant.getModifiedDate();
        }
    }

}


