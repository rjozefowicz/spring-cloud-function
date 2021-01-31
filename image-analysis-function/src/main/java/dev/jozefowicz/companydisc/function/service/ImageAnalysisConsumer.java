package dev.jozefowicz.companydisc.function.service;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ImageAnalysisConsumer implements Consumer<S3Event> {

    private final ImageAnalysisService imageAnalysisService;

    public ImageAnalysisConsumer(ImageAnalysisService imageAnalysisService) {
        this.imageAnalysisService = imageAnalysisService;
    }

    @Override
    public void accept(S3Event s3Event) {
        s3Event
                .getRecords()
                .forEach(record -> imageAnalysisService.analyse(record.getS3().getBucket().getName(), record.getS3().getObject().getKey()));

    }
}
