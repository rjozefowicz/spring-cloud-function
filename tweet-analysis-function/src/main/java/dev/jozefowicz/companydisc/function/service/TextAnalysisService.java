package dev.jozefowicz.companydisc.function.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;

@Service
public class TextAnalysisService {

    private final ComprehendClient comprehendClient;
    private final TweetRepository tweetRepository;

    public TextAnalysisService(ComprehendClient comprehendClient, TweetRepository tweetRepository) {
        this.comprehendClient = comprehendClient;
        this.tweetRepository = tweetRepository;
    }

    public void analyse(String id) {
        try {
            final TweetDTO tweet = tweetRepository.findById(id);
            System.out.println("Tweet found " + tweet.getId());
            final DetectSentimentResponse sentiment = comprehendClient.detectSentiment(DetectSentimentRequest.builder().languageCode(LanguageCode.EN).text(tweet.getBody()).build());
            tweet.setSentiment(sentiment.sentimentAsString());
            System.out.println("sentiment " + tweet.getSentiment());
            tweetRepository.persist(tweet);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
