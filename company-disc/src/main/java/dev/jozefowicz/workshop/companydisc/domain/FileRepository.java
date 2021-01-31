package dev.jozefowicz.workshop.companydisc.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileRepository {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final boolean tagsSupported;

    public FileRepository(AmazonS3 amazonS3, @Value("${bucket.name}") String bucketName, @Value("${tags.supported}") boolean tagsSupported) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.tagsSupported = tagsSupported;
    }

    public void upload(final MultipartFile multipartFile) throws IOException {
        amazonS3.putObject(new PutObjectRequest(bucketName, String.format("%s/%s", UUID.randomUUID().toString(), multipartFile.getOriginalFilename()), multipartFile.getInputStream(), new ObjectMetadata()));
    }

    public InputStream download(final String fileId, final String fileName) {
        return amazonS3.getObject(bucketName, String.format("%s/%s", fileId, fileName)).getObjectContent();
    }

    public List<FileDTO> listAll() {
        return amazonS3.listObjects(bucketName).getObjectSummaries().stream().map(s3ObjectSummary -> {
            String[] parts = s3ObjectSummary.getKey().split("/");
            if (parts.length == 2) {
                FileDTO dto = new FileDTO();
                dto.setSize(s3ObjectSummary.getSize());
                dto.setUploadDate(s3ObjectSummary.getLastModified().toString());
                dto.setFileId(parts[0]);
                dto.setFileName(parts[1]);
                final ObjectMetadata objectMetadata = amazonS3.getObjectMetadata(bucketName, s3ObjectSummary.getKey());
                if (tagsSupported && objectMetadata.getUserMetadata().containsKey("tags")) {
                    dto.setTags(objectMetadata.getUserMetadata().get("tags").split(","));
                }
                return dto;
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
