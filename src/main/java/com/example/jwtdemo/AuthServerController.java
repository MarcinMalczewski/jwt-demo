/*
 * Copyright 2002-2018 the original author or authors.
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

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class AuthServerController {

	@Autowired
	private JwtToken jwtToken;

	@GetMapping("/token/{username}")
	public Mono<String> generateToken(
			@PathVariable(value = "username") String username) {
		return Mono.just(username)
				.flatMap(un -> Mono.fromCallable(() -> {
					String token = null;
					try {
						token = jwtToken.generate(un);
					} catch (JOSEException e) {
						e.printStackTrace();
					}
					return token;
				}).subscribeOn(Schedulers.boundedElastic()));
	}
}
