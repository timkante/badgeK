package dev.timkante.badgeK.common.modules

import io.ktor.routing.*

interface Module {
    fun Routing.register(): Unit
}