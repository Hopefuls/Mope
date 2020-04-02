package me.hope.franxxmin.listeners;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.onStart.CooldownManager;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.listener.server.ServerLeaveListener;

public class onServerJoin implements ServerJoinListener, ServerLeaveListener {
    @Override
    public void onServerJoin(ServerJoinEvent event) {

        EmbedBuilder eb = Templates.infoembed();
        eb.setDescription("Bot has been added to a new Server!");
        eb.addField("Server Name", event.getServer().getName());
        eb.addInlineField("Server ID", event.getServer().getIdAsString());
        eb.addField("Owner", event.getServer().getOwner().getDiscriminatedName());
        eb.addInlineField("Owner ID", event.getServer().getOwner().getIdAsString());
        eb.addField("Member count", event.getServer().getMemberCount() + "");
        eb.setThumbnail(Main.api.getYourself().getAvatar());


        Main.api.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(eb);


        CooldownManager.updateServer(event.getServer());


    }

    @Override
    public void onServerLeave(ServerLeaveEvent event) {
        EmbedBuilder eb = Templates.infoembed();
        eb.setDescription("Bot has been removed from a Server!");
        eb.addField("Server Name", event.getServer().getName());
        eb.addInlineField("Server ID", event.getServer().getIdAsString());
        eb.setThumbnail(Main.api.getYourself().getAvatar());

        // Remove Server from cooldown refereer (might not be needed)
        // Main.cooldownref.remove(event.getServer().getIdAsString());


        Main.api.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(eb);
        CooldownManager.updateServer(event.getServer());

        //Remove Server from Array

    }


}
