package lb.kits.main;

import lb.kits.commands.CommandKit;
import lb.kits.utils.FunctionsManager;
import lb.kits.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainKits extends JavaPlugin {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private static MainKits instance;

    private ItemManager manager;
    private FunctionsManager functions;

    public static MainKits getPlugin() {
        return instance;
    }

    public void registerEvents() {
        //getServer().getPluginManager().registerEvents(new ListenerManager(), this);
        console.sendMessage("§aLBKits: Eventos carregados com sucesso.");
    }

    public void registerCommands() {
        CommandKit kit = new CommandKit(this, "kit");
        console.sendMessage("§aLBKits: Comandos carregados com sucesso.");
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        functions = new FunctionsManager();
        manager = new ItemManager();
        registerCommands();
        registerEvents();
        console.sendMessage("§aLBKits: Plugin habilitado com sucesso.");
    }

    @Override
    public void onDisable() {
        saveConfig();
        console.sendMessage("§cLBKits: Plugin desabilitado com sucesso.");
    }


    public FunctionsManager getFunctions() {
        return functions;
    }

    public ItemManager getManager() {
        return manager;
    }
}
