package me.hope.franxxmin.listeners.Commands;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.utils.RequestLibrary.makeRequest;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Status {
    public static EmbedBuilder request(String ID, MessageCreateEvent event) {
        //ID is important to keep track of the cooldown
        EmbedBuilder eb = Templates.defaultembed();
        eb.setThumbnail(Main.api.getYourself().getAvatar());
        long startTime = System.nanoTime();
        makeRequest.getResponse("https://franxx.ml/api.php?status&key=" + Main.apistring, Status.class, event);

        long elapsedTime = System.nanoTime() - startTime;
        long durationInMs = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        eb.setDescription("BOT STATUS: OK \u2713");
        eb.addField("ARABNET API", "Response Time: **" + durationInMs + "ms**");

        long startTimeCDN = System.nanoTime();
        JSONObject jsonobject = new JSONObject(makeRequest.getResponse("https://franxx.ml/api.php?count=all&key=" + Main.apistring, Status.class, event));
        long elapsedTimeCDN = System.nanoTime() - startTimeCDN;
        long durationInMsCDN = TimeUnit.MILLISECONDS.convert(elapsedTimeCDN, TimeUnit.NANOSECONDS);

        int hugcount = jsonobject.getInt("hugcount");
        int cuddlecount = jsonobject.getInt("cuddlecount");
        int patcount = jsonobject.getInt("patcount");
        int kisscount = jsonobject.getInt("kisscount");
        int smugcount = jsonobject.getInt("smugcount");
        int crycount = jsonobject.getInt("crycount");
        int pokecount = jsonobject.getInt("pokecount");
        int ticklecount = jsonobject.getInt("ticklecount");
        int feedcount = jsonobject.getInt("feedcount");

        int happycount = jsonobject.getInt("happycount");
        int lickcount = jsonobject.getInt("lickcount");
        int laughcount = jsonobject.getInt("laughcount");
        int blushcount = jsonobject.getInt("blushcount");
        eb.addField("Mope CDN Status", "Response Time: **" + durationInMsCDN + "ms**\nResource Count:\n \n      Resource \"kiss\"\u2192 " + kisscount + "\n      Resource \"hug\"\u2192 " + hugcount + "\n      Resource \"pat\"\u2192 " + patcount + "\n      Resource \"smug\"\u2192 " + smugcount + "\n      Resource \"cry\"\u2192 " + crycount + "\n      Resource \"poke\"\u2192 " + pokecount + "\n      Resource \"tickle\"\u2192 " + ticklecount + "\n      Resource \"feed\"\u2192 " + feedcount + "\n      Resource \"cuddle\"\u2192 " + cuddlecount + "\n      Resource \"laugh\"\u2192 " + laughcount + "\n      Resource \"lick\"\u2192 " + lickcount + "\n      Resource \"happy\"\u2192 " + happycount + "\n      Resource \"blush\"\u2192 " + blushcount);

        return eb;
    }
}
