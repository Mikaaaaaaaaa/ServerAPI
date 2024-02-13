package de.mika.serverapi.events;

import de.mika.serverapi.user.ServerUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ServerUser serverUser = new ServerUser(event.getPlayer().getUniqueId().toString());

        event.setJoinMessage("§7[§a+§7] §a" + event.getPlayer().getName());
        Player player = event.getPlayer();
        player.sendMessage("§7Willkommen auf dem Server, " + player.getName() + "!");
        player.sendMessage("§7Dein Rang: §r§l" + serverUser.getRank());

        if(serverUser.getBan().getBanTime() > System.currentTimeMillis() ) {
            Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
            Instant banTime = Instant.ofEpochMilli(serverUser.getBan().getBanTime());

            Duration duration = Duration.between(banTime, now);

            long totalMinutes = duration.toMinutes();

            Period period = Period.of(0, 0, (int) totalMinutes / (24 * 60));
            totalMinutes %= 24 * 60;
            int hours = (int) totalMinutes / 60;
            totalMinutes %= 60;

            String banDuration = String.format("%d years, %d months, %d days, %d hours, %d minutes",
                    period.getYears(), period.getMonths(), period.getDays(), hours, totalMinutes);

            player.kickPlayer("§cDu wurdest gebannt! \n§7Grund: §r§l" + serverUser.getBan().getBanReason() + "\n§7Dauer: §r§l" + banDuration);
        }
    }
}
