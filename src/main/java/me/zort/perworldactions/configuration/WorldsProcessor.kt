package me.zort.perworldactions.configuration

import me.zort.perworldactions.action.StoredAction
import org.bukkit.plugin.java.JavaPlugin

class WorldsProcessor(val plugin: JavaPlugin, val configuration: Configuration): ConfigurationProcessor(plugin, "worlds.yml") {

    private lateinit var actions: MutableMap<String, MutableList<StoredAction>>

    override fun load(): String? {
        return try {
            val worlds = config.getConfigurationSection("worlds")
            val actionsProcessor = configuration.getProcessor(ActionsProcessor::class.java)?: return "Actions processor is not loaded."
            actions = mutableMapOf(
                *worlds.getKeys(false).map { worldName ->
                    val world = config.getConfigurationSection("worlds.$worldName")
                    val actionList = world.getStringList("actions")
                    Pair(worldName, actionList.mapNotNull {
                        actionsProcessor.actions[it]
                    }.toMutableList())
                }.toTypedArray()
            )
            null
        } catch(ex: Exception) {
            ex.message
        }
    }

    fun getActions(worldName: String): MutableList<StoredAction> {
        return actions.getOrDefault(worldName, mutableListOf())
    }

}