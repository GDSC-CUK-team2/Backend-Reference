package gdsc.team2.matna.controller;

import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.dto.ReviewResponseDTO;
import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.entity.Shop;
import gdsc.team2.matna.etc.Rating;
import gdsc.team2.matna.repository.ShopRepository;
import gdsc.team2.matna.service.ReviewService;
import gdsc.team2.matna.service.ShopService;
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
    @PostMapping("/{shopId}/reviews")
    public ResponseEntity saveReview(
            @PathVariable Long shopId,
            @RequestParam("userId") Long userId,
            @RequestParam("rating") Rating rating,
            @RequestParam("comment") String comment,
            @RequestParam("image") List<MultipartFile> images){

        ReviewDTO reviewDTO = new ReviewDTO( userId,shopId,rating,comment);
        reviewDTO.setShopId(shopId);
        reviewDTO.setCreatedDate(LocalDateTime.now());

        ResponseEntity response = reviewService.reviewSave(reviewDTO,images);
        return response;

    }

    //댓글조회
    @GetMapping("/{shopId}/reviews")
    public  ResponseEntity<List<ReviewResponseDTO>> getReview(@PathVariable Long shopId){
        return reviewService.getReviews(shopId);
    }

    //댓글 삭제
    @DeleteMapping("/{shopId}/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long shopId,@PathVariable Long reviewId) {
        reviewService.deleteReview(shopId, reviewId);
    }
    //댓글수정
    @PatchMapping ("/{shopId}/reviews/{reviewId}")
    public ResponseEntity editReview (
            @PathVariable Long shopId,@PathVariable Long reviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("rating") Rating rating,
            @RequestParam("comment") String comment,
            @RequestParam("image") List<MultipartFile> images) {

        ReviewDTO reviewDTO = new ReviewDTO(userId, shopId, rating, comment);
        reviewDTO.setReviewId(reviewId);
        reviewDTO.setShopId(shopId);
        reviewDTO.setUpdatedDate(LocalDateTime.now());

            ResponseEntity response = reviewService.modifyReview(reviewId, reviewDTO, images);
            return response;


    }
}



