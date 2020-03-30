package me.hope.franxxmin.utils.RequestLibrary;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.onStart.CooldownManager;
import me.hope.franxxmin.utils.TimestampResolver;
import me.hope.franxxmin.utils.VariablesStorage.Cooldown;
import me.hope.franxxmin.utils.VariablesStorage.MiscVariables;
import me.hope.franxxmin.utils.cooldownutility;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OSU_PPY_SH extends Thread {
    public static MessageCreateEvent event;
    public static Message msg;
    public static String username;
    public static Boolean wentthr = false;
    public static WhatIWant iwant;

    public OSU_PPY_SH(String username, MessageCreateEvent event, Message msg, WhatIWant iwant) {
        OSU_PPY_SH.event = event;
        OSU_PPY_SH.username = username;
        OSU_PPY_SH.msg = msg;
        OSU_PPY_SH.iwant = iwant;
    }

    public void run() {
        if (iwant.equals(WhatIWant.GET_USER)) {
            getUser(username);

        } else if (iwant.equals(WhatIWant.GET_RECENT)) {
            getRecent(username);
        }
        for (int i = 0; i < 2; i++) {


            for (int i2 = 0; i2 < 20; i2++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                if (!wentthr) {
                    System.out.println("Failed querying on " + event.getServer().get().getIdAsString() + ", retrying..");
                    System.out.println("Wentthr" + wentthr.toString());
            } else if (wentthr) {

                    Thread.currentThread().interrupt();

                }
            }
            System.out.println("failed. retrying..");
        }
        Thread.currentThread().interrupt();
    }

    public void getRecent(String username) {
        String ID = event.getServer().get().getIdAsString();

        if (new cooldownutility(ID).chkcooldown(CooldownManager.TYPE.OSU) != 0.0) {
            event.getChannel().sendMessage(Templates.cooldownerrorembed("osu", Cooldown.def.get(CooldownManager.TYPE.OSU), new cooldownutility(ID).chkcooldown(CooldownManager.TYPE.OSU)));


        } else {
            new cooldownutility(ID).cooldownreset(CooldownManager.TYPE.OSU);


            msg = event.getChannel().sendMessage(Templates.defaultembed().setDescription("Querying Recent map from ``" + username.replace("%20", " ") + "``, please wait..").setThumbnail("https://i.pinimg.com/originals/3e/f0/e6/3ef0e69f3c889c1307330c36a501eb12.gif")).join();
            String apikey = Main.OSUAPIKEY;
            try {
                String type;
                EmbedBuilder eb = Templates.defaultembed();
                eb.setColor(Color.pink);

                try {
                    int num = Integer.parseInt(username);
                    type = "id";
                } catch (NumberFormatException e) {
                    type = "string";
                }
                JSONArray jsonarray = new JSONArray(makeRequest.getResponse("https://osu.ppy.sh/api/get_user_recent?k=" + apikey + "&u=" + username.replace(" ", "%20") + "&limit=1&type=" + type, OSU_PPY_SH.class, event));

                JSONObject result = jsonarray.getJSONObject(0);
                JSONArray jsonarrayuserinfo = new JSONArray(makeRequest.getResponse("https://osu.ppy.sh/api/get_user?k=" + apikey + "&u=" + username.replace(" ", "%20"), OSU_PPY_SH.class, event));
                JSONObject resultuserinfo = jsonarrayuserinfo.getJSONObject(0);
                JSONArray jsonarraybeatmapinfo = new JSONArray(makeRequest.getResponse("https://osu.ppy.sh/api/get_beatmaps?k=" + apikey + "&limit=1&b=" + result.getString("beatmap_id"), OSU_PPY_SH.class, event));
                JSONObject resultbeatmapinfo = jsonarraybeatmapinfo.getJSONObject(0);

                wentthr = true;
                eb.setTitle("<:osuicon:692410518090022944> :flag_" + resultuserinfo.getString("country").toLowerCase() + ": " + resultuserinfo.getString("username") + " | Recent play");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String dateString = format.format(new Date());
                Date date = null;
                try {
                    date = format.parse(result.getString("date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Timestamp ts = new Timestamp(date.getTime());


                eb.setDescription("most Recent Gameplay from " + new TimestampResolver(ts).resolvewithTime());
                eb.setImage("https://assets.ppy.sh/beatmaps/" + resultbeatmapinfo.getString("beatmapset_id") + "/covers/card.jpg");
                eb.addInlineField("Author - Title - Map Creator", "[" + resultbeatmapinfo.getString("artist") + " - " + resultbeatmapinfo.getString("title") + "](https://osu.ppy.sh/beatmapsets/" + resultbeatmapinfo.getString("beatmapset_id") + "#osu/" + result.getString("beatmap_id") + ") by [" + resultbeatmapinfo.getString("creator") + "](https://osu.ppy.sh/users/" + resultbeatmapinfo.getString("creator_id") + ")");
                eb.addInlineField("Difficulty", resultbeatmapinfo.getString("difficultyrating"));
                String ranking = result.getString("rank");
                String Rank;
                switch (ranking) {
                    case ("XH"):
                        Rank = "<:rankingXH:692418403994173451>";
                        break;
                    case ("X"):
                        Rank = "<:rankingX:692415554723643512>";
                        break;
                    case ("SH"):
                        Rank = "<:rankingSH:692415554065006694>";
                        break;
                    case ("S"):
                        Rank = "<:rankingS:692415554807398500>";
                        break;
                    case ("A"):
                        Rank = "<:rankingA:692415554841083954>";
                        break;
                    case ("B"):
                        Rank = "<:rankingB:692415554665054298>";
                        break;
                    case ("C"):
                        Rank = "<:rankingC:692415554341830666>";
                        break;
                    case ("D"):
                        Rank = "<:rankingD:692415554627305472> ";
                        break;
                    default:
                        Rank = "FAILED";
                        break;

                }
                eb.addInlineField("Rank", Rank);
                eb.addInlineField("Score", result.getString("score"));
                eb.addField("Max Combo", result.getString("maxcombo"));
                eb.addInlineField("Missed", result.getString("countmiss"));
                msg.edit(eb);


            } catch (JSONException e) {
                e.printStackTrace();
                msg.edit(Templates.argerrorembed().setDescription("**The osu! Player** ``" + username.replace("_", " ") + "`` **does not exist or this User hasn't played a Map recently.**"));

            }

        }
    }

    public void getUser(String username) {
        String ID = event.getServer().get().getIdAsString();

        if (new cooldownutility(ID).chkcooldown(CooldownManager.TYPE.OSU) != 0.0) {
            event.getChannel().sendMessage(Templates.cooldownerrorembed("osu", Cooldown.def.get(CooldownManager.TYPE.OSU), new cooldownutility(ID).chkcooldown(CooldownManager.TYPE.OSU)));


        } else {
            new cooldownutility(ID).cooldownreset(CooldownManager.TYPE.OSU);


            msg = event.getChannel().sendMessage(Templates.defaultembed().setDescription("Querying User Profile ``" + username.replace("%20", " ") + "``, please wait..").setThumbnail("https://i.pinimg.com/originals/3e/f0/e6/3ef0e69f3c889c1307330c36a501eb12.gif")).join();
            String apikey = Main.OSUAPIKEY;
            try {
                EmbedBuilder eb = Templates.defaultembed();
                eb.setColor(Color.pink);

                JSONArray jsonarray = new JSONArray(makeRequest.getResponse("https://osu.ppy.sh/api/get_user?k=" + apikey + "&u=" + username.replace(" ", "%20"), OSU_PPY_SH.class, event));

                JSONObject result = jsonarray.getJSONObject(0);
                JSONArray jsonarray1 = new JSONArray(makeRequest.getResponse("https://osu.ppy.sh/api/get_user_best?limit=1&k=" + apikey + "&u=" + username, OSU_PPY_SH.class, event));

                JSONObject result1 = jsonarray.getJSONObject(0);
                MiscVariables.jsonObjectHashMapGetBest.put(event.getMessageAuthor().getIdAsString(), result1);
                MiscVariables.jsonObjectHashMap.put(event.getMessageAuthor().getIdAsString(), result);
                eb.setTitle("<:osuicon:692410518090022944> :flag_" + result.getString("country").toLowerCase() + ": " + result.getString("username"));

                eb.setDescription("osu! Profile of User ID ``" + result.getString("user_id") + "``");


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateString = format.format(new Date());
                Date date = null;
                try {
                    date = format.parse(result.getString("join_date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Timestamp ts = new Timestamp(date.getTime());

                eb.addInlineField("osu! Player since", new TimestampResolver(ts).resolve());

                int seconds = result.getInt("total_seconds_played");

                StringBuilder sb = new StringBuilder();
                if (TimeUnit.SECONDS.toDays(seconds) != 0) {
                    sb.append(TimeUnit.SECONDS.toDays(seconds) + " Day(s), ");
                }
                if (TimeUnit.SECONDS.toHours(seconds) - (TimeUnit.SECONDS.toDays(seconds) * 24) != 0) {
                    sb.append(TimeUnit.SECONDS.toHours(seconds) - (TimeUnit.SECONDS.toDays(seconds) * 24) + " Hour(s), ");
                }
                if (TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60) != 0) {
                    sb.append(TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60) + " Minute(s), and ");
                }
                if (TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60) != 0) {
                    sb.append(TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60) + " Second(s)");
                }

                eb.setThumbnail("https://a.ppy.sh/" + result.getString("user_id"));

                eb.addInlineField("Total Time played", sb.toString());
                DecimalFormat df = new DecimalFormat("####");
                eb.addInlineField("Level", df.format(result.getDouble("level")));
                StringBuilder sb2 = new StringBuilder();
                try {
                    sb2.append("<:rankingXH:692418403994173451> " + result.getString("count_rank_ssh") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingX:692415554723643512> " + result.getString("count_rank_ss") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingSH:692415554065006694> " + result.getString("count_rank_sh") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingS:692415554807398500> " + result.getString("count_rank_s") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingA:692415554841083954> " + result.getString("count_rank_a") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingB:692415554665054298> " + result.getString("count_rank_b") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingC:692415554341830666> " + result.getString("count_rank_c") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingD:692415554627305472> " + result.getString("count_rank_d") + "\n");
                } catch (JSONException e) {

                }

                eb.addField("Ranking", sb2.toString());
                eb.addField("Global Rank / Country Rank (:flag_" + result.getString("country").toLowerCase() + ":)", "#" + result.getString("pp_rank") + " / #" + result.getString("pp_country_rank"), true);
                eb.addField("Performance Points (pp)", result.getString("pp_raw") + "pp", true);
                eb.addField("Plays", result.getString("playcount"), true);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("<:hit50:692449536907673601> " + result.getInt("count50") + "\n");
                sb4.append("<:hit100:692449537003880588> " + result.getInt("count100") + "\n");
                sb4.append("<:hit300:692449536811204678> " + result.getInt("count300") + "\n");

                eb.addField("Hits", sb4.toString()+"\n \n \n \n \n \n", true);
                eb.addField("Accuracy", df.format(result.getDouble("accuracy")), true);
                eb.addInlineField("Ranked Score / Total Score", result.getDouble("ranked_score") + " / " + result.getDouble("total_score"));
                eb.addInlineField("\u200B", "\u200B\n");
                eb.addInlineField("\u200B", "\u200B\n");
                eb.addInlineField("\u200B", "\u200B\n");


                msg.edit(eb);
                MiscVariables.osureactmessage.put(event.getServer().get().getIdAsString(), msg);
                MiscVariables.osuapieventholder.put(event.getServer().get().getIdAsString(), event);
                MiscVariables.osuuserid.put(event.getServer().get().getIdAsString(), result.getString("user_id"));
                MiscVariables.editosureactor.put(event.getServer().get().getIdAsString(), msg);
                wentthr = true;
                // event.getChannel().sendMessage(result.getString("username"));


            } catch (JSONException e) {

                msg.edit(Templates.argerrorembed().setDescription("**The osu! Player** ``" + username.replace("_", " ") + "`` **does not exist.**"));

            }

        }
    }


    public static void getUserBest(String username, String id, JSONObject result) {
    if (id == null) {

    } else {

        EmbedBuilder eb = Templates.defaultembed();
        eb.setTitle("<:osuicon:692410518090022944> Best Play | :flag_" + result.getString("country").toLowerCase() + ": " + result.getString("username"));
        eb.setFooter("This Command has a 6 Second Cooldown | Franxxmin | HopeDev | Version ID: "+Main.versionid);
        eb.setDescription("Work in Progress, not usable yet!");
        MiscVariables.editosureactor.get(event.getServer().get().getIdAsString()).edit(eb);
    }


    
    }

    public void getUserNoCreate(JSONObject result) {



            String apikey = "2e7283537383343f7c07a623915a91b05a026079";
            try {
                EmbedBuilder eb = Templates.defaultembed();
                eb.setColor(Color.pink);



                eb.setTitle("<:osuicon:692410518090022944> :flag_" + result.getString("country").toLowerCase() + ": " + result.getString("username"));

                eb.setDescription("osu! Profile of User ID ``" + result.getString("user_id") + "``");


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateString = format.format(new Date());
                Date date = null;
                try {
                    date = format.parse(result.getString("join_date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Timestamp ts = new Timestamp(date.getTime());

                eb.addInlineField("osu! Player since", new TimestampResolver(ts).resolve());

                int seconds = result.getInt("total_seconds_played");

                StringBuilder sb = new StringBuilder();
                if (TimeUnit.SECONDS.toDays(seconds) != 0) {
                    sb.append(TimeUnit.SECONDS.toDays(seconds) + " Day(s), ");
                }
                if (TimeUnit.SECONDS.toHours(seconds) - (TimeUnit.SECONDS.toDays(seconds) * 24) != 0) {
                    sb.append(TimeUnit.SECONDS.toHours(seconds) - (TimeUnit.SECONDS.toDays(seconds) * 24) + " Hour(s), ");
                }
                if (TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60) != 0) {
                    sb.append(TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60) + " Minute(s), and ");
                }
                if (TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60) != 0) {
                    sb.append(TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60) + " Second(s)");
                }

                eb.setThumbnail("https://a.ppy.sh/" + result.getString("user_id"));

                eb.addInlineField("Total Time played", sb.toString());
                DecimalFormat df = new DecimalFormat("####");
                eb.addInlineField("Level", df.format(result.getDouble("level")));
                StringBuilder sb2 = new StringBuilder();
                try {
                    sb2.append("<:rankingXH:692418403994173451> " + result.getString("count_rank_ssh") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingX:692415554723643512> " + result.getString("count_rank_ss") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingSH:692415554065006694> " + result.getString("count_rank_sh") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingS:692415554807398500> " + result.getString("count_rank_s") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingA:692415554841083954> " + result.getString("count_rank_a") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingB:692415554665054298> " + result.getString("count_rank_b") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingC:692415554341830666> " + result.getString("count_rank_c") + "\n");
                } catch (JSONException e) {

                }
                try {
                    sb2.append("<:rankingD:692415554627305472> " + result.getString("count_rank_d") + "\n");
                } catch (JSONException e) {

                }

                eb.addField("Ranking", sb2.toString());
                eb.addField("Global Rank / Country Rank (:flag_" + result.getString("country").toLowerCase() + ":)", "#" + result.getString("pp_rank") + " / #" + result.getString("pp_country_rank"), true);
                eb.addField("Performance Points (pp)", result.getString("pp_raw") + "pp", true);
                eb.addField("Plays", result.getString("playcount"), true);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("<:hit50:692449536907673601> " + result.getInt("count50") + "\n");
                sb4.append("<:hit100:692449537003880588> " + result.getInt("count100") + "\n");
                sb4.append("<:hit300:692449536811204678> " + result.getInt("count300") + "\n");

                eb.addField("Hits", sb4.toString(), true);
                eb.addField("Accuracy", df.format(result.getDouble("accuracy")), true);
                eb.addInlineField("Ranked Score / Total Score", result.getDouble("ranked_score") + " / " + result.getDouble("total_score"));
                eb.addInlineField("\u200B", "\u200B");
                eb.addInlineField("\u200B", "\u200B");
                eb.addInlineField("\u200B", "\u200B");

                msg.edit(eb);
                MiscVariables.osureactmessage.put(event.getServer().get().getIdAsString(), msg);
                MiscVariables.osuapieventholder.put(event.getServer().get().getIdAsString(), event);
                MiscVariables.osuuserid.put(event.getServer().get().getIdAsString(), result.getString("user_id"));
                wentthr = true;
                // event.getChannel().sendMessage(result.getString("username"));


            } catch (JSONException e) {

                msg.edit(Templates.argerrorembed().setDescription("**The osu! Player** ``" + username.replace("_", " ") + "`` **does not exist.**"));
            }
        }

    public enum WhatIWant {
        GET_USER,
        GET_RECENT
    }

}
