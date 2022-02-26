package me.zort.perworldactions.action.interact

import me.zort.perworldactions.action.EventAction
import me.zort.perworldactions.action.InvokationData
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class InteractAtEntityAction(configurationSection: ConfigurationSection, private val targetClassSimple: String): EventAction(configurationSection, PlayerInteractAtEntityEvent::class.java) {

    override fun verify(data: InvokationData): Boolean {
        return super.verify(data) && (data.event as PlayerInteractAtEntityEvent).rightClicked.let {
            if(targetClassSimple.equals("Player", true)) {
                it is Player
            } else it is LivingEntity
        }
    }

}