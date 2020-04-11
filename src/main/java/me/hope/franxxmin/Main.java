package me.hope.franxxmin;


import me.hope.franxxmin.listeners.CommandReprocessor;
import me.hope.franxxmin.listeners.Music.FinishedListener;
import me.hope.franxxmin.listeners.Reconnect;
import me.hope.franxxmin.listeners.onServerJoin;
import me.hope.franxxmin.utils.DBL;
import me.hope.franxxmin.utils.TimerThreadCooldown;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

public class Main {


    public static DiscordApi api;
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
    public static String botdcapitoken;
    public static String googleconsoletoken;

    public static Color blurple = new Color(114, 137, 218);

    public static void main(String[] args) {
        OSUAPIKEY = args[4];
        loggingtoken = args[5];
        dbltoken = args[6];
        googleconsoletoken = args[7];
        localmode = Boolean.valueOf(args[0]);
        System.out.println(googleconsoletoken);
        if (localmode) {
            new BotInitializer(args[2]);
            botdcapitoken = args[2];

        } else {
            DBL.dbl = new DiscordBotListAPI.Builder()
                    .token(Main.dbltoken)
                    .botId("688561837020545080")
                    .build();
            System.out.println("[DBL] DBL Connection successfully initialized!");
            new BotInitializer(args[1]);
            botdcapitoken = args[1];
            DBL.dbl.setStats(ServerHashmaps.ID.size());


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
        api.addReconnectListener(new Reconnect());
        System.out.println("Starting Timer Thread for cooldowns..");
        TimerThreadCooldown timer;

        // Ab jetzt wird "Timer"
        // im Hintergrund ausgegeben:
        timer = new TimerThreadCooldown();
        timer.start();
        System.out.println("Started! Outputting current Test values for help");
        //  api.addReactionAddListener(new osureactor());
        apistring = args[3];
        if (api.getYourself().getIdAsString().equals("691361576279736402")) {
            dfprfx = "mpdev>";
            api.updateActivity("DEVELOPMENT MODE!!!");
            System.out.println("Bot is running on Mope Development ==> using development mode");
        } else {
            dfprfx = "mp>";
            System.out.println("Bot initialized successfully!");

        }

        if (!UpdatedServerID.get("id", "empty").equals("empty")) {
            EmbedBuilder eb = Templates.debugembed();
            eb.setDescription("Update applied successfully!\n \nNew Version ID: " + Main.versionid);
            Main.api.getChannelById(UpdatedServerID.get("id", "0")).get().asServerTextChannel().get().sendMessage(eb);


        }


        int ccc = 0;

        //Thread reconnector for onOpcode reconnect failure --> see https://hope.is-inside.me/Eh4Bk6lQ.png
        System.out.println("NOT Starting OPCODE ReconnectorThread..");
        //ThreadReconnector thread;

        //thread = new ThreadReconnector();
        //thread.start();


        Main.api.addAudioSourceFinishedListener(new FinishedListener());
        api.updateActivity(ActivityType.LISTENING, Main.api.getServers().size() + " Guilds | mp> help");

    }

}

