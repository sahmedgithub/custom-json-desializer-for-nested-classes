package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class DominaWrapper {
    private String groupId;


    @Data
    class TransactionWrap {

        private String number;

        private En en;

        @Document
        @Data
        class En {
            @JsonIgnore
            private String id = getGroupId();

            @JsonIgnore
            private String transactionId = getNumber();

            private String firstName;
        }
    }

    @JsonDeserialize(contentUsing = NestedDeserializer.class)
    private List<TransactionWrap> enList;
}
