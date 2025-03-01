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

package org.springframework.boot.build;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import dev.adamko.dokkatoo.DokkatooExtension;
import dev.adamko.dokkatoo.formats.DokkatooHtmlPlugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.jetbrains.kotlin.gradle.dsl.JvmTarget;
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions;
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion;
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile;

/**
 * Conventions that are applied in the presence of the {@code org.jetbrains.kotlin.jvm}
 * plugin. When the plugin is applied:
 *
 * <ul>
 * <li>{@link KotlinCompile} tasks are configured to:
 * <ul>
 * <li>Use {@code apiVersion} and {@code languageVersion} 2.1.
 * <li>Use {@code jvmTarget} 17.
 * <li>Treat all warnings as errors
 * <li>Suppress version warnings
 * </ul>
 * </ul>
 *
 * <p/>
 *
 * @author Andy Wilkinson
 */
class KotlinConventions {

	void apply(Project project) {
		project.getPlugins().withId("org.jetbrains.kotlin.jvm", (plugin) -> {
			project.getTasks().withType(KotlinCompile.class, this::configure);
			project.getPlugins().withType(DokkatooHtmlPlugin.class, (dokkatooPlugin) -> configureDokkatoo(project));
		});
	}

	private void configure(KotlinCompile compile) {
		KotlinJvmCompilerOptions kotlinOptions = compile.getCompilerOptions();
		kotlinOptions.getApiVersion().set(KotlinVersion.KOTLIN_2_1);
		kotlinOptions.getLanguageVersion().set(KotlinVersion.KOTLIN_2_1);
		kotlinOptions.getJvmTarget().set(JvmTarget.JVM_17);
		kotlinOptions.getAllWarningsAsErrors().set(true);
		List<String> freeCompilerArgs = new ArrayList<>(kotlinOptions.getFreeCompilerArgs().get());
		freeCompilerArgs.add("-Xsuppress-version-warnings");
		kotlinOptions.getFreeCompilerArgs().set(freeCompilerArgs);
	}

	private void configureDokkatoo(Project project) {
		DokkatooExtension dokkatoo = project.getExtensions().getByType(DokkatooExtension.class);
		dokkatoo.getDokkatooSourceSets().configureEach((sourceSet) -> {
			if (SourceSet.MAIN_SOURCE_SET_NAME.equals(sourceSet.getName())) {
				sourceSet.getSourceRoots().setFrom(project.file("src/main/kotlin"));
				sourceSet.getClasspath()
					.from(project.getExtensions()
						.getByType(SourceSetContainer.class)
						.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
						.getOutput());
				sourceSet.getExternalDocumentationLinks().create("spring-boot-javadoc", (link) -> {
					link.getUrl().set(URI.create("https://docs.spring.io/spring-boot/api/java/"));
					link.getPackageListUrl()
						.set(URI.create("https://docs.spring.io/spring-boot/api/java/element-list"));
				});
				sourceSet.getExternalDocumentationLinks().create("spring-framework-javadoc", (link) -> {
					String url = "https://docs.spring.io/spring-framework/docs/%s/javadoc-api/"
						.formatted(project.property("springFrameworkVersion"));
					link.getUrl().set(URI.create(url));
					link.getPackageListUrl().set(URI.create(url + "/element-list"));
				});
			}
		});
	}

}
