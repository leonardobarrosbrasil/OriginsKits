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
    switch (args.length) {
      case 1 -> {
        args1(sender, args);
        return true;
      }
      case 2 -> {
        args2(sender, args);
        return true;
      }
      case 3 -> {
        args3(sender, args);
        return true;
      }
      case 4 -> {
        args4(sender, args);
        return true;
      }
      default -> {
        sender.sendMessage("§cArgumentos inválidos.");
        return true;
      }
    }
  }

  private void args1(CommandSender sender, String[] args) {
    if (args[0].equalsIgnoreCase("recarregar")) {
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
    } else {
      sender.sendMessage("§cArgumentos inválidos.");
    }
  }

  private void args2(CommandSender sender, String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage("§cVocê nâo pode usar este comando no console.");
      return;
    }
    if (args[0].equals("pegar")) {

      String kit = args[1];

      if (!player.hasPermission("kits." + kit)) {
        player.sendMessage("§cVocê não tem permissão para fazer isto.");
        return;
      }
      functions.pickKit(player, kit, true);
    } else {
      sender.sendMessage("§cArgumentos inválidos.");
    }
  }

  private void args3(CommandSender sender, String[] args) {
    switch (args[0]) {
      case "dar" -> {
        String kit = args[1];
        Player target = Bukkit.getPlayerExact(args[2]);

        if (target == null) {
          sender.sendMessage("§cJogador não encontrado.");
          return;
        }
        functions.giveKit(sender, target, kit, false);
      }
      case "resetar" -> {
        String kit = args[1];
        Player target = Bukkit.getPlayerExact(args[2]);

        if (target == null) {
          sender.sendMessage("§cJogador não encontrado.");
          return;
        }
        MainKits.getPlugin().getConfig().set("cooldowns." + kit + "." + target.getUniqueId() + ".millis", null);
        MainKits.getPlugin().saveConfig();
        sender.sendMessage("§aVocê resetou o tempo de recarga do conjunto " + kit + " de " + target.getName() + ".");
      }
      default -> {
        sender.sendMessage("§cArgumentos inválidos.");
      }
    }
  }

  private void args4(CommandSender sender, String[] args) {
    switch (args[0]) {
      case "dar" -> {
        String kit = args[1];
        Player target = Bukkit.getPlayerExact(args[2]);
        boolean cooldown = args[3].equalsIgnoreCase("verdadeiro");

        if (target == null) {
          sender.sendMessage("§cJogador não encontrado.");
          return;
        }
        functions.giveKit(sender, target, kit, cooldown);
      }
      default -> {
        sender.sendMessage("§cArgumentos inválidos.");
      }
    }
  }
}