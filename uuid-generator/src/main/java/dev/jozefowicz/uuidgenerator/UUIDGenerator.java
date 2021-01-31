package dev.jozefowicz.uuidgenerator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.UUID;

public class UUIDGenerator implements RequestHandler<Void, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Void unused, Context context) {
        final String res = System.getenv("PREFIX") + "-" + UUID.randomUUID().toString();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody(res);
        return response;
    }
}
