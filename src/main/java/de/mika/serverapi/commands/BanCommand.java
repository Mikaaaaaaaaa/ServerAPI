package de.mika.serverapi.commands;

import de.mika.serverapi.user.ServerUser;
import de.mika.serverapi.user.rank.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        //test ban command
        Player player = (Player) commandSender;
        ServerUser serverUser = new ServerUser(player.getUniqueId().toString());
        serverUser.getBan().setBanTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        return false;
    }
}
