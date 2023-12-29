package lb.kits.utils;

import dev.lone.itemsadder.api.CustomStack;
import lb.kits.main.MainKits;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionsManager {

  private final ConsoleCommandSender console = Bukkit.getConsoleSender();

  public boolean kitExist(String kitName) {
    return MainKits.getPlugin().getConfig().getConfigurationSection("kits." + kitName) != null;
  }

  public void pickKit(Player player, String kit, boolean addCooldown) {
    if (!kitExist(kit)) {
      player.sendMessage("§cKit não encontrado.");
      return;
    }
    if (addCooldown) {
      if (!hasCooldown(player, kit)) {
        long cooldown = MainKits.getPlugin().getConfig().getLong("cooldowns." + kit + "." + player.getUniqueId() + ".millis");
        player.sendMessage(getRemainingTime(cooldown));
        return;
      }
      addCooldown(player, kit);
    }
    try {
      addItems(player, kit);
      player.sendMessage("§aVoce recebeu o kit " + kit + ".");
    } catch (Exception exception) {
      player.sendMessage("§cOcorreu um erro ao pegar o kit " + kit + ".");
    }
  }

  public void giveKit(CommandSender sender, Player target, String kit, boolean addCooldown) {
    if (!kitExist(kit)) {
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

  public void giveKit(Player target, String kit, boolean addCooldown) {
    if (!kitExist(kit)) {
      return;
    }
    if (addCooldown) {
      addCooldown(target, kit);
    }
    try {
      addItems(target, kit);
    } catch (Exception exception) {
      Bukkit.getConsoleSender().sendMessage("§cOcorreu um erro ao dar o kit " + kit + " para " + target.getName() + ".");
    }
  }

  public ItemStack getItemsAdder(String namespace, int amount) {
    CustomStack stack = CustomStack.getInstance(namespace);
    if (stack != null) {
      ItemStack item = stack.getItemStack();
      item.setAmount(amount);
      return item;
    }
    return null;
  }

  public void addItems(Player player, String kit) {
    ConfigurationSection section = MainKits.getPlugin().getConfig().getConfigurationSection("kits." + kit + ".content");

    if (section != null) {
      for (String items : section.getKeys(false)) {
        String itemPath = "kits." + kit + ".content." + items + ".items.";

        String namespace = MainKits.getPlugin().getConfig().getString(itemPath + "itemsadder");
        int amount = MainKits.getPlugin().getConfig().getInt(itemPath + "amount");

        if (namespace != null) {
          ItemStack item = getItemsAdder(namespace, amount);

          if (item != null) {
            addItem(player, item);
            continue;
          }
        }

        String displayName = MainKits.getPlugin().getConfig().getString(itemPath + "displayName");
        List<String> lore = MainKits.getPlugin().getConfig().getStringList(itemPath + "lore");
        Material material = Material.valueOf(Objects.requireNonNull(MainKits.getPlugin().getConfig().getString(itemPath + "material")));
        List<String> enchantments = MainKits.getPlugin().getConfig().getStringList(itemPath + "enchantments");
        Integer model = MainKits.getPlugin().getConfig().getInt(itemPath + "model");

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
          if (displayName != null) {
            meta.setDisplayName(hex(displayName));
          }
          if (!lore.isEmpty()) {
            lore.replaceAll(this::hex);
            meta.setLore(lore);
          }

          meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
          meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
          meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
          meta.setCustomModelData(model);

          if (!enchantments.isEmpty()) {
            for (String enchant : enchantments) {
              String[] enchantData = enchant.split(" ");

              String enchantName = enchantData[0];
              int enchantLevel = enchantData.length == 2 ? Integer.parseInt(enchantData[1]) : 1;
              Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase()));

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

  public void addCooldown(Player player, String name) {
    long cooldown = MainKits.getPlugin().getConfig().getLong("kits." + name + ".cooldown");

    if (cooldown <= 0) {
      System.out.print("Kit cooldown " + name + " does not exist or is less than or equal to zero.");
      return;
    }
    try {
      MainKits.getPlugin().getConfig().set("cooldowns." + name + "." + player.getUniqueId() + ".millis", System.currentTimeMillis() + cooldown);
      MainKits.getPlugin().saveConfig();
    } catch (Exception exception) {
      Bukkit.getConsoleSender().sendMessage(exception.toString());
    }
  }

  public boolean hasCooldown(Player player, String kit) {
    long current = System.currentTimeMillis();
    long millis = MainKits.getPlugin().getConfig().getLong("cooldowns." + kit + "." + player.getUniqueId() + ".millis");
    return current >= millis;
  }

  public String getRemainingTime(long value) {
    long current = System.currentTimeMillis();
    long millis = value - current;

    if (millis <= 0) {
      return "§aVocê já pode pegar o kit.";
    }

    long weeks = TimeUnit.MILLISECONDS.toDays(millis) / 7;
    long days = TimeUnit.MILLISECONDS.toDays(millis) % 7;
    long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;

    StringBuilder remainingTime = new StringBuilder("§cVocê precisa esperar ");

    if (weeks > 0) {
      remainingTime.append("§c").append(weeks).append(" §c").append(weeks == 1 ? "semana" : "semanas").append(" e ");
    }

    if (days > 0) {
      remainingTime.append("§c").append(days).append(" §c").append(days == 1 ? "dia" : "dias").append(" e ");
    }

    if (hours > 0) {
      remainingTime.append("§c").append(hours).append(" §c").append(hours == 1 ? "hora" : "horas").append(" e ");
    }

    if (minutes > 0) {
      remainingTime.append("§c").append(minutes).append(" §c").append(minutes == 1 ? "minuto" : "minutos");
    }

    remainingTime.append(" para pegar o kit.");

    return remainingTime.toString();
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
    StringBuilder buffer = new StringBuilder();

    while (matcher.find()) {
      StringBuilder replacement = new StringBuilder("&x");
      for (char c : matcher.group().substring(1).toCharArray()) {
        replacement.append("&").append(c);
      }
      matcher.appendReplacement(buffer, replacement.toString());
    }
    matcher.appendTail(buffer);

    return ChatColor.translateAlternateColorCodes('&', buffer.toString());
  }
}
