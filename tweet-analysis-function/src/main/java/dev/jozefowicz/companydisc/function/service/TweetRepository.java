package dev.jozefowicz.companydisc.function.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TweetRepository {
    private final DynamoDB dynamoDB;
    private final Table table;
    private final ObjectMapper objectMapper;

    public TweetRepository(DynamoDB dynamoDB, ObjectMapper objectMapper) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.table = dynamoDB.getTable(System.getenv("TWEETS_TABLE_NAME"));
    }

    public void persist(TweetDTO dto) throws JsonProcessingException {
        table.putItem(Item.fromJSON(objectMapper.writeValueAsString(dto)));
    }

    public TweetDTO findById(String id) throws JsonProcessingException {
        final Item item = table.getItem("id", id);
        return objectMapper.readValue(item.toJSON(), TweetDTO.class);
    }

}
