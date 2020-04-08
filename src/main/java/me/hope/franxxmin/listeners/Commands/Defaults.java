package me.hope.franxxmin.listeners.Commands;


import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.utils.TimestampResolver;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.sql.Timestamp;
import java.util.prefs.Preferences;


public class Defaults {
    public static MessageCreateEvent event;


    public Defaults(MessageCreateEvent event) {
        Defaults.event = event;
    }

    public static void usercount() {
        int all = event.getServer().get().getMemberCount();
        int botcount = 0;
        int usercount = 0;
        int offline = 0;
        int online = 0;
        int dnd = 0;
        int away = 0;
        for (User x : event.getServer().get().getMembers()) {
            if (x.isBot()) {
                botcount++;
            }


            if (!x.isBot()) {
                usercount++;
                if (x.getStatus().equals(UserStatus.DO_NOT_DISTURB)) {
                    dnd++;
                } else if (x.getStatus().equals(UserStatus.IDLE)) {
                    away++;
                } else if (x.getStatus().equals(UserStatus.ONLINE)) {
                    online++;
                } else if (x.getStatus().equals(UserStatus.OFFLINE)) {
                    offline++;
                }

            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("overall Members in Server: " + all + "\n");
        sb.append("Bot users in Server: " + botcount + "\n");
        sb.append("Non bot users in Server: " + usercount + "\n");
        sb.append("\nOnline Users: " + online + "\n");
        sb.append("Do-not-disturb Users: " + dnd + "\n");
        sb.append("Idle Users: " + away + "\n");
        sb.append("Offline Users: " + offline);
        event.getChannel().sendMessage(Templates.norembed().setTitle("\uD83D\uDCDD Users in " + event.getServer().get().getName()).setDescription(sb.toString()).setColor(Color.blue));
    }

    public static void userinfo() {
        Object[] list = event.getMessage().getMentionedUsers().toArray();
        if (list.length > 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: mp> userinfo " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
        } else if (list.length == 0) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: mp> userinfo " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
        } else {
            User checkuser = event.getMessage().getMentionedUsers().get(0);
            EmbedBuilder eb = Templates.defaultembed();
            eb.setThumbnail(checkuser.getAvatar());
            eb.setDescription("[Avatar URL](" + checkuser.getAvatar().getUrl() + ")");
            eb.setColor(Color.blue);
            if (checkuser.isBot()) {
                eb.setTitle("\uD83C\uDF0D Global Informations about " + checkuser.getName() + " [BOT]");
            } else {
                eb.setTitle("\uD83C\uDF0D Global Informations about " + checkuser.getName() + "");
            }
            if (checkuser.isBotOwner()) {
                eb.setTitle("\uD83C\uDF0D Global Informations about " + checkuser.getName() + " [BOT OWNER]");
            } else {
                eb.setTitle("\uD83C\uDF0D Global Informations about " + checkuser.getName() + "");
            }
            eb.addInlineField("Username", checkuser.getName());
            eb.addInlineField("Discriminator", "#**" + checkuser.getDiscriminator() + "**");


            eb.addField("Account created", new TimestampResolver(Timestamp.from(checkuser.getCreationTimestamp())).resolve());
            StringBuilder sb = new StringBuilder();
            for (Server x : checkuser.getMutualServers()) {
                if (x.equals(event.getServer().get())) {
                    sb.append("- " + x.getName() + " [CURRENT]\n");
                } else {
                    sb.append("- " + x.getName() + "\n");

                }
            }
            if (event.getMessageAuthor().isBotOwner()) {
                eb.addField("Mutal Servers [OWNER ONLY]", sb.toString());
            }


            EmbedBuilder yeet = Templates.defaultembed();

            yeet.setTitle("\uD83D\uDCF0 Local Informations about " + checkuser.getName() + " on Server \"" + event.getServer().get().getName() + "\"");
            yeet.setColor(checkuser.getRoleColor(event.getServer().get()).get());
            if (!checkuser.getNickname(event.getServer().get()).isPresent()) {
                yeet.addField("Nickname", "*No Nickname given*");
            } else {
                yeet.addField("Nickname", checkuser.getNickname(event.getServer().get()).get());
            }


            yeet.addField("Server joined", new TimestampResolver(Timestamp.from(checkuser.getJoinedAtTimestamp(event.getServer().get()).get())).resolve());

            StringBuilder stri = new StringBuilder();

            for (Role x : checkuser.getRoles(event.getServer().get())) {
                if (x.isEveryoneRole()) {

                } else {
                    stri.append(x.getMentionTag() + "\n");

                }
            }
            yeet.addField("Roles", stri.toString());
            event.getChannel().sendMessage(eb);
            event.getChannel().sendMessage(yeet);

        }
    }

    public static void block() {
        String serverid = event.getServer().get().getIdAsString();
        Preferences prefs = Preferences.userNodeForPackage(Main.class).node("cmdblock");
        if (!event.getMessageAuthor().isBotOwner()) {
            event.getChannel().sendMessage(Templates.permerrorembed().setDescription("Only the bot owner is permitted to use command management features!"));

        } else {
            if (prefs.getBoolean(serverid, false) == true) {
                prefs.putBoolean(serverid, false);
                event.getChannel().sendMessage(Templates.debugembed().setDescription("Command Listening for **" + event.getServer().get().getName() + " (" + event.getServer().get().getIdAsString() + ")** has been __enabled__."));
            } else if (prefs.getBoolean(serverid, false) == false) {
                prefs.putBoolean(serverid, true);
                event.getChannel().sendMessage(Templates.debugembed().setDescription("Command Listening for **" + event.getServer().get().getName() + " (" + event.getServer().get().getIdAsString() + ")** has been __disabled__."));

            }


        }
    }

    public static void osuhelpPage(String prefix) {


        EmbedBuilder generalembed = Templates.defaultembed();

        generalembed.setColor(Color.MAGENTA).setTitle("<:osuicon:692410518090022944> osu! commands [WIP]");
        generalembed.setThumbnail(Main.api.getYourself().getAvatar());
        generalembed.addField(prefix + " osu user <username/id>", "Get the osu! stats of a specific User.");
        generalembed.addField(prefix + " osu recent <username/id>", "Get the most recent map of a specific User.");

        event.getChannel().sendMessage(generalembed);


    }

    public static void generalhelpPage(String prefix) {


        EmbedBuilder generalembed = Templates.defaultembed();

        generalembed.setColor(Color.MAGENTA).setTitle("\uD83C\uDF10 General Commands");
        generalembed.setThumbnail(Main.api.getYourself().getAvatar());
        generalembed.addField(prefix + " users", "Get a count of every user in your server.");
        generalembed.addField(prefix + " userinfo @mention", "Shows global and local informations about a mentioned user.");
        generalembed.addField(prefix + " osu", "Opens the osu help page");
        generalembed.addField(prefix + " prefix <prefix>/reset", "Set a custom prefix for the bot(default is ``mp>``)/use reset to reset it's prefix back to ``mp>`` [Usable by Server Admins]");
        generalembed.addField(prefix + " status", "Prints ARABAPI and Mope CDN Status");
        generalembed.addField(prefix + " about", "about this Bot.");
        generalembed.addField("fmdev>", "only works on the Development instance of Mope, so pretty much useless for the normal user :)");
        event.getChannel().sendMessage(generalembed);


    }

    public static void moderationhelpPage(String prefix) {


        EmbedBuilder generalembed = Templates.defaultembed();

        generalembed.setColor(Color.MAGENTA).setTitle("\uD83D\uDEE0Moderation Commands");
        generalembed.setThumbnail(Main.api.getYourself().getAvatar());
        generalembed.addField(prefix + " clearchat <limit>", "Deletes given amount of Messages in called channel");
        event.getChannel().sendMessage(generalembed);


    }


    public static void allHelpPages(String prefix) {

        System.out.println("yeet");
        EmbedBuilder generalembed = new EmbedBuilder().setFooter("Mope | HopeDev | Version ID: " + Main.versionid);

        generalembed.setColor(Color.MAGENTA).setTitle("\uD83C\uDF10 Mope Bot Help");
        generalembed.setThumbnail(Main.api.getYourself().getAvatar());
        generalembed.setDescription("Available Help pages for Mope");
        generalembed.addField(prefix + " help general", "Shows all general commands for Mope");
        generalembed.addField(prefix + " help moderation", "Shows all Moderation commands");
        generalembed.addField(prefix + " help interactive", "Shows all interaction commands (powered by my own API)");
        generalembed.addField(prefix + " help osu", "Shows all osu stats commands");

        System.out.println(generalembed.toString());
        event.getChannel().sendMessage(generalembed);


    }

    public static void interactivehelpPage(String prefix) {

        //Testing
        EmbedBuilder interactiveembed = Templates.defaultembed();
        interactiveembed.setThumbnail(Main.api.getYourself().getAvatar());
        interactiveembed.setColor(Color.MAGENTA).setTitle("\uD83D\uDE02 Interactive Commands [UPDATED]");
        interactiveembed.setDescription("_Took me hours to make a working api myself_\nHere's a list of available Interactive Commands:");
        interactiveembed.addField(prefix + " smug", "makes you smug");
        interactiveembed.addField(prefix + " cry", "makes you cry");
        interactiveembed.addField(prefix + " laugh", "makes you laugh");
        interactiveembed.addField(prefix + "blush", "makes you blush");
        interactiveembed.addField(prefix + " hug @mention", "Hugs mentioned user");
        interactiveembed.addField(prefix + " kiss @mention", "Kiss mentioned user");
        interactiveembed.addField(prefix + " pat @mention", "Give mentioned user headpats");
        interactiveembed.addField(prefix + " poke @mention", "pokes mentioned user");
        interactiveembed.addField(prefix + " tickle @mention", "tickles mentioned user");
        interactiveembed.addField(prefix + " feed @mention", "feed mentioned user");
        interactiveembed.addField(prefix + " cuddle @mention", "Cuddle with mentioned user");
        interactiveembed.addField(prefix + " lick @mention", "Licks mentioned user");

        event.getChannel().sendMessage(interactiveembed);


    }


    public static void MusicHelp(String prefix) {

        EmbedBuilder interactiveembed = Templates.defaultembed();
        interactiveembed.setThumbnail(Main.api.getYourself().getAvatar());
        interactiveembed.setColor(Color.MAGENTA).setTitle("\u266A Music Commands [DANGEROUSLY BETA]");
        interactiveembed.setDescription("This feature is very very experimental! Please use it at your own Risk! Feedback --> Hope#1445");
        interactiveembed.addField(prefix + " music play <URL or Keyword>", "Searches for a Video and plays it back (You have to be in a voice channel)");
        interactiveembed.addField(prefix + " music pause/unpause", "Pauses/Unpauses the Playback");

        event.getChannel().sendMessage(interactiveembed);


    }


    public static void ImageSender(String Description, String URL) {


        EmbedBuilder embed = Templates.defaultembed();

        embed.setColor(Color.cyan);
        embed.setImage(URL);
        embed.setDescription(Description);

        event.getChannel().sendMessage(embed);


    }

}
