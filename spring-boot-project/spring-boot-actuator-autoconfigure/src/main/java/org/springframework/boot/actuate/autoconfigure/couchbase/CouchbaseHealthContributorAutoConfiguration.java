/*
 * Copyright 2012-2025 the original author or authors.
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

package org.springframework.boot.actuate.autoconfigure.couchbase;

import com.couchbase.client.java.Cluster;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.couchbase.CouchbaseHealthIndicator;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for
 * {@link CouchbaseHealthIndicator}.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Andy Wilkinson Nicoll
 * @since 2.0.0
 */
@AutoConfiguration(
		after = { CouchbaseAutoConfiguration.class, CouchbaseReactiveHealthContributorAutoConfiguration.class })
@ConditionalOnClass(Cluster.class)
@ConditionalOnBean(Cluster.class)
@ConditionalOnEnabledHealthIndicator("couchbase")
public class CouchbaseHealthContributorAutoConfiguration
		extends CompositeHealthContributorConfiguration<CouchbaseHealthIndicator, Cluster> {

	public CouchbaseHealthContributorAutoConfiguration() {
		super(CouchbaseHealthIndicator::new);
	}

	@Bean
	@ConditionalOnMissingBean(name = { "couchbaseHealthIndicator", "couchbaseHealthContributor" })
	public HealthContributor couchbaseHealthContributor(ConfigurableListableBeanFactory beanFactory) {
		return createContributor(beanFactory, Cluster.class);
	}

}
