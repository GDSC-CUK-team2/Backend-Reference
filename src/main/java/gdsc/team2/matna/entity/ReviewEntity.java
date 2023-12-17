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

    @Column
    private Long shopId;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column
    private String comment;



    @CreatedDate
    @Column(updatable = false,nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime updatedDate;

    public void ReviewEntity(ReviewDTO reviewDTO,String imgUrl){
        this.reviewId =reviewDTO.getReviewId();
        this.shopId = reviewDTO.getShopId();
        this.userId = reviewDTO.getUserId();
        this.rating = reviewDTO.getRating();
        this.comment=reviewDTO.getComment();
        this.createdDate = reviewDTO.getCreatedDate();
        this.updatedDate = reviewDTO.getUpdatedDate();

    }

    public static ReviewEntity registerComment(ReviewDTO reviewDTO){
        return ReviewEntity.builder()
                .reviewId(reviewDTO.getReviewId())
                .shopId(reviewDTO.getShopId())
                .userId(reviewDTO.getUserId())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdDate(reviewDTO.getCreatedDate())
                .build();
    }

    public static ReviewEntity toEnitty(ReviewDTO reviewDTO){
        return ReviewEntity.builder()
                .reviewId(reviewDTO.getReviewId())
                .shopId(reviewDTO.getShopId())
                .userId(reviewDTO.getUserId())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdDate(reviewDTO.getCreatedDate())
                .updatedDate(reviewDTO.getUpdatedDate())
                .build();

    }



}
