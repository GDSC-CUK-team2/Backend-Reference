package gdsc.team2.matna.service;


import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.dto.ReviewResponseDTO;
import gdsc.team2.matna.entity.FileEntity;
import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.entity.ReviewImageEntity;
import gdsc.team2.matna.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ReviewService extends  FileSevice{

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    UserRepository userRepository;

@Autowired
    FileRepository fileRepository;

    @Autowired
    ReviewImageRepository reviewImageRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String matnabucket;

    Storage storage = StorageOptions.getDefaultInstance().getService();



    public ResponseEntity reviewSave(ReviewDTO reviewDTO, List<MultipartFile> images) {
        //restaurant ID 에 대한 처리 필요

        try {
            if (restaurantRepository.existsById(reviewDTO.getRestaurantId()) && userRepository.existsById(reviewDTO.getUserId())) {



                //DB에 저장하기 위한 객체 생성 후 DB 저장
                ReviewEntity reviewEntity = ReviewEntity.registerComment(reviewDTO);
                reviewRepository.save(reviewEntity);

                //저장한 review의 reviewId가져오기
                Long reviewId = reviewEntity.getReviewId();

                uploadReviewImage(images,reviewId);


            } else {
                // 레스토랑이 존재하지 않을 때의 처리
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 Id가 없습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 입니다");
        }
        return ResponseEntity.ok("리뷰가 저장되었습니다");
    }

    //댓글 조회
    public List<ReviewResponseDTO> getReviews(Long restaurantId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByRestaurantId(restaurantId);
        List<ReviewResponseDTO> reviewResponses = new ArrayList<>();

        for (ReviewEntity reviewEntity : reviewEntities) {
            List<ReviewImageEntity> imagesForReview = reviewImageRepository.findAllByReviewId(reviewEntity.getReviewId());
            List<String> fileNames = new ArrayList<>();

            for (ReviewImageEntity imageEntity : imagesForReview) {
                Long fileId = imageEntity.getFileId();
                Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);

                if (fileEntityOptional.isPresent()) {
                    FileEntity fileEntity = fileEntityOptional.get();
                    String fileName = fileEntity.getFileLogicId(); // 파일 이름을 가져옴
                    fileNames.add(fileName);  // 파일 이름을 리스트에 추가
                }
            }
            ReviewResponseDTO responseDTO = new ReviewResponseDTO(reviewEntity, fileNames);
            reviewResponses.add(responseDTO);
        }
        return reviewResponses;
    }

    public ResponseEntity uploadReviewImage(List<MultipartFile> images, Long reviewId) throws IOException {
        try {
            //GCP에 업로드
            List<String> fullUrls = uploadImageToGCP(images);
            //file DB에 저장
            List<FileEntity> fileEntities = saveFileEntities(images, fullUrls);

            //ReviewImage DB에 저장하기 위한 객체 생성
            List<ReviewImageEntity> reviewImages = new ArrayList<>();
            for (FileEntity fileEntity : fileEntities) {
                ReviewImageEntity reviewImageEntity = new ReviewImageEntity(reviewId, fileEntity.getFileId());
                reviewImages.add(reviewImageEntity);

            }
            //만들어진 객체 저장
            reviewImageRepository.saveAll(reviewImages);
            return ResponseEntity.ok("이미지가 등록되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 업로드 실패");
        }

    }


    public ResponseEntity deleteReviewImage( Long reviewId) {
        List<ReviewImageEntity> reviewImageEntities = reviewImageRepository.findAllByReviewId(reviewId);

        for (ReviewImageEntity imageEntity : reviewImageEntities) {
            //파일 Repo에서 파일 Entity 가져오기
            Optional<FileEntity> fileEntityOptional = fileRepository.findById(imageEntity.getFileId());
            if (fileEntityOptional.isPresent()) {
                FileEntity fileEntity = fileEntityOptional.get();

                String logicName = fileEntity.getFileLogicId();
                String uuid = extractUUID(logicName);
                deleteObject(uuid); //GCP에서 지우기

                reviewImageRepository.delete(imageEntity);
                fileRepository.delete(fileEntity);
                return ResponseEntity.ok("리뷰가 삭제되었습니다");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 찾을 수 없습니다.");
            }

        }
        return null;
    }

    //댓글 삭제
    public synchronized ResponseEntity deleteReview(Long restaurantId, Long reviewId) {

        try {
            if (reviewRepository.existsById(reviewId)) {
                //삭제하려는 게시물이 해당 식당 리뷰 인지 확인
                if (reviewRepository.findAllByRestaurantId(restaurantId) != null) {
                    //리뷰 DB 에서 해당하는 리뷰 Entity 를 가져온다

                    List<ReviewImageEntity> reviewImageEntities = reviewImageRepository.findAllByReviewId(reviewId);
                    //리뷰 이미지의 값이 있다 -> 저장된 사진이 있으면
                    if (reviewImageEntities != null) {
                        System.out.println(reviewImageEntities);
                        for (ReviewImageEntity imageEntity : reviewImageEntities) {
                            //파일 Repo에서 파일 Entity 가져오기
                            Optional<FileEntity> fileEntityOptional = fileRepository.findById(imageEntity.getFileId());

                            if (fileEntityOptional.isPresent()) {
                                FileEntity fileEntity = fileEntityOptional.get();

                                String logicName = fileEntity.getFileLogicId();
                                String uuid = extractUUID(logicName);
                                deleteObject(uuid); //GCP에서 지우기

                                reviewImageRepository.delete(imageEntity);
                                fileRepository.delete(fileEntity);


                            } else {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 찾을 수 없습니다.");
                            }

                        }
                        reviewRepository.deleteById(reviewId);
                        return ResponseEntity.ok("리뷰가 삭제되었습니다");
                    }
                        reviewRepository.deleteById(reviewId);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("식당 아이디를 찾을 수 없습니다.");
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰를 찾을 수 없습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 입니다");
        }
        return null;
    }



@Transactional
    public ResponseEntity modifyReview(Long reviewId, ReviewDTO reviewDTO,List<MultipartFile> images) {
    try {
        //해당 레스토랑이 있는지
        if (restaurantRepository.existsById(reviewDTO.getRestaurantId())) {
            //리뷰 아이디가 존재하는지
            if (reviewRepository.existsById(reviewId)) {
                //해당하는 reviewEntity 받아옴
                ReviewEntity reviewEntity = reviewRepository.findById(reviewId).orElse(null);

                if (reviewEntity.getUserId().equals(reviewDTO.getUserId())) {
                    //entity와 현재 들어온 아이디 일치하는지 확인

                    if (!images.isEmpty()) {
                        //수정하려는 이미지가 있는 경우
                        //기존에는 이미지가 존재했는지 확인
                        if (reviewImageRepository.existsById(reviewId)) {
                            //이미지를 지우기

                            List<ReviewImageEntity> reviewImageEntities = reviewImageRepository.findAllByReviewId(reviewId);
                            if (reviewImageEntities != null) {

                                for (ReviewImageEntity imageEntity : reviewImageEntities) {
                                    //파일 Repo에서 파일 Entity 가져오기
                                    Optional<FileEntity> fileEntityOptional;
                                    fileEntityOptional = fileRepository.findById(imageEntity.getFileId());

                                    if (fileEntityOptional.isPresent()) {
                                        FileEntity fileEntity = fileEntityOptional.get();

                                        String logicName = fileEntity.getFileLogicId();
                                        String uuid = extractUUID(logicName);
                                        deleteObject(uuid); //GCP에서 지우기

                                        reviewImageRepository.delete(imageEntity);
                                        fileRepository.delete(fileEntity);

                                    } else {
                                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 찾을 수 없습니다.");
                                    }

                                }
                                reviewRepository.deleteById(reviewId);
                                return ResponseEntity.ok("리뷰가 수정되었습니다");
                            }
                        }
                        //업로드 진행
                        uploadReviewImage(images, reviewId);
                    }
                    //바로 셋팅
                    //이미지 없는 경우 바로 review 만 수정
                    reviewEntity.setRating(reviewDTO.getRating());
                    reviewEntity.setComment(reviewDTO.getComment());
                    reviewEntity.setUpdatedDate(LocalDateTime.now());
                    reviewRepository.save(reviewEntity);

                    return ResponseEntity.ok("리뷰가 수정되었습니다");

                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인의 리뷰만 수정할 수 있습니다");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰를 찾을 수 없습니다");

            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("식당을 찾을 수 없습니다");
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
}




