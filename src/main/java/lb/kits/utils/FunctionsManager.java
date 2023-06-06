package lb.kits.utils;

import lb.kits.main.MainKits;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionsManager {

  public FileConfiguration getConfig() {
    return MainKits.getPlugin().getConfig();
  }

  public void pickKit(Player player, String kit, boolean addCooldown) {
    if (getConfig().getConfigurationSection("kits." + kit) == null) {
      player.sendMessage("§cKit não encontrado.");
      return;
    }
    if (addCooldown) {
      if (!hasCooldown(player, kit)) {
        player.sendMessage(getRemainingTime(getConfig().getLong("cooldowns." + kit + "." + player.getUniqueId() + ".millis")));
        return;
      }
      addCooldown(player, kit);
    }
    try {
      addItems(player, kit);
      player.sendMessage("§aVoce recebeu o kit " + kit + ".");
    } catch (Exception exception) {
      player.sendMessage("§cOcorreu um erro ao enviar o kit " + kit);
    }
  }

  public void giveKit(CommandSender sender, Player target, String kit, boolean addCooldown) {
    if (getConfig().getConfigurationSection("kits." + kit) == null) {
      sender.sendMessage("§cKit não encontrado.");
      return;
    }
    if (addCooldown) {
      addCooldown(target, kit);
    }
    try {
      addItems(target, kit);
      target.sendMessage("§aVoce recebeu o kit " + kit + ".");
      sender.sendMessage("§aVocê deu o kit " + kit + " para " + target.getName() + ".");
    } catch (Exception exception) {
      sender.sendMessage("§cOcorreu um erro ao dar o kit " + kit + " para " + target.getName() + ".");
    }
  }

  private final ConsoleCommandSender console = Bukkit.getConsoleSender();

  public void addItems(Player player, String kit) {
    ConfigurationSection section = getConfig().getConfigurationSection("kits." + kit + ".content");

    if (section != null) {
      for (String items : section.getKeys(false)) {
        String displayName = getConfig().getString("kits." + kit + ".content." + items + ".items.displayName");
        ItemStack item = new ItemStack(Material.valueOf(getConfig().getString("kits." + kit + ".content." + items + ".items.material")), getConfig().getInt("kits." + kit + ".content." + items + ".items.amount"));
        List<String> lore = getConfig().getStringList("kits." + kit + ".content." + items + ".items.lore");
        Integer model = getConfig().getInt("kits." + kit + ".content." + items + ".items.model");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
          meta.setDisplayName(hex(displayName));
          lore.replaceAll(s -> hex(s));
          meta.setLore(lore);
          meta.setCustomModelData(model);

          console.sendMessage("passou");
          if (getConfig().getStringList("kits." + kit + ".content." + items + ".items.enchantments").size() > 0) {
            for (String enchant : getConfig().getStringList("kits." + kit + ".content." + items + ".items.enchantments")) {
              if (enchant != null) {
                String enchantName = enchant.split(":")[0];
                int enchantLevel = Integer.parseInt(enchant.split(":")[1]);
                Enchantment enchantment = Enchantment.getByName(enchantName);

                if (enchantment != null) {
                  meta.addEnchant(enchantment, enchantLevel, true);
                }
              }
            }
          }
          item.setItemMeta(meta);
          addItem(player, item);
        }
      }
    }
  }

  public void addCooldown(Player player, String name) {
    long cooldown = getConfig().getLong("kits." + name + ".cooldown");

    if (cooldown <= 0) {
      System.out.print("Kit cooldown " + name + " does not exist or is less than or equal to zero.");
      return;
    }
    try {
      getConfig().set("cooldowns." + name + "." + player.getUniqueId() + ".millis", System.currentTimeMillis() + cooldown);
      MainKits.getPlugin().saveConfig();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public boolean hasCooldown(Player player, String name) {
    long current = System.currentTimeMillis();
    long millis = getConfig().getLong("cooldowns." + name + "." + player.getUniqueId() + ".millis");
    return current >= millis;
  }

  public String getRemainingTime(long value) {
    long current = System.currentTimeMillis();
    long millis = value - current;

    long minutes = 0;
    long hours = 0;
    long days = 0;
    long weeks = 0;

    if (millis <= 0) {
      return "§aAguardando atualização";
    }
    if (millis < 60000) {
      long seconds = 0;
      while (millis > 1000) {
        millis -= 1000;
        seconds++;
      }
      return "§cVocê precisa esperar " + seconds + " §c" + ((seconds == 1) ? "segundo " : "segundos " + "para pegar o kit.");
    }
    while (millis > 1000) {
      millis -= 1000 * 60;
      minutes++;
    }
    while (minutes > 60) {
      minutes -= 60;
      hours++;
    }
    while (hours > 24) {
      hours -= 24;
      days++;
    }
    while (days > 7) {
      days -= 7;
      weeks++;
    }
    String weeklist = "";
    if (weeks >= 1) {
      weeklist = "§c" + weeks + " §c" + ((weeks == 1) ? "§csemana e " : "§csemanas e ");
    }

    String daylist = "";
    if (days >= 1) {
      daylist = "§c" + days + " §c" + ((days == 1) ? "§dia e " : "§cdias e ");
    }

    String hourlist = "";
    if (hours >= 1) {
      hourlist = "§c" + hours + " §c" + ((hours == 1) ? "§chora e " : "§choras e ");
    }
    String minutelist = "";
    if (minutes >= 1) {
      minutelist = "§c" + minutes + " §c" + ((minutes == 1) ? "§cminuto " : "§cminutos");
    }
    return "§cVocê precisa esperar " + weeklist + daylist + hourlist + minutelist + " para pegar o kit.";
  }

  public void addItem(Player player, ItemStack item) {
    if (player.getInventory().firstEmpty() == -1) {
      player.getWorld().dropItem(player.getLocation(), item);
    } else {
      player.getInventory().addItem(item);
    }
  }

  public String hex(String message) {
    Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    Matcher matcher = pattern.matcher(message);
    while (matcher.find()) {
      String hexCode = message.substring(matcher.start(), matcher.end());
      String replaceSharp = hexCode.replace('#', 'x');

      char[] ch = replaceSharp.toCharArray();
      StringBuilder builder = new StringBuilder();
      for (char c : ch) {
        builder.append("&").append(c);
      }
      message = message.replace(hexCode, builder.toString());
      matcher = pattern.matcher(message);
    }
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}
