package me.zort.perworldactions.action

import org.bukkit.configuration.ConfigurationSection

enum class EventType(val factory: ActionFactory<ConfigurationSection>) {

    INTERACT_AT_PLAYER({me.zort.perworldactions.action.interact.InteractAtEntityAction(it, "Player")})

}