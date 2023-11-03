//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gdsc.team2.matna.controller;

import gdsc.team2.matna.entity.Restaurant;
import gdsc.team2.matna.service.RestaurantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/api/restaurants")
    public CreateRestaurantResponse saveRestaurant(@RequestBody @Valid CreateRestaurantRequest request) {
        Long id = restaurantService.add(request.getName(),
                request.getAddress(),
                request.getFood_type());
        return new CreateRestaurantResponse(id);
    }

    @GetMapping("/api/restaurants")
    public Result getListRestaurant() {
        List<Restaurant> findRestaurants = restaurantService.findRestaurants();
        List<RestaurantDto> collect = findRestaurants.stream()
                .map(r -> new RestaurantDto(r.getId(), r.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    // TODO update 기능 dto 학습 후 진행
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
        private Float rating;
        private String address;
        private List<String> food_type;
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
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class RestaurantDto {
        private Long id;
        private String name;
    }
}


