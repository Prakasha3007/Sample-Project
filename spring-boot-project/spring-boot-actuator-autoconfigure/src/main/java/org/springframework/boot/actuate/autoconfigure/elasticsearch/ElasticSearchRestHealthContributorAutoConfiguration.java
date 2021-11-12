/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.actuate.autoconfigure.elasticsearch;

import org.elasticsearch.client.RestClient;

import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthContributorConfigurations.RestClientHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for for Elasticsearch health
 * incidator using REST clients.
 *
 * @author Artsiom Yudovin
 * @since 2.1.1
 */
@SuppressWarnings("deprecation")
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestClient.class)
@ConditionalOnEnabledHealthIndicator("elasticsearch")
@AutoConfigureAfter(ElasticsearchRestClientAutoConfiguration.class)
@Import({ ElasticSearchRestHealthContributorConfigurations.RestHighLevelClientHealthContributorConfiguration.class,
		RestClientHealthContributorConfiguration.class })
public class ElasticSearchRestHealthContributorAutoConfiguration {

}
