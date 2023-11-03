package gdsc.team2.matna.service;

import gdsc.team2.matna.entity.Address;
import gdsc.team2.matna.entity.FoodType;
import gdsc.team2.matna.entity.Restaurant;
import gdsc.team2.matna.entity.RestaurantFoodType;
import gdsc.team2.matna.repository.FoodTypeRepository;
import gdsc.team2.matna.repository.RestaurantRepository;
import gdsc.team2.matna.repository.RestaurantSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // 의존성 주입
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final FoodTypeRepository foodTypeRepository;

    /**
     * 식당 등록
     */
    @Transactional // 쓰기는 readOnly = false
    public Long add(String name, String stringAddress, List<String> foodTypeNames){

        // string address -> Address Object
        String[] splitAddress = stringAddress.split(" ");
        Address address = new Address(splitAddress[0], splitAddress[1], splitAddress[2], splitAddress[3]);

        // foodTypeNames -> RestaurantFoodType list
        List<FoodType> foodTypeList = new ArrayList<>();
        List<RestaurantFoodType> restaurantFoodlist = new ArrayList<>();
        for (String foodTypeName : foodTypeNames) { // 없는 카테고리명인 가능성 있음 오류
            FoodType foodType = foodTypeRepository.findByName(foodTypeName).get(0);
            foodTypeList.add(foodType);
        }
        for (FoodType foodType : foodTypeList) {
            RestaurantFoodType restaurantFoodType = RestaurantFoodType.createRestaurantFoodType(foodType);
            restaurantFoodlist.add(restaurantFoodType);
        }

        // 식당 생성
        Restaurant restaurant = Restaurant.createRestaurant(name, address, restaurantFoodlist);

        // 식당 저장, 다대다 테이블의 경우 cascade ALL에 의해 수정 불필요
        restaurantRepository.save(restaurant);

        return restaurant.getId();

    }

    /**
     * 식당 조회
     */
    public List<Restaurant> findRestaurants() {
        return restaurantRepository.findAll();
    }

    /**
     * 식당 상세조회
     */
    public Restaurant findOne(Long restaurantId) {
        return restaurantRepository.findOne(restaurantId);
    }

    /**
     * 식당 검색
     */

    /**
     * 식당 업데이트
     */


}
