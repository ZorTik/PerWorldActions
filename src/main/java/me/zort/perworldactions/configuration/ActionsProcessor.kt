package me.zort.perworldactions.configuration

import me.zort.perworldactions.action.InvokationData
import me.zort.perworldactions.action.StoredAction
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.plugin.java.JavaPlugin

class ActionsProcessor(val plugin: JavaPlugin): ConfigurationProcessor(plugin, "actions.yml") {

    lateinit var actions: MutableMap<String, StoredAction>

    override fun load(): String? {
        actions = mutableMapOf()
        val actionsSection = config.getConfigurationSection("actions")
        val logger = plugin.logger
        actionsSection.getKeys(false).forEach { actionName ->
            val actionSection = config.getConfigurationSection("actions.$actionName")
            val type = StoredAction.getType(actionSection)
            if(type == null) {
                logger.info("Cannot load action '$actionName'. Unknown action type!")
                return@forEach
            }
            val action = type.factory.create(actionSection)
            if(action is StoredAction) {
                actions[actionName] = action
            }
        }
        return null
    }

    fun invokeAll(p: Player, e: Event) {
        actions.values.forEach {
            try {
                it.invoke(InvokationData(p, e, it.configurationSection))
            } catch(ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}