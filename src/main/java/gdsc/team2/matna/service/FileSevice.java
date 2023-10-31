package gdsc.team2.matna.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import gdsc.team2.matna.entity.FileEntity;
import gdsc.team2.matna.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileSevice {

    @Autowired
    FileRepository fileRepository;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String matnabucket;

    Storage storage = StorageOptions.getDefaultInstance().getService();

    private String generateUUIDFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        int lastDot = originalFileName.lastIndexOf(".");
        String extension = originalFileName.substring(lastDot);
        return uuid + extension;
    }

    public static String extractUUID(String url) {
        String uuid = null;
        String regex = "https://storage.cloud.google.com/matna-bucket/([a-f0-9-]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            uuid = matcher.group(1);
        }

        return uuid;
    }


    //GCP에 업로드하는 함수
    public List<String> uploadImageToGCP(List<MultipartFile> images) throws IOException {
        List<String> fullImageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            //UUID 이름으로 만든다
            String uuidFileName = generateUUIDFileName(image) + ".png"; // 파일 확장자 추가
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(matnabucket, uuidFileName)
                            .setContentType(image.getContentType())
                            .build(),
                    image.getInputStream()
            );
            //전체 URL을 얻는 부분 (DB에 저장 위해)
            String bucketName = blobInfo.getBucket();
            String objectName = blobInfo.getName();
            String fullImageUrl = "https://storage.cloud.google.com/" + bucketName + "/" + objectName;

            fullImageUrls.add(fullImageUrl);
        }

        return fullImageUrls;
    }

    //파일 DB에 저장하는 함수
    public  List<FileEntity> saveFileEntities(List<MultipartFile> images,List<String> fullImageUrls) {
        List<FileEntity> fileEntities = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String fullImageUrl = fullImageUrls.get(i);
            // 파일 정보를 DB에 저장
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileLogicId(fullImageUrl);
            fileEntity.setFileOrgName(image.getOriginalFilename());
            fileRepository.save(fileEntity);

            fileEntities.add(fileEntity);
        }
        return fileEntities;
    }

    //GCP에서 파일 제거하는 함수
    public static void deleteObject(String objectName) {

        String projectId = "applied-card-403302";
        String bucketName = "matna-bucket";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null) {
            System.out.println("The object " + objectName + " wasn't found in " + bucketName);
            return;
        }

        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());

        storage.delete(bucketName, objectName, precondition);

    }


}
