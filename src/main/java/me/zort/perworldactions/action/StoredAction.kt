package me.zort.perworldactions.action

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

open class StoredAction(val configurationSection: ConfigurationSection): Action {

    companion object {
        @JvmStatic
        fun typeExists(configurationSection: ConfigurationSection): Boolean {
            return getType(configurationSection) != null
        }

        @JvmStatic
        fun getType(configurationSection: ConfigurationSection): EventType? {
            if(!configurationSection.contains("event")) return null
            return EventType.values()
                .firstOrNull {
                    val typeStr = configurationSection.getString("event")
                    it.name.equals(typeStr.replace("-", "_"), true)
                }
        }
    }

    override fun verify(data: InvokationData): Boolean {
        val s = data.section
        val conditionsSection = s.getConfigurationSection("conditions")
        val conditions: List<Condition> = conditionsSection?.getKeys(false)?.mapNotNull {
            val conditionSection = conditionsSection.getConfigurationSection(it)
            if(conditionSection.contains("type")) {
                Condition.values().firstOrNull { condition ->
                    condition.name.equals(conditionSection.getString("type").replace("-", "_"), true)
                }
            } else null
        }?: mutableListOf()
        return conditions.all {
            it.condition.invoke(data)
        }
    }

    override fun invoke(data: InvokationData) {
        val s = data.section
        val tasks: Map<String, Task> = mapOf(
            *(s.getConfigurationSection("actions").getKeys(false)?.mapNotNull {
                val conditionSection = s.getConfigurationSection("actions.$it")
                if(conditionSection.contains("type")) {
                    val task = Task.values().firstOrNull { condition ->
                        condition.name.equals(conditionSection.getString("type").replace("-", "_"), true)
                    }
                    if(task != null) {
                        Pair(it, task)
                    } else null
                } else null
            }?: mutableListOf<Pair<String, Task>>()).toTypedArray()
        )
        tasks.forEach { (id, task) ->
            task.action.invoke(data, s.getConfigurationSection("actions.$id"))
        }
    }

    val name: String
        get() = configurationSection.name

    enum class Condition(val condition: (InvokationData) -> Boolean) {

        CHAT_PLAYER({
            val e = it.event
            val p = it.player
            val s = it.section
            val whoString = if(s.contains("who")) s.getString("who") else "TARGET"
            val who = if(whoString.equals("TARGET", true) || whoString.equals("SELF", true)) {
                whoString.uppercase()
            } else "TARGET"
            val subject = if(who.equals("TARGET", true)) {
                getTargetEntity(e)
            } else p
            val gameMode = if(s.contains("gamemode")) {
                val gameModeString = s.getString("gamemode")
                org.bukkit.GameMode.values()
                    .firstOrNull { gameModeType ->
                        gameModeType.name.equals(gameModeString, true)
                    }
            } else null
            subject != null && gameMode != null && subject is Player && subject.gameMode == gameMode
        });

        companion object {
            fun getTargetEntity(e: Event): Entity? {
                return when(e) {
                    is PlayerInteractEntityEvent -> {
                        e.rightClicked
                    }
                    is EntityDamageByEntityEvent -> {
                        e.entity
                    }
                    else -> null
                }
            }
        }

    }

    enum class Task(val action: (InvokationData, ConfigurationSection) -> Unit) {

        CHAT_PLAYER({ data, s ->
            if(s.contains("message")) {
                val message = org.bukkit.ChatColor.translateAlternateColorCodes('&', s.getString("message"))
                data.player.chat(message)
            }
        }), COMMAND_PLAYER({ data, s ->
            val p = data.player
            var command = if(s.contains("message")) {
                s.getString("message")
            } else if(s.contains("command")) {
                s.getString("command")
            } else null
            if(command != null) {
                command = command!!
                    .replace("%self%", p.name)
                    .replace("%target%", Condition.getTargetEntity(data.event).let { entity ->
                        entity?.name?: "unknown"
                    })
                p.performCommand(command)
            }
        })

    }

}