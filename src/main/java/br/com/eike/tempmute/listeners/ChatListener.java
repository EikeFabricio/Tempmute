package br.com.eike.tempmute.listeners;

import br.com.eike.tempmute.cache.MuteInfo;
import br.com.eike.tempmute.cache.objects.MutePlayer;
import br.com.eike.tempmute.database.MyDB;
import br.com.eike.tempmute.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final MyDB myDB;

    public ChatListener(MyDB myDB) {
        this.myDB = myDB;
    }

    private MyDB getMyDB() {
        return myDB;
    }

    @EventHandler
    private void when(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MutePlayer mutePlayer = MutePlayer.of(player, getMyDB());

        if (mutePlayer.alwaysMuted()) {
            player.sendMessage(new String[] {
                    "§cYou're muted!",
                    "§cRemaining time: " + TimeUtils.format(mutePlayer.getRemainingTime())
            });

            event.setCancelled(true);
        } else {
            MuteInfo.getInstance().getMutedPlayers().remove(mutePlayer);
            getMyDB().update("DELETE FROM `mutedUsers` WHERE `userId` = '" + mutePlayer.getPlayerId().toString() + "'");
        }
    }
}
