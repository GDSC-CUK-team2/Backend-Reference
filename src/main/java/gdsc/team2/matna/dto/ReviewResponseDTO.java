package gdsc.team2.matna.dto;

import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.entity.ReviewImageEntity;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    //댓글 조회시 사용하는 DTO
    private ReviewEntity reviewEntity;
    private  List<String> fileNames;

}
