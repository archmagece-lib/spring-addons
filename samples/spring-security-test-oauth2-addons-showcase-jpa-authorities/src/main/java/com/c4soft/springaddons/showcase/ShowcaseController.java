/*
 * Copyright 2019 Jérôme Wacongne
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
package com.c4soft.springaddons.showcase;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ShowcaseController {
	@GetMapping("greeting")
	public String getGreeting(Authentication authentication) {
		return String.format("Hello, %s!", authentication.getName());
	}

	@GetMapping("restricted/greeting")
	public String getRestrictedGreeting(Authentication authentication) {
		return "Welcome to restricted area.";
	}

	@GetMapping("jwt")
	public String getJwtClaims(@AuthenticationPrincipal Jwt jwt) {
		return jwt.getClaims().toString();
	}
}