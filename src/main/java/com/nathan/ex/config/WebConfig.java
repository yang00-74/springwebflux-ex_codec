package com.nathan.ex.config;

import com.nathan.ex.config.codec.ExDecoder;
import com.nathan.ex.config.codec.ExEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @author nathan.yang
 */
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {

        configurer.customCodecs().decoder(new ExDecoder());
        configurer.customCodecs().writer(new EncoderHttpMessageWriter<>(new ExEncoder()));
    }
}