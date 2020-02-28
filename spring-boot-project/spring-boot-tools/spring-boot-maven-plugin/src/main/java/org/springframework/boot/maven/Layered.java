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

package org.springframework.boot.maven;

/**
 * Layer configuration options.
 *
 * @author Madhura Bhave
 * @since 2.3.0
 */
public class Layered {

	private boolean enabled;

	private boolean includeLayerTools = true;

	/**
	 * Whether layered jar layout is enabled.
	 * @return true if the layered layout is enabled.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Whether to include the layer tools jar.
	 * @return true if layer tools should be included
	 */
	public boolean isIncludeLayerTools() {
		return this.includeLayerTools;
	}

}
