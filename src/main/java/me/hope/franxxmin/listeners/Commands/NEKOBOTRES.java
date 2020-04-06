package me.hope.franxxmin.listeners.Commands;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.utils.RequestLibrary.makeRequest;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.JSONObject;

import java.awt.*;

public class NEKOBOTRES {
    private static MessageCreateEvent event;

    public NEKOBOTRES(MessageCreateEvent event) {
        NEKOBOTRES.event = event;
    }

    public static String base = "https://nekobot.xyz/api/imagegen?type=";

    public static void baguette() {
        Message msg = null;
        String type = "baguette";
        String url;

        msg = event.getChannel().sendMessage(Templates.infoembed().setDescription("Loading, please wait...")).join();

        if (event.getMessage().getMentionedUsers().isEmpty()) {

            url = event.getMessageAuthor().getAvatar().getUrl().toString();
        } else {
            url = event.getMessage().getMentionedUsers().get(0).getAvatar().getUrl().toString();
        }


        String resp = makeRequest.getResponse(base + type + "&url=" + url, NEKOBOTRES.class, event);

        JSONObject jsonObj = new JSONObject(resp);

        String result = jsonObj.getString("message");

        msg.edit(Templates.defaultembed().setDescription("It's a baguette, not bread :) though, baguette is a bread.").setImage(result).setColor(Color.green).setFooter("Using nekobot.xyz API | Franxxmin | HopeDev | Version ID: " + Main.versionid));

    }

    public static void magik() {
        Message msg = null;
        String[] str = event.getMessageContent().split(" ");

        try {


            Integer.valueOf(str[2]);

            msg = event.getChannel().sendMessage(Templates.infoembed().setDescription("Loading, please wait...")).join();
            String type = "magik";
            String intensity = str[2];
            String resp = null;

            if (event.getMessage().getAttachments().isEmpty() && event.getMessage().getMentionedUsers().isEmpty()) {
                resp = makeRequest.getResponse(base + type + "&image=" + event.getMessageAuthor().getAvatar().getUrl() + "&intensity=" + intensity, NEKOBOTRES.class, event);
            } else if (event.getMessage().getMentionedUsers().isEmpty() && !event.getMessage().getAttachments().isEmpty()) {
                resp = makeRequest.getResponse(base + type + "&image=" + event.getMessage().getAttachments().get(0).getUrl() + "&intensity=" + intensity, NEKOBOTRES.class, event);
            } else {
                resp = makeRequest.getResponse(base + type + "&image=" + event.getMessage().getMentionedUsers().get(0).getAvatar().getUrl() + "&intensity=" + intensity, NEKOBOTRES.class, event);

            }


            JSONObject jsonObj = new JSONObject(resp);

            String result = jsonObj.getString("message");


            msg.edit(Templates.defaultembed().setDescription("I don't even know anymore.").setImage(result).setColor(Color.green).setFooter("Using nekobot.xyz API | Franxxmin | HopeDev | Version ID: " + Main.versionid));


        } catch (NumberFormatException e) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Wrong usage!\n \nUsage: fm> magik <intensity> @mention(optional, would use your avatar or attached image)\n \n**Usage example: fm> magik 2 " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
            msg.delete();
        }
    }
}
