package br.com.eike.tempmute.commands;

import br.com.eike.tempmute.cache.MuteInfo;
import br.com.eike.tempmute.cache.objects.MutePlayer;
import br.com.eike.tempmute.database.MyDB;
import br.com.eike.tempmute.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempmuteCommand implements CommandExecutor {

    private final MyDB myDB;

    public TempmuteCommand(MyDB myDB) {
        this.myDB = myDB;
    }

    private MyDB getMyDB() {
        return myDB;
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String lb, String[] args) {
        if (args.length < 2) {
            s.sendMessage("§cCorrect usage: /tempmute (player) (time)");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            s.sendMessage("§cThe target player isn't online.");
            return true;
        }

        long formattedTime = System.currentTimeMillis() + TimeUtils.millisFromString(args[1]);

        if (formattedTime == -1) {
            s.sendMessage("§cHey! Format the time correctly.");
            return true;
        }

        MutePlayer player = MutePlayer.of(target, getMyDB());

        if (player.alwaysMuted()) {
            s.sendMessage("§cThe target player always muted!");
            return true;
        }

        player.setTime(formattedTime);

        MuteInfo.getInstance().getMutedPlayers().add(player);

        target.sendMessage(new String[] {
                "§cYou're muted!",
                "§cAuthor: " + (s instanceof Player ? s.getName() : "CONSOLE"),
                "§cTime: " + TimeUtils.format((formattedTime - System.currentTimeMillis()))
        });

        s.sendMessage("§a" + target.getName() + " was muted!");
        return false;
    }
}
