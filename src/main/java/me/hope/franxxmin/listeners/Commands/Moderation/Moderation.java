package me.hope.franxxmin.listeners.Commands.Moderation;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.List;
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
            if (sb.toString().equalsIgnoreCase("none")) {
                eb.addField("Reason", sb.toString());
            } else {
                eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
            }
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
        if (sb.toString().equalsIgnoreCase("none")) {
            eb.addField("Reason", sb.toString());
        } else {
            eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
        }
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
            if (sb.toString().equalsIgnoreCase("none")) {
                eb.addField("Reason", sb.toString());
            } else {
                eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
            }
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
        if (sb.toString().equalsIgnoreCase("none")) {
            eb.addField("Reason", sb.toString());
        } else {
            eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
        }
        event.getChannel().sendMessage(eb).join();
        event.getServer().get().banUser(attacker, 1, sb.toString().substring(0, sb.length() - 1));
        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");

        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");
    }

    public static boolean checkMuteRole() {

        //Role checking
        if (!event.getServer().get().canYouManageRoles()) {
            event.getChannel().sendMessage(Templates.missingperms("Manage Roles", "[optional] Manage Channels"));
            return false;

        }
        List<Role> Roles;
        try {
            Roles = event.getServer().get().getRolesByNameIgnoreCase("muted");
            Roles.get(0);
            if (Roles.size() != 1) {
                event.getChannel().sendMessage(Templates.moreThanoneMuteRoleEmbed());
            }
        } catch (Exception e) {
            event.getChannel().sendMessage(Templates.MuteRolemissingEmbed());
            return false;
        }
        Role mrole = Roles.get(0);
        // Debug event.getChannel().sendMessage("Successfully detected Mute Role: " + mrole.getMentionTag());

        return true;
    }

    public static void unmuteUser() {
        if (!checkMuteRole()) {
            return;
        }

        User author = event.getMessageAuthor().asUser().get();
        User bot = event.getApi().getYourself();
        Role mutedrole = event.getServer().get().getRolesByNameIgnoreCase("muted").get(0);
        if (!event.getServer().get().canYouManageRoles()) {
            event.getChannel().sendMessage(Templates.missingperms("Manage Roles", "[optional] Manage Channels"));
            return;
        }
        if (!event.getApi().getYourself().canManageRole(mutedrole)) {
            event.getChannel().sendMessage(Templates.cantmanageroleerrorEmbed(mutedrole.getMentionTag()));
            return;

        }
        //Author needs to have Manage Messages Permission to be able to use this Command!


        if (!event.getChannel().canManageMessages(author)) {
            event.getChannel().sendMessage(Templates.missinguserperms("Manage Messages"));
            return;
        }

        // Debug event.getChannel().sendMessage("All checks passed!");

        if (cmd.length == 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Please Mention a User to Mute"));
            return;
        }

        User attacker;
        try {
            attacker = event.getMessage().getMentionedUsers().get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This User is not on this Server!"));
            return;
        }
        System.out.println(attacker.getRoles(event.getServer().get()));
        if (!attacker.getRoles(event.getServer().get()).contains(mutedrole)) {
            event.getChannel().sendMessage(Templates.notmutedEmbed().setColor(Color.red));
            return;
        }
        boolean couldmsg = false;
        try {
            EmbedBuilder eb = Templates.punishmentEmbed();
            eb.setDescription("Your Punishment in " + event.getServer().get().getName() + " has been removed");
            eb.addInlineField("Type", "Mute");
            eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
            eb.setColor(Color.green);
            Message msg = attacker.sendMessage(eb).join();

            couldmsg = true;

        } catch (Exception e) {
            // lmao literally everyone disables dms nowadays because of scams pog
        }
        String ok = "";
        if (!couldmsg) {
            ok = "\n \n_Bot Note: Could not inform user of their Punishment: DMs are disabled_";
        }
        EmbedBuilder eb = Templates.muteEmbed();
        eb.setTitle("\uD83D\uDD0A User unmuted");
        eb.addInlineField("User", attacker.getDiscriminatedName());
        eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
        event.getChannel().sendMessage(eb).join();
        attacker.removeRole(mutedrole);
    }


    public static void unmuteUserhandler(User attacker, Role mutedrole) {
        if (cmd.length == 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Please Mention a User to Mute"));
            return;
        }


        boolean couldmsg = false;
        try {
            EmbedBuilder eb = Templates.punishmentEmbed();
            eb.setDescription("Your Punishment in " + event.getServer().get().getName() + " has been removed");
            eb.addInlineField("Type", "Mute");
            eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
            eb.setColor(Color.green);
            Message msg = attacker.sendMessage(eb).join();

            couldmsg = true;

        } catch (Exception e) {
            // lmao literally everyone disables dms nowadays because of scams pog
        }
        String ok = "";
        if (!couldmsg) {
            ok = "\n \n_Bot Note: Could not inform user of their Punishment: DMs are disabled_";
        }
        EmbedBuilder eb = Templates.muteEmbed();
        eb.setTitle("\uD83D\uDD0A User unmuted");
        eb.addInlineField("User", attacker.getDiscriminatedName());
        eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
        event.getChannel().sendMessage(eb).join();
        attacker.removeRole(mutedrole, "Unmuted by " + event.getMessageAuthor().asUser().get().getDiscriminatedName());

    }

    public static void permMuteUser() {

        if (!checkMuteRole()) {
            return;
        }

        User author = event.getMessageAuthor().asUser().get();
        User bot = event.getApi().getYourself();
        Role mutedrole = event.getServer().get().getRolesByNameIgnoreCase("muted").get(0);
        if (!event.getServer().get().canYouManageRoles()) {
            event.getChannel().sendMessage(Templates.missingperms("Manage Roles", "[optional] Manage Channels"));
            return;
        }
        if (!event.getApi().getYourself().canManageRole(mutedrole)) {
            event.getChannel().sendMessage(Templates.cantmanageroleerrorEmbed(mutedrole.getMentionTag()));
            return;

        }
        //Author needs to have Manage Messages Permission to be able to use this Command!


        if (!event.getChannel().canManageMessages(author)) {
            event.getChannel().sendMessage(Templates.missinguserperms("Manage Messages"));
            return;
        }

        // Debug event.getChannel().sendMessage("All checks passed!");

        if (cmd.length == 1) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Please Mention a User to Mute"));
            return;
        }

        User attacker;
        try {
            attacker = event.getMessage().getMentionedUsers().get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This User is not on this Server!"));
            return;
        }
        if (attacker.getRoles(event.getServer().get()).contains(mutedrole)) {
            unmuteUserhandler(attacker, mutedrole);
            return;
        }
        if (attacker.equals(author)) {
            event.getChannel().sendMessage(Templates.muteErrorEmbed().setDescription("You cannot mute yourself!"));
            return;
        }
        // Finally, for the first fucking time, make the final mute function
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
            eb.addInlineField("Type", "Mute");
            eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
            if (sb.toString().equalsIgnoreCase("none")) {
                eb.addField("Reason", sb.toString());
            } else {
                eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
            }
            Message msg = attacker.sendMessage(eb).join();
            couldmsg = true;

        } catch (Exception e) {
            // lmao literally everyone disables dms nowadays because of scams pog
        }
        String ok = "";
        if (!couldmsg) {
            ok = "\n \n_Bot Note: Could not inform user of their Punishment: DMs are disabled_";
        }
        EmbedBuilder eb = Templates.muteEmbed();
        eb.addInlineField("Muted User", attacker.getDiscriminatedName());
        eb.setColor(Color.red);
        eb.addInlineField("Moderator", event.getMessageAuthor().getDiscriminatedName());
        if (sb.toString().equalsIgnoreCase("none")) {
            eb.addField("Reason", sb.toString());
        } else {
            eb.addField("Reason", sb.toString().substring(0, sb.length() - 1));
        }
        event.getChannel().sendMessage(eb).join();
        attacker.addRole(mutedrole, sb.toString().substring(0, sb.length() - 1));

        // Relevant for banning  event.getServer().get().banUser(attacker, 1, sb.toString().substring(0, sb.length() - 1));
        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");
        //  event.getChannel().sendMessage("*kicks "+attacker.getName()+" for the Reason "+sb.toString().substring(0, sb.length()-1)+"*");
    }

    // actual Muting Function














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

