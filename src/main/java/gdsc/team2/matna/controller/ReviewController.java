package gdsc.team2.matna.controller;

import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.dto.ReviewResponseDTO;
import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.service.ReviewService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;


@RestController
@RequestMapping("Matna/restaurants")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    //댓글 작성
    @PostMapping("/{restaurant_id}/reviews")
    public ResponseEntity saveReview(
            @PathVariable Long restaurant_id,
            @RequestParam("userId") Long userId,
            @RequestParam("rating") String rating,
            @RequestParam("comment") String comment,
            @RequestParam("image") List<MultipartFile> images){

        ReviewDTO reviewDTO = new ReviewDTO( userId,restaurant_id,rating,comment);
        reviewDTO.setRestaurantId(restaurant_id);
        reviewDTO.setCreatedDate(LocalDateTime.now());

        ResponseEntity response = reviewService.reviewSave(reviewDTO,images);
        return response;

    }

    //댓글조회
    @GetMapping("/{restaurant_id}/reviews")
    public List<ReviewResponseDTO> getReview(@PathVariable Long restaurant_id){
        return reviewService.getReviews(restaurant_id);
    }

    //댓글 삭제
    @DeleteMapping("/{restaurant_id}/reviews/{review_id}")
    public void deleteReview(@PathVariable Long restaurant_id,@PathVariable Long review_id) {
        reviewService.deleteReview(restaurant_id, review_id);
    }
    //뎃글수정
    @PatchMapping ("/{restaurant_id}/reviews/{review_id}")
    public ResponseEntity editReview (
            @PathVariable Long restaurant_id,@PathVariable Long review_id,
            @RequestParam("userId") Long userId,
            @RequestParam("rating") String rating,
            @RequestParam("comment") String comment,
            @RequestParam("image") List<MultipartFile> images) {

        ReviewDTO reviewDTO = new ReviewDTO(userId, restaurant_id, rating, comment);
        reviewDTO.setRestaurantId(restaurant_id);
        reviewDTO.setUpdatedDate(LocalDateTime.now());

            ResponseEntity response = reviewService.modifyReview(review_id, reviewDTO, images);
            return response;


    }
}



