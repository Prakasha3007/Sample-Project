/*
 * Copyright 2012-2015 the original author or authors.
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

package org.springframework.boot.actuate.security;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

/**
 * {@link ApplicationListener} to expose Spring Security
 * {@link AbstractAuthenticationEvent authentication events} as {@link AuditEvent}s.
 *
 * @author Dave Syer
 * @author Vedran Pavic
 */
public interface AuthenticationAuditListener extends
		ApplicationListener<AbstractAuthenticationEvent>, ApplicationEventPublisherAware {

	void setApplicationEventPublisher(ApplicationEventPublisher publisher);

	void onApplicationEvent(AbstractAuthenticationEvent event);

}
