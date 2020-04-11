package me.hope.franxxmin.listeners;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.onStart.CooldownManager;
import me.hope.franxxmin.utils.DBL;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.listener.server.ServerLeaveListener;

import java.util.prefs.Preferences;


public class onServerJoin implements ServerJoinListener, ServerLeaveListener {
    public static Preferences pref = Preferences.userNodeForPackage(onServerJoin.class).node("informational");

    @Override
    public void onServerJoin(ServerJoinEvent event) {
        Preferences pref = ServerHashmaps.blacklist;
        if (pref.getBoolean(event.getServer().getIdAsString(), false)) {
            event.getServer().getSystemChannel().get().sendMessage("Oops!\n \nYour Guild is blacklisted from using this Bot! Contact Hope#1445 on Discord or per E-Mail at hopedevmail@yahoo.com");
            event.getServer().leave();
            EmbedBuilder eb = Templates.infoembed();
            eb.setDescription("Blacklisted Guild tried to add Bot");
            eb.addField("Guild Name", event.getServer().getName());
            eb.addInlineField("Guild ID", event.getServer().getIdAsString());
            eb.addField("Owner", event.getServer().getOwner().getDiscriminatedName());
            eb.addInlineField("Owner ID", event.getServer().getOwner().getIdAsString());
            eb.addField("Member count", event.getServer().getMemberCount() + "");
            eb.setThumbnail(Main.api.getYourself().getAvatar());


            Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb);
            return;
        }
        EmbedBuilder eb = Templates.infoembed();
        eb.setDescription("Bot has been added to a new Guild!");
        eb.addField("Guild Name", event.getServer().getName());
        eb.addInlineField("Guild ID", event.getServer().getIdAsString());
        eb.addField("Owner", event.getServer().getOwner().getDiscriminatedName());
        eb.addInlineField("Owner ID", event.getServer().getOwner().getIdAsString());
        eb.addField("Member count", event.getServer().getMemberCount() + "");
        eb.setThumbnail(Main.api.getYourself().getAvatar());
        boolean canmsg = false;
        for (TextChannel channel : event.getServer().getTextChannels()) {
            if (channel.canYouWrite()) {
                canmsg = true;
                channel.sendMessage(Templates.ServerJoinEmbed());

                break;
            }
        }


        Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb);
        if (!canmsg) {
            Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage("Warning: Bot doesn't have access to any channels, suspected Botfarm Server.");

        }

        pref.putInt("count", pref.getInt("count", 0) + 1);
        System.out.println("Guild count Update: " + pref.getInt("count", 0) + "+1");
        CooldownManager.updateServer(event.getServer());
        Main.api.updateActivity(ActivityType.LISTENING, Main.api.getServers().size() + " Servers | mp> help");
        if (!Main.localmode) {
            DBL.dbl.setStats(Main.api.getServers().size());
        }

    }

    @Override
    public void onServerLeave(ServerLeaveEvent event) {
        EmbedBuilder eb = Templates.infoembed();
        eb.setDescription("Bot has been removed from a Guild!");
        eb.addField("Guild Name", event.getServer().getName());
        eb.addInlineField("Guild ID", event.getServer().getIdAsString());
        eb.setThumbnail(Main.api.getYourself().getAvatar());

        // Remove Guild from cooldown refereer (might not be needed)
        // Main.cooldownref.remove(event.getServer().getIdAsString());


        Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb);
        pref.putInt("count", pref.getInt("count", 0) - 1);
        System.out.println("Guild count Update: " + pref.getInt("count", 0) + "-1");

        CooldownManager.updateServer(event.getServer());
        Main.api.updateActivity(ActivityType.LISTENING, Main.api.getServers().size() + " Servers | mp> help");
        if (!Main.localmode) {
            DBL.dbl.setStats(Main.api.getServers().size());
        }


        //Remove Guild from Array

    }


}
