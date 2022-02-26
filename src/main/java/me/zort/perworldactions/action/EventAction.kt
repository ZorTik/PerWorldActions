package me.zort.perworldactions.action

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.Event

abstract class EventAction(configurationSection: ConfigurationSection, private val eventClass: Class<out Event>): StoredAction(configurationSection) {

    override fun verify(data: InvokationData): Boolean {
        return super.verify(data) && data.event.javaClass == eventClass
    }

}