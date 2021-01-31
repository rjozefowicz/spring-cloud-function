package dev.jozefowicz.workshop.companydisc.domain;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TweetRepository {
    private final DynamoDB dynamoDB;
    private final Table table;
    private final ObjectMapper objectMapper;

    public TweetRepository(DynamoDB dynamoDB, @Value("${tweet.table}") String table, ObjectMapper objectMapper) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.table = dynamoDB.getTable(table);
    }

    public void persist(TweetDTO dto) throws JsonProcessingException {
        table.putItem(Item.fromJSON(objectMapper.writeValueAsString(dto)));
    }

    public List<TweetDTO> findAll() {
        final List<TweetDTO> notes = new ArrayList<>();
        table.scan().forEach(item -> {
            try {
                notes.add(objectMapper.readValue(item.toJSON(), TweetDTO.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return notes;
    }
}
