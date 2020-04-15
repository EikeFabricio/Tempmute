package br.com.eike.tempmute.cache.objects;

import br.com.eike.tempmute.cache.MuteInfo;
import br.com.eike.tempmute.database.MyDB;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MutePlayer {

    private final UUID playerId;
    private long time;

    public MutePlayer(UUID playerId) {
        this.playerId = playerId;
        this.time = 0L;
    }

    public MutePlayer(UUID playerId, long time) {
        this.playerId = playerId;
        this.time = time;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getRemainingTime() {
        return getTime() - System.currentTimeMillis();
    }

    public boolean alwaysMuted() {
        return getRemainingTime() > 0L;
    }

    public static MutePlayer of(Player player, MyDB database) {
        MuteInfo muteInfo = MuteInfo.getInstance();
        MutePlayer mutePlayer = new MutePlayer(player.getUniqueId());

        if (muteInfo.exists(player)) return muteInfo.from(player);
        else {
            List<Object> result = database.query(String.format("SELECT `muteTime` FROM `mutedUsers` WHERE `userId` = '%s'",
                    mutePlayer.getPlayerId().toString()));

            if (!result.isEmpty()) {
                long muteTime = Long.parseLong(result.get(0).toString());
                mutePlayer.setTime(muteTime);
            }
        }

        if (mutePlayer.getTime() > 0L) muteInfo.getMutedPlayers().add(mutePlayer);

        return mutePlayer;
    }

}
