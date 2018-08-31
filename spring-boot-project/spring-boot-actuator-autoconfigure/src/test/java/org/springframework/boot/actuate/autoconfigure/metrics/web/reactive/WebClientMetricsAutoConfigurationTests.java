/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.metrics.web.reactive;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.metrics.web.reactive.client.WebClientExchangeTagsProvider;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.mock.http.client.reactive.MockClientHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link WebClientMetricsAutoConfiguration}
 *
 * @author Brian Clozel
 */
public class WebClientMetricsAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(MetricsAutoConfiguration.class,
					WebClientMetricsAutoConfiguration.class,
					SimpleMetricsExportAutoConfiguration.class,
					WebClientAutoConfiguration.class));

	private ClientHttpConnector connector;

	@Before
	public void setup() {
		this.connector = mock(ClientHttpConnector.class);
		given(this.connector.connect(any(), any(), any()))
				.willReturn(Mono.just(new MockClientHttpResponse(HttpStatus.OK)));
	}

	@Test
	public void webClientCreatedWithBuilderIsInstrumented() {
		this.contextRunner.run((context) -> {
			WebClient.Builder builder = context.getBean(WebClient.Builder.class);
			WebClient webClient = builder.clientConnector(this.connector).build();
			MeterRegistry registry = context.getBean(MeterRegistry.class);
			assertThat(registry.find("http.client.requests").meter()).isNull();
			webClient.get().uri("http://example.org/projects/{project}", "spring-boot")
					.exchange().block();
			assertThat(registry.find("http.client.requests")
					.tags("uri", "/projects/{project}").meter()).isNotNull();
		});
	}

	@Test
	public void shouldNotOverrideCustomTagsProvider() {
		this.contextRunner.withUserConfiguration(CustomTagsProviderConfig.class)
				.run((context) -> assertThat(context)
						.getBeans(WebClientExchangeTagsProvider.class).hasSize(1)
						.containsKey("customTagsProvider"));
	}

	@Configuration
	protected static class CustomTagsProviderConfig {

		@Bean
		public WebClientExchangeTagsProvider customTagsProvider() {
			return mock(WebClientExchangeTagsProvider.class);
		}

	}

}
