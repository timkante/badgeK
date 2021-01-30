package dev.timkante.badgeK

import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.util.*

object ApplicationConfig {
    private val config: HoconApplicationConfig by lazy {
        HoconApplicationConfig(ConfigFactory.load())
    }

    private fun getProperty(key: String): String? = System.getenv(key.envCase)
        ?: config.propertyOrNull(key)?.getString()

    fun requireProperty(key: String): String = getProperty(key)
        ?: throw IllegalStateException(
            "Missing application property [key=$key] or env-variable [name=${key.envCase}]"
        )

    private val String.envCase: String get() = toUpperCase().replace(oldValue = ".", newValue = "_")
}
