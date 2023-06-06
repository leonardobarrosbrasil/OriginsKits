package lb.kits.commands;

import lb.kits.commands.tab.CommandKitTab;
import lb.kits.main.MainKits;
import lb.kits.utils.FunctionsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandKit implements CommandExecutor {

  private final FunctionsManager functions = new FunctionsManager();

  public CommandKit(MainKits main, String command) {
    Objects.requireNonNull(main.getCommand(command)).setExecutor(this);
    Objects.requireNonNull(main.getCommand(command)).setTabCompleter(new CommandKitTab());
  }

  @Override
  public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
    if (args.length == 0) {
      sender.sendMessage("§cVocê pode usar /comandos para aprender a usar o comando.");
      return true;
    }
    if (args.length == 2) {
      args2(sender, args);
      return true;
    }
    if (args.length == 3) {
      args3(sender, args);
      return true;
    }
    sender.sendMessage("§cArgumentos inválidos.");
    return true;
  }

  private void args1(CommandSender sender, String[] args) {
    if (!sender.hasPermission("kits.admin.reload")) {
      sender.sendMessage("§cVocê não tem permissão para fazer isto.");
      return;
    }
    try {
      MainKits.getPlugin().reloadConfig();
      sender.sendMessage("§aConfigurações reiniciadas com sucesso.");
    } catch (Exception error) {
      sender.sendMessage("§cOcorreu um erro ao reiniciar as configurações.");
    }
  }

  private void args2(CommandSender sender, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cVocê nâo pode usar este comando no console.");
      return;
    }
    Player player = (Player) sender;

    switch (args[0]) {
      case "pegar": {
        String kit = args[1];

        if (!player.hasPermission("kits." + kit)) {
          player.sendMessage("§cVoce não tem permissão");
          return;
        }
        functions.pickKit(player, kit, true);
        break;
      }
    }
  }

  private void args3(CommandSender sender, String[] args) {
    switch (args[0]) {
      case "dar": {
        String kit = args[1];
        Player target = Bukkit.getPlayerExact(args[2]);

        if (target == null) {
          sender.sendMessage("§cJogador não encontrado.");
          return;
        }
        functions.giveKit(sender, target, kit, false);
        break;
      }
      case "resetar": {
        String kit = args[1];
        Player target = Bukkit.getPlayerExact(args[2]);

        if (target == null) {
          sender.sendMessage("§cJogador não encontrado.");
          return;
        }
        MainKits.getPlugin().getConfig().set("cooldowns." + kit + "." + target.getUniqueId() + ".millis", null);
        MainKits.getPlugin().saveConfig();
        sender.sendMessage("§aVocê resetou o tempo de recarga do kit " + kit + " de " + target.getName() + ".");
        break;
      }
    }
  }
}