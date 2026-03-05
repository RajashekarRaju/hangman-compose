import java.util.Properties
import org.gradle.api.provider.Provider

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

val sentryDsnProvider: Provider<String> = providers.gradleProperty("SENTRY_DSN")
    .orElse(providers.environmentVariable("SENTRY_DSN"))
    .orElse(localProperties.getProperty("SENTRY_DSN") ?: "")

extra["hangmanSentryDsnProvider"] = sentryDsnProvider
extra["hangmanSentryDsn"] = sentryDsnProvider.get()
