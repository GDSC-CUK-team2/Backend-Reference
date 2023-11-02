package gdsc.team2.matna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Lock;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@IdClass(ReviewImageId.class)
@Table(name="review_image")
public class ReviewImageEntity {

    @Id
    @Column
    private Long reviewId;

    @Id
    @Column
    private Long fileId;

}

@Data
class ReviewImageId implements Serializable {

    private Long reviewId;
    private Long fileId;
}
