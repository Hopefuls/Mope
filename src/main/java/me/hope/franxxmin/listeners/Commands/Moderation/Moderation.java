package me.hope.franxxmin.listeners.Commands.Moderation;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.prefs.Preferences;

public class Moderation {


    //Initialize Class Variables for pre-functions
    private static MessageCreateEvent mce;
    private static String prefix;
    private static String[] cmd;
    private static Preferences settings = Preferences.userNodeForPackage(Moderation.class);
    private static Boolean ModerationAllowed;

    public Moderation(MessageCreateEvent event, String[] cmd, String pr) {


        Preferences ac = settings.node(event.getServer().get().getIdAsString());
        ModerationAllowed = settings.getBoolean("useModeration", false);
        //
        Moderation.cmd = cmd;
        mce = event;
        prefix = pr;

    }

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
}
