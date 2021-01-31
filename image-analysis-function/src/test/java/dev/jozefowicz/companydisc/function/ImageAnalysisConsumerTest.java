package dev.jozefowicz.companydisc.function;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageAnalysisConsumerTest {

    private static final String BUCKET_NAME = "company-disc-rj";
    private static final String KEY = "";

    @Autowired
    private Consumer<S3Event> imageAnalysisConsumer;

    @Test
    public void contextLoad() {}

    @Test
    public void shouldAnalyseImage() {
        // given
        final S3Event s3Event = new S3Event(new ArrayList<>());
        final S3EventNotification.S3BucketEntity bucket = new S3EventNotification.S3BucketEntity(
                BUCKET_NAME,
                null,
                null);
        final S3EventNotification.S3ObjectEntity object = new S3EventNotification.S3ObjectEntity(
                KEY,
                null,
                null,
                null,
                null);
        final S3EventNotification.S3Entity s3Entity = new S3EventNotification.S3Entity(
                null,
                bucket,
                object,
                null);

        final S3EventNotification.S3EventNotificationRecord record =
                new S3EventNotification.S3EventNotificationRecord(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        s3Entity,
                        null);
        s3Event.getRecords().add(record);
        // when
        imageAnalysisConsumer.accept(s3Event);

        // then
    }
}
