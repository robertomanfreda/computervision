package com.ro8m4n.computervision;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public Gson prettyGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
