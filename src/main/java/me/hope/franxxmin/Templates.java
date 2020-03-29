package me.hope.franxxmin;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.text.DecimalFormat;


// Simple Template to make working with Embeds, Prefixes, etc easier :)
public class Templates {

    public static EmbedBuilder defaultembed() {
        EmbedBuilder prefembed = new EmbedBuilder().removeAllFields().setFooter("Franxxmin | HopeDev | Version ID: "+Main.versionid);
        return prefembed;
    }


    public static EmbedBuilder infoembed() {

        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.yellow).setTitle("\u2139 Information");

        return tempeb;
    }
    public static EmbedBuilder minerrorembed() {

        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.orange).setTitle("\u26A0 Minor error occured");

        return tempeb;
    }

    public static EmbedBuilder majerrorembed() {

        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.red).setTitle("\u2757 Major error occured");

        return tempeb;
    }
    public static EmbedBuilder norembed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.white);

        return tempeb;
    }

    public static EmbedBuilder permerrorembed() {

        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.red).setTitle("\u274C Not permitted!");

        return tempeb;
    }
    public static EmbedBuilder cooldownerrorembed(String command, double defaultcooldown, double servercooldownleft) {

        EmbedBuilder tempeb = defaultembed();
        DecimalFormat df = new DecimalFormat("####0.0");
        tempeb.setColor(Color.red).setDescription("\uD83D\uDD51 Woah! That's too fast!\n \nCooldown for ``"+command+"``: ``"+defaultcooldown+"`` seconds.\n \nPlease retry in ``"+df.format(servercooldownleft).replace(",",".")+"`` seconds.");

        return tempeb;
    }
    public static EmbedBuilder debugembed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.cyan).setTitle("\uD83D\uDD27 Debugging");

        return tempeb;
    }
    public static EmbedBuilder argerrorembed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.red).setTitle("\u2753 Usage Error");

        return tempeb;
    }
    public static EmbedBuilder helpembed() {
        EmbedBuilder tempeb = defaultembed();

        tempeb.setColor(Color.MAGENTA).setTitle("\u2139 General Commands");
        tempeb.setDescription("Here's a list of available Commands:");
        tempeb.addField("fm> users", "Get a count of every user in your server.");
        tempeb.addField("fm> userinfo @mention", "Shows global and local informations about a mentioned user.");
        tempeb.addField("fmdebug>", "Only usable for Bot Owner");
        tempeb.addField("fm> about", "about this Bot.");


        return tempeb;
    }
    public static EmbedBuilder aboutembed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.MAGENTA).setTitle("\uD83E\uDDED About this Bot");
        tempeb.setThumbnail(Main.api.getOwner().join().getAvatar());
        tempeb.setDescription("This Bot was made with \u2764 by HopeDev\n \nDiscord: Hope#1445\nEmail: hopedevmail@yahoo.com\n[Development Server](https://discord.gg/xAURjuc)\n[Alternative Server](https://discord.gg/RSfuEgq)\n \nThanks to [nekobot.xyz](https://nekobot.xyz) for making their API available for everyone! <3\n \nCredit for the Avatar goes to [u/youregretti](https://www.reddit.com/r/DarlingInTheFranxx/comments/8nnvgs/dr_frank_werner_dr_franxx_minimalistic_wallpaper/)");
        tempeb.addField("Planned Features", "- Custom Prefix (Done)\n- Automoderation\n- React Roles\n- Image Manipulation (Partially done)\n- Moderation\n- Raid Protection (partially implemented into Moderation)\n");


        return tempeb;
    }



}
