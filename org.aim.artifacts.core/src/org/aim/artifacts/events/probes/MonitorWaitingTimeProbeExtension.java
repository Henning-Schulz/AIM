/**
 * Copyright 2014 SAP AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aim.artifacts.events.probes;

import java.util.HashSet;
import java.util.Set;

import org.aim.api.events.AbstractEventProbe;
import org.aim.api.events.AbstractEventProbeExtension;
import org.aim.description.scopes.SynchronizedScope;

/**
 * Event probe for gathering the waiting times on monitor(synchronization)
 * requests.
 * 
 * @author Alexander Wert
 * 
 */
public class MonitorWaitingTimeProbeExtension extends AbstractEventProbeExtension {

	@Override
	public AbstractEventProbe createExtensionArtifact() {
		return new MonitorWaitingTimeProbe(this);
	}

	@Override
	public Class<? extends AbstractEventProbe> getProbeClass() {
		return MonitorWaitingTimeProbe.class;
	}

	@Override
	public Set<Class<?>> getScopeDependencies() {
		Set<Class<?>> supportedScopes = new HashSet<>();
		supportedScopes.add(SynchronizedScope.class);
		return supportedScopes;
	}

}
