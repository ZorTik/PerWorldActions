package me.zort.perworldactions.configuration

import org.bukkit.plugin.java.JavaPlugin

class ConfigProcessor(plugin: JavaPlugin): ConfigurationProcessor(plugin, "config.yml") {

    override fun load(): String? {
        return null
    }

}