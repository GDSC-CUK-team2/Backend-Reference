package gdsc.team2.matna.controller;

import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.dto.ReviewResponseDTO;
import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.etc.Rating;
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
@RequestMapping("api/restaurants")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    //댓글 작성
    @PostMapping("/{restaurantId}/reviews")
    public ResponseEntity saveReview(
            @PathVariable Long restaurantId,
            @RequestParam("userId") Long userId,
            @RequestParam("rating") Rating rating,
            @RequestParam("comment") String comment,
            @RequestParam("image") List<MultipartFile> images){

        ReviewDTO reviewDTO = new ReviewDTO( userId,restaurantId,rating,comment);
        reviewDTO.setRestaurantId(restaurantId);
        reviewDTO.setCreatedDate(LocalDateTime.now());

        ResponseEntity response = reviewService.reviewSave(reviewDTO,images);
        return response;

    }

    //댓글조회
    @GetMapping("/{restaurantId}/reviews")
    public List<ReviewResponseDTO> getReview(@PathVariable Long restaurantId){
        return reviewService.getReviews(restaurantId);
    }

    //댓글 삭제
    @DeleteMapping("/{restaurantId}/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long restaurantId,@PathVariable Long reviewId) {
        reviewService.deleteReview(restaurantId, reviewId);
    }
    //뎃글수정
    @PatchMapping ("/{restaurantId}/reviews/{reviewId}")
    public ResponseEntity editReview (
            @PathVariable Long restaurantId,@PathVariable Long reviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("rating") Rating rating,
            @RequestParam("comment") String comment,
            @RequestParam("image") List<MultipartFile> images) {

        ReviewDTO reviewDTO = new ReviewDTO(userId, restaurantId, rating, comment);
        reviewDTO.setReviewId(reviewId);
        reviewDTO.setRestaurantId(restaurantId);
        reviewDTO.setUpdatedDate(LocalDateTime.now());

            ResponseEntity response = reviewService.modifyReview(reviewId, reviewDTO, images);
            return response;


    }
}



