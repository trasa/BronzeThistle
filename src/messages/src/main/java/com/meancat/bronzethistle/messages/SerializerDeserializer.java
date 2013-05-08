package com.meancat.bronzethistle.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SerializerDeserializer {
    @Autowired
    protected ObjectMapper objectMapper;

    public Message stringToMessage(String data) throws IOException {
        return objectMapper.readValue(data, Message.class);
    }

    public String messageToString(Message message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }
}
