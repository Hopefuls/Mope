package me.hope.franxxmin;

import me.hope.franxxmin.onStart.CooldownManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Permissions;

public class BotInitializer {

    public BotInitializer(String token) {

        //Initialize API with API token from Arguments
        // Use Shard 0 for Debugging reasons


        Main.api = new DiscordApiBuilder().setToken(token).login().join();
        //Print Bot invite link for easier use and making sure that the bot did successfully conntect to the Discord API
        System.out.println("Bot Invite: " + Main.api.createBotInvite(Permissions.fromBitmask(8)));

        //initialize the startUpdater
        //yeet
        CooldownManager.onStartUpdate();
    }

    private static void onShardLogin(DiscordApi api) {
        System.out.println("Shard " + api.getCurrentShard() + " logged in!");
        // You can treat the shard like a normal bot account, e.g. registering listeners

    }
}
