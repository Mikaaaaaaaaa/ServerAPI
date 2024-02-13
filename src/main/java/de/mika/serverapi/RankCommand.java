package de.mika.serverapi;

import de.mika.serverapi.user.ServerUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        ServerUser serverUser = new ServerUser(player.getUniqueId().toString());

        player.sendMessage("§7Dein Rang: §r§l" + serverUser.getRank());

        return false;
    }
}
