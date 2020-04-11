package me.hope.franxxmin.listeners;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.connection.ReconnectEvent;
import org.javacord.api.listener.connection.ReconnectListener;

public class Reconnect implements ReconnectListener {
    @Override
    public void onReconnect(ReconnectEvent event) {
        System.out.println("[OPCODE] Reconnecting API..");
        Main.api = new DiscordApiBuilder().setToken(Main.botdcapitoken).login().join();
        System.out.println("[OPCODE] Successfully reconnected!");
        EmbedBuilder eb = Templates.debugembed();
        if (Main.debug.getBoolean("enabled", false)) {
            eb.setDescription("[OPCODE] Reconnect Notification");
            eb.addField("INFO", "Reconnected successfully!");
            Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb);
            Main.api.updateActivity(ActivityType.LISTENING, Main.api.getServers().size() + " Guilds | mp> help");
        }

    }
}
