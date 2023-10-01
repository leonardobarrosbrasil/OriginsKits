package lb.kits.main;

import lb.kits.commands.CommandKit;
import lb.kits.utils.FunctionsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainKits extends JavaPlugin {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private static MainKits instance;

    private FunctionsManager functions;

    public static MainKits getPlugin() {
        return instance;
    }

    public void registerCommands() {
        new CommandKit(this, "conjunto");
        console.sendMessage("§a" + getPlugin().getName() + ": Comandos carregados com sucesso.");
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        functions = new FunctionsManager();
        registerCommands();
        console.sendMessage("§a" + getPlugin().getName() + ": Plugin habilitado com sucesso.");
    }

    @Override
    public void onDisable() {
        saveConfig();
        console.sendMessage("§c" + getPlugin().getName() + ": Plugin desabilitado com sucesso.");
    }

    public FunctionsManager getFunctions() {
        return functions;
    }

}
