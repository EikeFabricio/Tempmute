package br.com.eike.tempmute.cache;

import br.com.eike.tempmute.cache.objects.MutePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class MuteInfo {

    private static final MuteInfo muteInfo;

    static {
        muteInfo = new MuteInfo();
    }

    private final List<MutePlayer> mutedPlayers;

    private MuteInfo() {
        this.mutedPlayers = new ArrayList<>();
    }

    public List<MutePlayer> getMutedPlayers() {
        return mutedPlayers;
    }

    public static MuteInfo getInstance() {
        return muteInfo;
    }

    public MutePlayer from(Player player) {
        return getMutedPlayers().stream().filter(it -> it.getPlayerId()
                .equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public boolean exists(Player player) {
        return from(player) != null;
    }
}
