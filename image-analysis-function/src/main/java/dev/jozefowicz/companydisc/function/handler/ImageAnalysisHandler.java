package dev.jozefowicz.companydisc.function.handler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class ImageAnalysisHandler extends SpringBootRequestHandler<S3Event, Void> {
}
