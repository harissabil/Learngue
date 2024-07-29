package com.harissabil.learngue.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    companion object {
        fun fromRoute(route: String): Screen? {
            return Screen::class.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }?.objectInstance
        }
    }

    @Serializable
    data object Home : Screen()

    @Serializable
    data class VocabDetail(
        val scannedTextId: Int,
        val textFromImage: String?
    ) : Screen()

    @Serializable
    data object History : Screen()

    @Serializable
    data object Quiz : Screen()
}