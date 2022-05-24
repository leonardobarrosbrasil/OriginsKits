package lb.kits.utils;

import lb.kits.main.MainKits;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionsManager {

    public boolean getAllowKit(Player player, String kitName) {
        long current = System.currentTimeMillis();
        long millis = getRemainingTime(player, kitName);
        return current >= millis;
    }

    public FileConfiguration getConfig() {
        return MainKits.getPlugin().getConfig();
    }

    public void giveKit(Player player, String kitName, int cooldown, Boolean setTime) {
        FileConfiguration config = MainKits.getPlugin().getConfig();
        if (setTime) {
            if (!MainKits.getPlugin().getFunctions().getAllowKit(player, kitName)) {
                player.sendMessage(getRemainingTime(getRemainingTime(player, kitName)));
                return;
            }
            config.set(kitName + "." + player.getUniqueId() + ".millis", System.currentTimeMillis() + cooldown);
        }
        try {
            MainKits.getPlugin().saveConfig();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        for (ItemStack item : getItems(kitName)) {
            addItem(player, item);
        }
        player.sendMessage("§aVocê recebeu o kit " + kitName + ".");
    }

    public Long getRemainingTime(Player player, String kitName) {
        return MainKits.getPlugin().getConfig().getLong(kitName + "." + player.getUniqueId() + ".millis");
    }

    public List<ItemStack> getItems(String kitName) {
        switch (kitName) {
            case "comida":
                return MainKits.getPlugin().getManager().foodItems;
            case "inicial":
                return MainKits.getPlugin().getManager().initialKit;
        }
        return null;
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
            return "§cVocê precisa esperar " + seconds + " §c" + ((seconds == 1) ? "segundo " : "segundos " + " ¶para pegar o kit.");
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

    public ItemStack createItem(Material material, byte amount, int data, String name, String... lores) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setCustomModelData(data);
        meta.setLore(Arrays.asList(lores));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItem(Material material, byte amount, String name, String... lores) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lores));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItemWithEnchantment(Material material, byte amount, String name, Integer level, Enchantment... enchants) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (byte i = 0; i < enchants.length; i = (byte) (i + 1))
            enchantments.put(enchants[i], level);
        item.addUnsafeEnchantments(enchantments);
        return item;
    }
}
