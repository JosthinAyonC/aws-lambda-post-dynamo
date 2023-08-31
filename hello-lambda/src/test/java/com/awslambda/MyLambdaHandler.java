package com.awslambda;

import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

/**
 * Unit test for simple App.
 */
public class MyLambdaHandler {
    @Test
    public void test1() {
        MyLambdaHandler helloLambda = new MyLambdaHandler();

        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();

        Context context = null;

    }
}
