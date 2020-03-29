package me.hope.franxxmin.utils.RequestLibrary.waitforosu;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.utils.RequestLibrary.OSU_PPY_SH;
import me.hope.franxxmin.utils.VariablesStorage.MiscVariables;
import me.hope.franxxmin.utils.cooldownutility;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.json.JSONObject;

import java.util.HashMap;

public class osureactor implements ReactionAddListener {
private static HashMap<String, Message> map = MiscVariables.osureactmessage;

    @Override
    public void onReactionAdd(ReactionAddEvent event) {
        if (event.getUser().isYourself()) {

        } else {
            System.out.println("Yes!");
            Message thismessage = event.getMessage().get();
            Message equalmsg = map.get(event.getServer().get().getIdAsString());
            MessageCreateEvent preevent = MiscVariables.osuapieventholder.get(event.getServer().get().getIdAsString());
            String useridforres = MiscVariables.osuuserid.get(event.getServer().get().getIdAsString());
            if (thismessage.equals(equalmsg)) {
                event.removeReaction();

              /*  if (event.getEmoji().asCustomEmoji().get().getIdAsString().equals("692820580336009267")) {
                    if (new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown("osu-react-command") != 0.0) {
                        event.removeReaction();

                    } else {
                        new cooldownutility(event.getServer().get().getIdAsString()).cooldownreset("osu-react-command");

                        System.out.println("Sdodosdpakwfekowefio");
                        if (Main.osuembedpage.getOrDefault(event.getServer().get().getIdAsString(), 1) == 1) {
                            Main.osuembedpage.put(event.getServer().get().getIdAsString(), 2);

                            new OSU_PPY_SH(useridforres, preevent, equalmsg).getUserBest(useridforres, event.getServer().get().getIdAsString(),Main.jsonObjectHashMapGetBest.get(event.getUser().getIdAsString()));
                        } else if (Main.osuembedpage.get(event.getServer().get().getIdAsString()) == 2) {
                            Main.osuembedpage.put(event.getServer().get().getIdAsString(), 1);
                            new OSU_PPY_SH(useridforres, preevent, equalmsg).getUserNoCreate(Main.jsonObjectHashMap.get(event.getUser().getIdAsString()));
                        }
                    }
                }

               */
            }


        }

    }
}
