package gdsc.team2.matna.dto;


import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.etc.Rating;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO
{
    private Long reviewId;
    private Long shopId;
    private Long userId;
    private Rating rating;
    public String comment;
    public LocalDateTime createdDate;

    public LocalDateTime updatedDate;

    private MultipartFile image;

    public static ReviewDTO toDTO(ReviewEntity reviewEntity){
        return ReviewDTO.builder()
                .reviewId(reviewEntity.getReviewId())
                .shopId(reviewEntity.getShopId())
                .userId(reviewEntity.getUserId())
                .rating(Rating.valueOf(String.valueOf(reviewEntity.getRating())))
                .comment(reviewEntity.getComment())
                .createdDate(reviewEntity.getCreatedDate())
                .updatedDate(reviewEntity.getUpdatedDate())
                .build();

    }

        public ReviewDTO(Long userId,Long shopId, Rating rating, String comment, MultipartFile image){
            this.userId = userId;
            this.rating= rating;
            this.comment= comment;
            this.image= image;
            this.shopId = shopId;
        }

    public ReviewDTO(Long userId,Long shopId, Rating rating, String comment){
        this.userId = userId;
        this.rating= rating;
        this.comment= comment;
        this.shopId = shopId;
    }

    public ReviewDTO(ReviewDTO reviewDTO){
        this.reviewId= reviewDTO.getReviewId();
        this.userId = reviewDTO.getUserId();
        this.rating = reviewDTO.getRating();
        this.comment = reviewDTO.getComment();
        this.createdDate = reviewDTO.getCreatedDate();
        this.updatedDate = reviewDTO.getUpdatedDate();
        this.image = reviewDTO.getImage();
    }




}
