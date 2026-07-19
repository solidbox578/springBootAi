package com.vibhu.openai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pojo.CountryCity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/structured-output")
public class StructuredOutputController {

    private static final Logger LOG = LoggerFactory.getLogger(StructuredOutputController.class);

    private final ChatClient ollamaChatClient;

    public StructuredOutputController(@Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    @GetMapping("/chat-bean")
    public ResponseEntity<CountryCity> sendMessageGetBean(@RequestParam String message){
        CountryCity countryCity = ollamaChatClient.prompt().user(message)
                .call().entity(CountryCity.class);
        LOG.info("Received structured Pojo output Using Bean: {}", countryCity);
        return ResponseEntity.ok(countryCity);
    }

    @GetMapping("/chat-bean-converter")
    public ResponseEntity<CountryCity> sendMessageGetBeanUsingConverter(@RequestParam String message){
        CountryCity countryCity = ollamaChatClient.prompt().user(message)
                .call().entity(new BeanOutputConverter<>(CountryCity.class));
        LOG.info("Received structured Pojo output Using BeanOutputConverter: {}", countryCity);
        return ResponseEntity.ok(countryCity);
    }

    @GetMapping("/chat-list")
    public ResponseEntity<List<String>> sendMessageGetList(@RequestParam String message){
        List<String> countryCityList = ollamaChatClient.prompt().user(message)
                .call().entity(new ListOutputConverter());
        LOG.info("Received structured List output using ListOutputConverter: {}", countryCityList);
        return ResponseEntity.ok(countryCityList);
    }

    @GetMapping("/chat-map")
    public ResponseEntity<Map<String, Object>> sendMessageGetMap(@RequestParam String message){
        Map<String, Object> countryCityMap = ollamaChatClient.prompt().user(message)
                .call().entity(new MapOutputConverter());
        LOG.info("Received structured Map output using MapOutputConverter: {}", countryCityMap);
        return ResponseEntity.ok(countryCityMap);
    }

    @GetMapping("/chat-pojo-list")
    public ResponseEntity<List<CountryCity>> sendMessageGetPojoList(@RequestParam String message){
        List<CountryCity> countryCityList = ollamaChatClient.prompt().user(message)
                .call().entity(new ParameterizedTypeReference<List<CountryCity>>() {
                });
        LOG.info("Received structured List of Pojo output using ParameterizedTypeReference: {}", countryCityList);
        return ResponseEntity.ok(countryCityList);
    }


}
