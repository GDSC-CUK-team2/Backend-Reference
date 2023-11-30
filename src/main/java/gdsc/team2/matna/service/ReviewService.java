package gdsc.team2.matna.service;


import gdsc.team2.matna.dto.ReviewDTO;
import gdsc.team2.matna.dto.ReviewResponseDTO;
import gdsc.team2.matna.entity.FileEntity;
import gdsc.team2.matna.entity.ReviewEntity;
import gdsc.team2.matna.entity.ReviewImageEntity;

import gdsc.team2.matna.entity.Shop;
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


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ReviewService extends  FileSevice{

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ShopService shopService;

@Autowired
    FileRepository fileRepository;

    @Autowired
    ReviewImageRepository reviewImageRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String matnabucket;

    Storage storage = StorageOptions.getDefaultInstance().getService();



    public ResponseEntity<String> reviewSave(ReviewDTO reviewDTO, List<MultipartFile> images) {
        try {
            if (shopRepository.existsByShopUid(reviewDTO.getShopId()) && userRepository.existsById(reviewDTO.getUserId())) {
                Shop shop = shopService.findByUid(reviewDTO.getShopId());
                shop.addReview();
                shop.addRating(reviewDTO.getRating().getPoint());
                shopRepository.save(shop);

                ReviewEntity reviewEntity = ReviewEntity.registerComment(reviewDTO);
                reviewRepository.save(reviewEntity);

                Long reviewId = reviewEntity.getReviewId();

                uploadReviewImage(images, reviewId);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당하는 Id가 없습니다");
            }
            return ResponseEntity.ok("리뷰가 저장되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 입니다");
        }
    }

    //댓글 조회
    public ResponseEntity<List<ReviewResponseDTO>> getReviews(Long shopId) {
        try {
            List<ReviewEntity> reviewEntities = reviewRepository.findAllByShopId(shopId);
            List<ReviewResponseDTO> reviewResponses = new ArrayList<>();

            for (ReviewEntity reviewEntity : reviewEntities) {
                List<ReviewImageEntity> imagesForReview = reviewImageRepository.findAllByReviewId(reviewEntity.getReviewId());
                List<String> fileNames = new ArrayList<>();

                for (ReviewImageEntity imageEntity : imagesForReview) {
                    Long fileId = imageEntity.getFileId();
                    Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);

                    if (fileEntityOptional.isPresent()) {
                        FileEntity fileEntity = fileEntityOptional.get();
                        String fileName = fileEntity.getFileLogicId();
                        fileNames.add(fileName);
                    }
                }
                ReviewResponseDTO responseDTO = new ReviewResponseDTO(reviewEntity, fileNames);
                reviewResponses.add(responseDTO);
            }
            return ResponseEntity.ok(reviewResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<String> uploadReviewImage(List<MultipartFile> images, Long reviewId) {
        try {
            List<String> fullUrls = uploadImageToGCP(images);
            List<FileEntity> fileEntities = saveFileEntities(images, fullUrls);

            List<ReviewImageEntity> reviewImages = new ArrayList<>();
            for (FileEntity fileEntity : fileEntities) {
                ReviewImageEntity reviewImageEntity = new ReviewImageEntity(reviewId, fileEntity.getFileId());
                reviewImages.add(reviewImageEntity);
            }

            reviewImageRepository.saveAll(reviewImages);
            return ResponseEntity.ok("이미지가 등록되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 업로드 실패");
        }
    }


    //댓글 삭제
    public ResponseEntity<String> deleteReview(Long shopId, Long reviewId) {
        try {
            if (reviewRepository.existsById(reviewId)) {
                if (reviewRepository.findAllByShopId(shopId) != null) {
                    List<ReviewImageEntity> reviewImageEntities = reviewImageRepository.findAllByReviewId(reviewId);

                    if (reviewImageEntities != null) {
                        for (ReviewImageEntity imageEntity : reviewImageEntities) {
                            Optional<FileEntity> fileEntityOptional = fileRepository.findById(imageEntity.getFileId());

                            if (fileEntityOptional.isPresent()) {
                                FileEntity fileEntity = fileEntityOptional.get();
                                String logicName = fileEntity.getFileLogicId();
                                deleteObject(logicName);
                                reviewImageRepository.delete(imageEntity);
                                fileRepository.delete(fileEntity);
                            }
                        }
                    }
                    ReviewEntity review = reviewRepository.findById(reviewId).get();
                    Shop shop = shopService.findByUid(shopId);
                    shop.subReview();
                    shop.subRating(review.getRating().getPoint());
                    shopRepository.save(shop);

                    reviewRepository.deleteById(reviewId);
                    return ResponseEntity.ok("리뷰가 삭제되었습니다");
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
    }



@Transactional
public ResponseEntity<String> modifyReview(Long reviewId, ReviewDTO reviewDTO, List<MultipartFile> images) {
    try {
        if (shopRepository.existsByShopUid(reviewDTO.getShopId())) {
            if (reviewRepository.existsById(reviewId)) {
                ReviewEntity reviewEntity = reviewRepository.findById(reviewId).get();

                if (reviewEntity.getUserId().equals(reviewDTO.getUserId())) {
                    if (!images.isEmpty()) {
                        List<ReviewImageEntity> reviewImageEntities = reviewImageRepository.findAllByReviewId(reviewId);
                        if (!reviewImageEntities.isEmpty()) {
                            for (ReviewImageEntity imageEntity : reviewImageEntities) {
                                Optional<FileEntity> fileEntityOptional = fileRepository.findById(imageEntity.getFileId());

                                if (fileEntityOptional.isPresent()) {
                                    FileEntity fileEntity = fileEntityOptional.get();
                                    String logicName = fileEntity.getFileLogicId();
                                    deleteObject(logicName);
                                    reviewImageRepository.delete(imageEntity);
                                    fileRepository.deleteById(fileEntity.getFileId());
                                }
                            }
                        }
                        uploadReviewImage(images, reviewId);
                    }

                    Shop shop = shopService.findByUid(reviewDTO.getShopId());
                    shop.subRating(reviewEntity.getRating().getPoint());
                    shop.addRating(reviewDTO.getRating().getPoint());
                    shopRepository.save(shop);

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




