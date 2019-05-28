/*
 * Copyright 2019 Jérôme Wacongne.
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
package org.springframework.security.test.context.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.OAuth2Authentication;
import org.springframework.security.oauth2.server.resource.authentication.embedded.AuthoritiesClaimGrantedAuthoritiesService;
import org.springframework.security.oauth2.server.resource.authentication.embedded.WithAuthoritiesJwtClaimSet;
import org.springframework.security.test.context.support.WithMockJwtClaimSet.Factory;

/**
 * @author Jérôme Wacongne &lt;ch4mp&#64;c4-soft.com&gt;
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = Factory.class)
public @interface WithMockJwtClaimSet {

	@AliasFor("authorities")
	String[] value() default { "ROLE_USER" };

	@AliasFor("value")
	String[] authorities() default { "ROLE_USER" };

	@AliasFor("subject")
	String name() default "user";

	@AliasFor("name")
	String subject() default "user";

	StringAttribute[] claims() default {};

	@AliasFor(annotation = WithSecurityContext.class)
	TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;

	public final class Factory implements WithSecurityContextFactory<WithMockJwtClaimSet> {
		private final StringAttributeParserSupport parsingSupport = new StringAttributeParserSupport();

		@Override
		public SecurityContext createSecurityContext(WithMockJwtClaimSet annotation) {
			final SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication(annotation));

			return context;
		}

		public OAuth2Authentication<WithAuthoritiesJwtClaimSet> authentication(WithMockJwtClaimSet annotation) {
			final var claimsBuilder = WithAuthoritiesJwtClaimSet.builder();
			parsingSupport.parse(annotation.claims()).forEach(claimsBuilder::claim);

			claimsBuilder.subject(annotation.subject());
			claimsBuilder.authorities(annotation.authorities());

			return new OAuth2Authentication<>(claimsBuilder.build(), new AuthoritiesClaimGrantedAuthoritiesService());
		}
	}
}
