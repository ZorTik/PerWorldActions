package me.zort.perworldactions.configuration

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class ConfigurationProcessor(private val plugin: JavaPlugin, private val fileName: String) {

    lateinit var file: File
    lateinit var config: FileConfiguration

    abstract fun load(): String?

    fun init(): String? {
        if(plugin.getResource(fileName) == null) return "File $fileName does not exist."
        val relativePath = "/${fileName}"
        file = File(plugin.dataFolder.absolutePath + relativePath)
        if(!file.exists()) {
            plugin.saveResource(fileName, false)
        }
        config = YamlConfiguration.loadConfiguration(file)
        config.options().copyDefaults(true)
        save()
        return load()
    }

    fun save() {
        config.save(file)
    }

}