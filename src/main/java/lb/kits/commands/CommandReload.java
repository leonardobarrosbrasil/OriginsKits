package lb.kits.commands;

import lb.kits.main.MainKits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class CommandReload implements CommandExecutor {

  public CommandReload(MainKits main, String command) {
    Objects.requireNonNull(main.getCommand(command)).setExecutor(this);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
    if(!sender.hasPermission("kits.admin.reload")) {
      sender.sendMessage("§cVocê não tem permissão para fazer isto.");
      return false;
    }
    try {
      MainKits.getPlugin().reloadConfig();
      sender.sendMessage("§aConfigurações do plugin " + MainKits.getPlugin().getName() + " reiniciadas com sucesso.");
    } catch (Exception error) {
      sender.sendMessage("§cOcorreu um erro ao reiniciar as configurações.");
    }
    return false;
  }
}