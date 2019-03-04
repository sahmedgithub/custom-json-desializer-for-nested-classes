package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class GroTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String data = "{\"groupId\": \"22\", \"enList\": [ { \"number\": \"1\", \"en\" : { \"firstName\": \"babu\" } }, { \"number\": \"2\", \"en\" : { \"firstName\": \"babu\" } } ] }";

    @Test
    void testJsonData() throws IOException {
        DominaWrapper dominaWrapper = objectMapper.readValue(data, DominaWrapper.class);
        List<DominaWrapper.TransactionWrap> enList = dominaWrapper.getEnList();

        enList.stream().forEach( transactionWrap -> {
            System.out.println(transactionWrap.getEn().toString());
        } );


    }
}

