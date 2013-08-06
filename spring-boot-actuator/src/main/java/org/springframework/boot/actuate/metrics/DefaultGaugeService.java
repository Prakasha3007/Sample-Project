/*
 * Copyright 2012-2013 the original author or authors.
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

package org.springframework.boot.actuate.metrics;

import java.util.Date;

/**
 * Default implementation of {@link GaugeService}.
 * 
 * @author Dave Syer
 */
public class DefaultGaugeService implements GaugeService {

	private MetricRepository metricRepository;

	/**
	 * @param counterRepository
	 */
	public DefaultGaugeService(MetricRepository counterRepository) {
		super();
		this.metricRepository = counterRepository;
	}

	@Override
	public void set(String metricName, double value) {
		this.metricRepository.set(wrap(metricName), value, new Date());
	}

	private String wrap(String metricName) {
		if (metricName.startsWith("gauge")) {
			return metricName;
		}
		else {
			return "gauge." + metricName;
		}
	}

}
