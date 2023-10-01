package lb.kits.commands.tab;

import lb.kits.main.MainKits;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandKitTab implements TabCompleter {

  private final ConsoleCommandSender console = Bukkit.getConsoleSender();
  Configuration config = MainKits.getPlugin().getConfig();

  private final List<String> args1 = Arrays.asList("pegar");
  private final List<String> args1Admin = Arrays.asList("resetar", "dar", "recarregar");

  private final ArrayList<String> kits = new ArrayList<>();
  private final ArrayList<String> players = new ArrayList<>();

  private final List<String> args2Admin = Arrays.asList("verdadeiro", "falso");

  @Override
  public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
    final List<String> completions = new ArrayList<>();
    if (!(sender instanceof Player)) {
      return completions;
    }
    switch (args.length) {
      case 1 -> {
        if (sender.hasPermission("lb.admin.kits")) {
          StringUtil.copyPartialMatches(args[0], args1Admin, completions);
        }
        StringUtil.copyPartialMatches(args[0], args1, completions);
      }
      case 2 -> {
        switch (args[0]) {
          case "dar", "resetar", "pegar" -> {
            ConfigurationSection section = MainKits.getPlugin().getConfig().getConfigurationSection("kits");
            kits.clear();

            if (section != null) {
              for (String kit : section.getKeys(false)) {
                if (sender.hasPermission("kits." + kit)) {
                  kits.add(kit);
                }
              }
            }
            StringUtil.copyPartialMatches(args[1], kits, completions);
          }
        }
      }
      case 3 -> {
        switch (args[0]) {
          case "dar", "resetar" -> {
            players.clear();
            Bukkit.getOnlinePlayers().forEach(p -> {
              players.add(p.getName());
            });
            StringUtil.copyPartialMatches(args[2], players, completions);
          }
        }
      }
      case 4 -> {
        if (args[0].equalsIgnoreCase("dar")) {
          StringUtil.copyPartialMatches(args[3], args2Admin, completions);
        }
      }
    }
    Collections.sort(completions);
    return completions;
  }
}
