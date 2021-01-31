package dev.jozefowicz.companydisc.function.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.MetadataDirective;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageAnalysisService {

    private final static List<String> EXTENSIONS = List.of(".jpg", ".jpeg", ".gif", ".bmp", ".png");

    private final RekognitionClient rekognitionClient;
    private final S3Client s3Client;

    public ImageAnalysisService(RekognitionClient rekognitionClient, S3Client s3Client) {
        this.rekognitionClient = rekognitionClient;
        this.s3Client = s3Client;
    }

    public void analyse(String bucketName, String key) {
        String[] parts = key.split("/");

        if (EXTENSIONS.stream().anyMatch(extension -> parts[parts.length - 1].contains(extension))) {
            System.out.println("Analyzing image");
            final DetectLabelsResponse res = rekognitionClient.detectLabels(DetectLabelsRequest.builder()
                    .image(
                            Image.builder().s3Object(
                                    S3Object.builder().bucket(bucketName).name(key).build()
                            ).build())
                    .maxLabels(10)
                    .minConfidence(0.75f)
                    .build());

            System.out.println("Found labels: ");
            res.labels().forEach(label -> System.out.println(label.name()));

            final CopyObjectRequest copyObjectRequest = CopyObjectRequest
                    .builder()
                    .copySource(String.format("%s/%s", bucketName, key))
                    .metadata(Map.of("tags", String.join(",", res.labels().stream().map(label -> label.name()).collect(Collectors.toList()))))
                    .destinationKey(key)
                    .metadataDirective(MetadataDirective.REPLACE)
                    .destinationBucket(bucketName)
                    .build();
            s3Client.copyObject(copyObjectRequest);

        }

    }
}
