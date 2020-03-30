package me.hope.franxxmin.utils;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;
import org.discordbots.api.client.DiscordBotListAPI;

public class DBL {
    public static DiscordBotListAPI dbl;

    public static void init() {

        dbl= new DiscordBotListAPI.Builder()
                .token(Main.dbltoken)
                .botId("688561837020545080")
                .build();
        System.out.println("[DBL] DBL Connection successfully initialized!");

    }
    public static void updateServerCount() {
        dbl.setStats(ServerHashmaps.ID.size());
        System.out.println("[DBL] Stat \"Server Count\" successfully updated");
    }
}
