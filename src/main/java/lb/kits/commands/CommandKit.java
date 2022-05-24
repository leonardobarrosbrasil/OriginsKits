package lb.kits.commands;

import lb.kits.commands.tab.CommandKitTab;
import lb.kits.main.MainKits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandKit implements CommandExecutor {

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
        sender.sendMessage("§cArgumentos inválidos.");
        return true;
    }

    private void args2(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVocê nâo pode usar este comando no console.");
            return;
        }
        Player player = (Player) sender;
        switch (args[0]) {
            case "pegar":
                switch (args[1]) {
                    case "comida":
                        if (!player.hasPermission("lb.player.kit." + args[1].toLowerCase())) {
                            player.sendMessage("§cVocê não tem permissão para fazer isto.");
                            return;
                        }
                        MainKits.getPlugin().getFunctions().giveKit(player, args[1].toLowerCase(), 600000, true);
                        break;
                    case "inicial":
                        if (!player.hasPermission("lb.player.kit." + args[1].toLowerCase())) {
                            player.sendMessage("§cVocê não tem permissão para fazer isto.");
                            return;
                        }
                        MainKits.getPlugin().getFunctions().giveKit(player, args[1].toLowerCase(), 86400000, true);
                        break;
                    default:
                        player.sendMessage("§cKit não encontrado.");
                        break;
                }
                break;
            case "dar":

                break;
            default:
                player.sendMessage("§cArgumentos inválidos.");
                break;
        }
    }
}