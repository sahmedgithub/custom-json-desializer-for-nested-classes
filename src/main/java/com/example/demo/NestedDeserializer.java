package com.example.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class NestedDeserializer extends StdDeserializer<DominaWrapper.TransactionWrap> implements ResolvableDeserializer {

    private JsonDeserializer<Object> underlyingDeserializer;


    protected NestedDeserializer() {
        super(DominaWrapper.TransactionWrap.class);
    }

    @Override
    public DominaWrapper.TransactionWrap deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        JsonStreamContext ourContext = jsonParser.getParsingContext();
        JsonStreamContext listContext = ourContext.getParent();
        JsonStreamContext containerContext = listContext.getParent();

        DominaWrapper container = (DominaWrapper) containerContext.getCurrentValue();

        DominaWrapper.TransactionWrap transactionWrap = container.new TransactionWrap();
        transactionWrap.setEn(transactionWrap.new En());

        underlyingDeserializer.deserialize(jsonParser, deserializationContext, transactionWrap);

        return transactionWrap;
    }

    @Override
    public void resolve(DeserializationContext deserializationContext) throws JsonMappingException {
        underlyingDeserializer = deserializationContext
                .findRootValueDeserializer(deserializationContext.getTypeFactory().constructType(DominaWrapper.TransactionWrap.class));

    }
}
