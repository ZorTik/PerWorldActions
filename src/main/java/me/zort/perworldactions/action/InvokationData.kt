package me.zort.perworldactions.action

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.Event

data class InvokationData(val player: Player, val event: Event, val section: ConfigurationSection)
