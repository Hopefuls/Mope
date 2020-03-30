package me.hope.franxxmin;


import me.hope.franxxmin.listeners.CommandReprocessor;
import me.hope.franxxmin.listeners.onServerJoin;
import me.hope.franxxmin.utils.TimerThreadCooldown;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

public class Main {


public static DiscordApi api;
    public static DiscordApi logging;
    public static Preferences pref = Preferences.userNodeForPackage(Main.class);
    public static Preferences UpdatedServerID = Preferences.userNodeForPackage(Main.class).node("dev");

    public static Preferences debug = Preferences.userNodeForPackage(Main.class).node("Main");

    //TODO in einer eigenen Klasse machen für bessere Übersicht

    public static String OSUAPIKEY;
    public static String versionid = UUID.randomUUID().toString().substring(30);
    public static String loggingtoken;
    public static String dbltoken;
    public static String dfprfx;
    public static String apistring;
    public static Boolean localmode;
    public static void main(String[] args) {
        OSUAPIKEY = args[4];
        loggingtoken = args[5];
        dbltoken = args[6];
        localmode = Boolean.valueOf(args[0]);
        if (localmode) {
            new BotInitializer(args[2]);

        } else {
            new BotInitializer(args[1]);

        }

        System.out.println("Using versionID " + versionid);
        //adds CommandReprocessor for Commands such as fm> or a custom one (later updates)

        api.addMessageCreateListener(event -> {
            if (event.isPrivateMessage()) {

            } else {


                if (event.getMessageAuthor().isYourself() || event.getMessageAuthor().isBotUser()) {
                } else {
                    List<String> Arr = new LinkedList<String>(Arrays.asList(event.getMessageContent().split(" ")));
                    Arr.remove(0);
                    String[] str = Arr.toArray(new String[Arr.size()]);
                    new CommandReprocessor(event, str, event.getMessageContent().split(" "));
                }
            }
        });

        api.addServerJoinListener(new onServerJoin());
        api.addServerLeaveListener(new onServerJoin());

        System.out.println("Starting Timer Thread for cooldowns..");
        TimerThreadCooldown timer;

        // Ab jetzt wird "Timer"
        // im Hintergrund ausgegeben:
        timer = new TimerThreadCooldown();
        timer.start();
        System.out.println("Started! Outputting current Test values for help");
        api.updateActivity("Running on "+ ServerHashmaps.ID.size()+" Servers");
        //  api.addReactionAddListener(new osureactor());
        apistring = args[3];
        if (api.getYourself().getIdAsString().equals("691361576279736402")) {
            dfprfx = "fmdev>";
            api.updateActivity("DEVELOPMENT MODE!!!");
            System.out.println("Bot is running on Franxxmin Development ==> using development mode");
        } else {
            dfprfx = "fm>";
            System.out.println("Bot initialized successfully!");

        }

        if (!UpdatedServerID.get("id", "empty").equals("empty")) {
            EmbedBuilder eb = Templates.debugembed();
            eb.setDescription("Update applied successfully!\n \nNew Version ID: " + Main.versionid);
            Main.api.getChannelById(UpdatedServerID.get("id", "0")).get().asServerTextChannel().get().sendMessage(eb);


        }
        int ccc = 0;
        for (User user : Main.api.getServerById("264445053596991498").get().getMembers()) {
            if (user.isBot()) {
                if (user.getName().toLowerCase().contains("covid19") || user.getName().toLowerCase().contains("covid-19") || user.getName().toLowerCase().contains("corona") || user.getName().toLowerCase().contains("coronavirus")) {

                    ccc++;
                }
            }
        }

    }

}

