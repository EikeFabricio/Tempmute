package br.com.eike.tempmute.commands;

import br.com.eike.tempmute.cache.MuteInfo;
import br.com.eike.tempmute.cache.objects.MutePlayer;
import br.com.eike.tempmute.database.MyDB;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {

    private final MyDB myDB;

    public UnmuteCommand(MyDB myDB) {
        this.myDB = myDB;
    }

    public MyDB getMyDB() {
        return myDB;
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String lb, String[] args) {
        if (args.length == 0) {
            s.sendMessage("§cCorrect usage: /unmute (player)");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        MutePlayer mutePlayer = MutePlayer.of(target, getMyDB());

        if (mutePlayer.alwaysMuted()) {
            MuteInfo.getInstance().getMutedPlayers().remove(mutePlayer);
        } else {
            s.sendMessage("§cThat player isn't muted!");
            return true;
        }

        target.sendMessage("§aYeah! You can talk again.");
        s.sendMessage("§aSucessfuly unmuted");

        getMyDB().update("DELETE FROM `mutedUsers` WHERE `userId` = '" + mutePlayer.getPlayerId().toString() + "'");

        return false;
    }
}
