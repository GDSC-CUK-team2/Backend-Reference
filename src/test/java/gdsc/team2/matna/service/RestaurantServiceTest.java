package gdsc.team2.matna.service;

import gdsc.team2.matna.repository.RestaurantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트에서는 기본 롤백 지원
public class RestaurantServiceTest {

    @Autowired RestaurantService restaurantService;
    @Autowired RestaurantRepository restaurantRepository;

    @Test
    public void 음식점_생성() throws  Exception {
        //given
        String name = "맛집맞나?";
        String stringAddress = "서울특별시 강남구 압구정동 489";
        List<String> foodTypes = Arrays.asList("한식", "양식");

        //when
        Long savedId = restaurantService.add(name, stringAddress, foodTypes);

        //then
        assertEquals(name, restaurantRepository.findOne(savedId).getName(), "이름이 같은지 확인");
        assertEquals(2,
                     restaurantRepository.findOne(savedId).getRestaurantFoodTypes().size(),
                     "다대다 매핑 개수 확인");
    }

    // TODO: 11/3/23 음식점 카테고리 형식 예외 처리

    // TODO: 11/3/23 음식점 주소 형식 예외 처리

}