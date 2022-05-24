package lb.kits.utils;

import lb.kits.main.MainKits;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemManager {

    public final List<ItemStack> foodItems = Collections.singletonList(
            MainKits.getPlugin().getFunctions().createItem(Material.BREAD, (byte) 6, ChatColor.of("#D89D2F") + "Pães"));

    public final List<ItemStack> initialKit = Arrays.asList(
            MainKits.getPlugin().getFunctions().createItem(Material.STONE_SWORD, (byte) 1, ChatColor.of("#8c8b8b") + "Espada de pedra"),
            MainKits.getPlugin().getFunctions().createItem(Material.STONE_PICKAXE, (byte) 1, ChatColor.of("#8c8b8b") + "Picareta de pedra"),
            MainKits.getPlugin().getFunctions().createItem(Material.STONE_AXE, (byte) 1, ChatColor.of("#8c8b8b") + "Machado de pedra"),
            MainKits.getPlugin().getFunctions().createItem(Material.STONE_SHOVEL, (byte) 1, ChatColor.of("#8c8b8b") + "Pá de pedra"),
            MainKits.getPlugin().getFunctions().createItem(Material.GOLDEN_SHOVEL, (byte) 1, ChatColor.of("#a8a8a8") + "Pá da proteção"),
            MainKits.getPlugin().getFunctions().createItem(Material.CRAFTING_TABLE, (byte) 1, ChatColor.of("#9f6037") + "Mesa de trabalho"),
            MainKits.getPlugin().getFunctions().createItem(Material.FURNACE, (byte) 1, ChatColor.of("#737373") + "Fornalha"),
            MainKits.getPlugin().getFunctions().createItem(Material.LEATHER_HELMET, (byte) 1, ChatColor.of("#855435") + "Capacete de couro"),
            MainKits.getPlugin().getFunctions().createItem(Material.LEATHER_CHESTPLATE, (byte) 1, ChatColor.of("#855435") + "Peitoral de couro"),
            MainKits.getPlugin().getFunctions().createItem(Material.LEATHER_LEGGINGS, (byte) 1, ChatColor.of("#855435") + "Calças de couro"),
            MainKits.getPlugin().getFunctions().createItem(Material.LEATHER_BOOTS, (byte) 1, ChatColor.of("#855435") + "Botas de couro"));
}
