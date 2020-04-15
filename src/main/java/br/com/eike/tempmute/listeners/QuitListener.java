package br.com.eike.tempmute.listeners;

import br.com.eike.tempmute.cache.MuteInfo;
import br.com.eike.tempmute.cache.objects.MutePlayer;
import br.com.eike.tempmute.database.MyDB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final MyDB myDB;

    public QuitListener(MyDB myDB) {
        this.myDB = myDB;
    }

    private MyDB getMyDB() {
        return myDB;
    }

    @EventHandler
    void when(PlayerQuitEvent event) {
        MutePlayer player = MutePlayer.of(event.getPlayer(), getMyDB());

        if (player.alwaysMuted() && MuteInfo.getInstance().exists(event.getPlayer())) {
            getMyDB().update("INSERT INTO `mutedUsers` VALUES ('" + event.getPlayer().getUniqueId().toString() + "', '" +
                    player.getTime() + "');");
        }

    }
}
