package com.darkbit;

import com.darkbit.security.config.SecurityDarkbitAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de configuración automática de la librería.
 */
class SecurityDarkbitLibraryTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(SecurityDarkbitAutoConfiguration.class));

	@Test
	void autoConfigurationLoads() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(SecurityDarkbitAutoConfiguration.class);
		});
	}

}
