package me.zort.perworldactions

import me.zort.perworldactions.configuration.ActionsProcessor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class PerWorldActionsListener(private val plugin: PerWorldActions): Listener {

    private val actionsProcessor: ActionsProcessor? = plugin.configuration.getProcessor(ActionsProcessor::class.java)

    @EventHandler
    fun onInteract(e: PlayerInteractAtEntityEvent) {
        actionsProcessor?.invokeAll(e.player, e)
    }

}