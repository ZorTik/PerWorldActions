package me.zort.perworldactions

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class GeneralCommandExecutor: CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(sender.hasPermission("perworldactions.admin")) {
            if(args.size == 1 && args[0].equals("reload", true)) {
                sender.sendMessage("§e§l[!] §eReloaduji plugin...")
                if(PerWorldActions.INSTANCE.configuration.reloadAll()) {
                    sender.sendMessage("§6§l[!] §6Plugin uspesne reloadnut!")
                } else {
                    sender.sendMessage("§c§l[!] §cNekde se bohuzel stala chyba!")
                }
            } else {
                sender.sendMessage("§e§lPER WORLD ACTIONS")
                sender.sendMessage("§6§l* §f/pwa §areload §8(§cReloadne plugin§8)")
            }
        }
        return true
    }

}