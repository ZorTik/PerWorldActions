package me.zort.perworldactions.configuration

import me.zort.perworldactions.PerWorldActions

class Configuration(val plugin: PerWorldActions) {

    private var processors: LinkedHashMap<String, ConfigurationProcessor> = linkedMapOf()

    init {
        reloadAll()
    }

    fun reloadAll(): Boolean {
        processors = linkedMapOf(
            Pair("config.yml", ConfigProcessor(plugin)),
            Pair("actions.yml", ActionsProcessor(plugin)),
            Pair("worlds.yml", WorldsProcessor(plugin, this))
        )
        val errorMessages = processors.values
            .mapNotNull { it.init() }
        return if(errorMessages.isNotEmpty()) {
            val logger = plugin.logger
            logger.info("There were several errors:")
            errorMessages.forEach {
                logger.info("- $it")
            }
            false
        } else true
    }

    fun <T: ConfigurationProcessor> getProcessor(clazz: Class<T>): T? {
        return processors.values
            .firstOrNull {
                it.javaClass == clazz
            } as T?
    }

}