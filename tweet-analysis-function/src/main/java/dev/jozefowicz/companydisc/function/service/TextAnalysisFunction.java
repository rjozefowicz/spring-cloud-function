package dev.jozefowicz.companydisc.function.service;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class TextAnalysisFunction implements Function<DynamodbEvent, String> {

    private final TextAnalysisService textAnalysisService;

    public TextAnalysisFunction(TextAnalysisService textAnalysisService) {
        this.textAnalysisService = textAnalysisService;
    }

    @Override
    public String apply(DynamodbEvent dynamodbEvent) {
        System.out.println("Start analyzing");
        dynamodbEvent
                .getRecords()
                .forEach(record -> {
                    final Map<String, AttributeValue> keys = record.getDynamodb().getKeys();
                    final String id = keys.get("id").getS();
                    textAnalysisService.analyse(id);
                });
        return "processed";
    }
}
