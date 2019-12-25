package com.spring4all.swagger;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;

/**
 * @Author: He Zhigang
 * @Date: 2019/5/8 10:48
 * @Description:
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.http.converters.preferred-json-mapper", havingValue = "gson")
public class GsonSerializerConfigration {
    class SpringfoxJsonToGsonSerializer implements JsonSerializer<Json> {
        @Override
        public JsonElement serialize(Json json, Type type, JsonSerializationContext jsonSerializationContext) {
            final JsonParser parser = new JsonParser();
            return parser.parse(json.value());
        }
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonSerializer());
        log.info("注册 SpringfoxJsonToGsonSerializer TypeAdapter");
        gsonHttpMessageConverter.setGson(builder.create());
        return gsonHttpMessageConverter;
    }
}
