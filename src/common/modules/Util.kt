package dev.timkante.badgeK.common.modules

import io.ktor.routing.*

fun Routing.registerModule(module: Module) {
    module.run { register() }
}