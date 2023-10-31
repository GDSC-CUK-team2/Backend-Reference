package gdsc.team2.matna.entity;

import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.etc.Rating;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.Lock;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Review")
public class ReviewEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "comment")
    private String comment;



//    @Column(name = "created_date")
//    private LocalDateTime createdDate;
//
//    @Column(name = "updated_date")
//    private LocalDateTime updatedDate;

    @CreatedDate
    @Column(name = "created_date", updatable = false,nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime updatedDate;

    public void ReviewEntity(ReviewDTO reviewDTO,String imgUrl){
        this.reviewId =reviewDTO.getReviewId();
        this.restaurantId = reviewDTO.getRestaurantId();
        this.userId = reviewDTO.getUserId();
        this.rating = reviewDTO.getRating();
        this.comment=reviewDTO.getComment();
        this.createdDate = reviewDTO.getCreatedDate();
        this.updatedDate = reviewDTO.getUpdatedDate();

    }

    public static ReviewEntity registerComment(ReviewDTO reviewDTO){
        return ReviewEntity.builder()
                .reviewId(reviewDTO.getReviewId())
                .restaurantId(reviewDTO.getRestaurantId())
                .userId(reviewDTO.getUserId())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdDate(reviewDTO.getCreatedDate())
                .build();
    }

    public static ReviewEntity toEnitty(ReviewDTO reviewDTO){
        return ReviewEntity.builder()
                .reviewId(reviewDTO.getReviewId())
                .restaurantId(reviewDTO.getRestaurantId())
                .userId(reviewDTO.getUserId())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdDate(reviewDTO.getCreatedDate())
                .updatedDate(reviewDTO.getUpdatedDate())
                .build();

    }



}
