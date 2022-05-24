package lb.kits.commands.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandKitTab implements TabCompleter {

    private final List<String> args1 = Arrays.asList("pegar");
    private final List<String> args1Admin = Arrays.asList("resetar", "dar");

    private final List<String> args2 = Arrays.asList("comida", "inicial", "teste");
    private final ArrayList<String> kits = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return completions;
        }
        if (args.length == 1) {
            if (sender.hasPermission("lb.admin.kits")) {
                StringUtil.copyPartialMatches(args[0], args1Admin, completions);
            }
            StringUtil.copyPartialMatches(args[0], args1, completions);
        }
        if (args.length == 2) {
            Player player = (Player) sender;
            kits.clear();
            for (String kitsName : args2) {
                if (player.hasPermission("lb.player.kit." + kitsName)) kits.add(kitsName);
            }
            StringUtil.copyPartialMatches(args[1], kits, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
