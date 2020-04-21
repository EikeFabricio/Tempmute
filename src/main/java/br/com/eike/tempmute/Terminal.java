package br.com.eike.tempmute;

import br.com.eike.tempmute.cache.MuteInfo;
import br.com.eike.tempmute.commands.TempmuteCommand;
import br.com.eike.tempmute.commands.UnmuteCommand;
import br.com.eike.tempmute.database.MyDB;
import br.com.eike.tempmute.listeners.ChatListener;
import br.com.eike.tempmute.listeners.QuitListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Terminal extends JavaPlugin {

    private MyDB myDB;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        myDB = new MyDB(getDataFolder());
        // SQLite doesn't support Long type
        myDB.update("CREATE TABLE IF NOT EXISTS `mutedUsers` " +
                "(`userId` CHAR(36) NOT NULL, `muteTime`INTEGER NOT NULL, PRIMARY KEY (userId));");

        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {
        MuteInfo.getInstance().getMutedPlayers().forEach((mp) -> {
            if (myDB.query("SELECT * FROM `mutedUsers` WHERE `userId` = '" + mp.getPlayerId().toString() + "'").isEmpty())
            myDB.update("INSERT INTO `mutedUsers` VALUES ('" + mp.getPlayerId().toString() + "', '" + mp.getTime() + "');");
        });

        myDB.close();
    }

    private void registerListener() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ChatListener(myDB), this);
        pm.registerEvents(new QuitListener(myDB), this);
    }

    private void registerCommands() {
        getCommand("tempmute").setExecutor(new TempmuteCommand(myDB));
        getCommand("unmute").setExecutor(new UnmuteCommand(myDB));
    }
}
