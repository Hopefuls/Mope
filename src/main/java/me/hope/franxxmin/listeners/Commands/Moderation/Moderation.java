package me.hope.franxxmin.listeners.Commands.Moderation;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.prefs.Preferences;

public class Moderation {


    //Initialize Class Variables for pre-functions
    private static MessageCreateEvent event;
    private static String prefix;
    private static String[] cmd;
    private static User yourself = Main.api.getYourself();
    private static Preferences settings = Preferences.userNodeForPackage(Moderation.class);
    private static Boolean ModerationAllowed;

    public Moderation(MessageCreateEvent event, String[] cmd, String pr) {


        Preferences ac = settings.node(event.getServer().get().getIdAsString());
        ModerationAllowed = settings.getBoolean("useModeration", false);
        //
        Moderation.cmd = cmd;
        Moderation.event = event;
        prefix = pr;

    }

    public static void clearChat() {

        if (!event.getChannel().canManageMessages(event.getMessageAuthor().asUser().get())) {
            event.getChannel().sendMessage(Templates.missinguserperms("Manage Messages"));
            return;
        }
        if (!event.getChannel().canYouManageMessages()) {
            event.getChannel().sendMessage(Templates.missingperms("Manage Messages"));
            return;
        }
        if (cmd.length == 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to enter a Limit!"));
            return;
        }
        int msgcount;
        try {
            msgcount = Integer.parseInt(cmd[1]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Please enter a valid Number!"));
            return;
        }
        event.getChannel().bulkDelete(event.getChannel().getMessages(msgcount).join());
        event.getChannel().sendMessage(Templates.defaultembed().setColor(Color.green).setDescription("Successfully deleted " + msgcount + " Messages!"));

    }


    public static void kickUser() {
        if (!event.getServer().get().canYouKickUsers()) {
            event.getChannel().sendMessage(Templates.missingperms("Kick Members"));
            return;
        }
        if (cmd.length == 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Please Mention a User to Kick"));
            return;
        }
        User attacker;
        try {
            attacker = event.getMessage().getMentionedUsers().get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This User is not on this Server!"));
            return;
        }
        if (!event.getServer().get().canKickUser(yourself, attacker)) {
            event.getChannel().sendMessage(Templates.higherroleerror("kick"));
            return;
        }
        if (!event.getServer().get().canKickUser(event.getMessageAuthor().asUser().get(), attacker)) {
            event.getChannel().sendMessage(Templates.higherroleuusererror("kick"));
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (cmd.length > 2) {
            for (int i = 2; i < cmd.length; i++) {
                sb.append(cmd[i] + " ");
            }
        } else {
            sb.append("none");
        }
        boolean couldmsg = false;
        try {
            EmbedBuilder eb = Templates.punishmentEmbed();
            eb.setDescription("You have been given a Punishment in " + event.getServer().get().getName());
            eb.addInlineField("Type", "Kick");
            eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
            eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
            Message msg = attacker.sendMessage(eb).join();
            couldmsg = true;

        } catch (Exception e) {
            // lmao literally everyone disables dms nowadays because of scams pog
        }
        String ok = "";
        if (!couldmsg) {
            ok = "\n \n_Bot Note: Could not inform user of their Punishment: DMs are disabled_";
        }
        EmbedBuilder eb = Templates.kickEmbed();
        eb.addInlineField("Kicked User", attacker.getDiscriminatedName());
        eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
        eb.addField("Reason", sb.toString().substring(0, sb.length() - 1) + ok);
        event.getChannel().sendMessage(eb).join();
        event.getServer().get().kickUser(attacker, sb.toString().substring(0, sb.length() - 1));
        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");
    }

    public static void banUser() {

        if (!event.getServer().get().canYouBanUsers()) {
            event.getChannel().sendMessage(Templates.missingperms("Ban Members"));
            return;
        }
        if (cmd.length == 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Please Mention a User to Ban"));
            return;
        }
        User attacker;
        try {
            attacker = event.getMessage().getMentionedUsers().get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This User is not on this Server!"));
            return;
        }
        if (!event.getServer().get().canBanUser(yourself, attacker)) {
            event.getChannel().sendMessage(Templates.higherroleerror("Ban"));
            return;
        }
        if (!event.getServer().get().canBanUser(event.getMessageAuthor().asUser().get(), attacker)) {
            event.getChannel().sendMessage(Templates.higherroleuusererror("Ban"));
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (cmd.length > 2) {
            for (int i = 2; i < cmd.length; i++) {
                sb.append(cmd[i] + " ");
            }
        } else {
            sb.append("none");
        }
        boolean couldmsg = false;
        try {
            EmbedBuilder eb = Templates.punishmentEmbed();
            eb.setDescription("You have been given a Punishment in " + event.getServer().get().getName());
            eb.addInlineField("Type", "Ban");
            eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
            eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
            Message msg = attacker.sendMessage(eb).join();
            couldmsg = true;

        } catch (Exception e) {
            // lmao literally everyone disables dms nowadays because of scams pog
        }
        String ok = "";
        if (!couldmsg) {
            ok = "\n \n_Bot Note: Could not inform user of their Punishment: DMs are disabled_";
        }
        EmbedBuilder eb = Templates.banEmbed();
        eb.addInlineField("Banned User", attacker.getDiscriminatedName());
        eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
        eb.addField("Reason", sb.toString().substring(0, sb.length() - 1) + ok);
        event.getChannel().sendMessage(eb).join();
        event.getServer().get().banUser(attacker, 1, sb.toString().substring(0, sb.length() - 1));
        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");

        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");
    }
















/*will fix soon
    public static void kickUser() {
        if (ModerationAllowed) {
            if (mce.getMessageAuthor().canKickUsersFromServer()) {


                if (cmd.length == 1) {
                    mce.getChannel().sendMessage("You need to enter a UserID or Mention a User and optionally a Reason.");

                } else {
                    boolean finalized = false;
                    User user = null;
                    try {
                        user = mce.getMessage().getMentionedUsers().get(0);
                        finalized = true;

                    } catch (IndexOutOfBoundsException e) {
                        mce.getChannel().sendMessage("You need to mention someone.");
                    }
                    if (finalized) {
                        if (user.equals(mce.getMessageAuthor().asUser().get())) {
                            mce.getChannel().sendMessage("You can't kick yourself!");

                        } else if (!mce.getServer().get().canYouKickUsers()) {
                            mce.getChannel().sendMessage("I need KICK_MEMBER permissions in order to kick Members.");
                        } else if (!mce.getServer().get().canYouKickUser(user)) {
                            mce.getChannel().sendMessage(user.getDiscriminatedName() + " could not be kicked: They have a higher Role than i do");
                        } else {
                            mce.getChannel().sendMessage("Kicked " + user.getDiscriminatedName());
                        }
                    }
                }

            } else {
                mce.getChannel().sendMessage("You are not allowed to kick Members.");
            }
        }
    }
    */
}
