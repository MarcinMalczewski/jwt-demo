/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.jwtdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.interfaces.RSAPublicKey;

@EnableWebFluxSecurity
public class SecurityConfig {

    /*
    testing commands:
    generate token (JWT): curl -i localhost:8080/token/admin
    setup TOKEN variable: export TOKEN={JWT}
    make request adding JWT in a header: curl -i -H "Authorization: Bearer $TOKEN" localhost:8080/message
     */

    @Value("${spring.security.oauth2.resourceserver.jwt.public-key}")
    private RSAPublicKey key;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/token/**").permitAll()
                .pathMatchers("/public-message").permitAll()
                .pathMatchers("/message/**").hasAuthority("SCOPE_message:read")
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec
                                .jwt(jwt ->
                                        jwt.jwtDecoder(jwtDecoder())
                                )
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(this.key).build();
    }
}
