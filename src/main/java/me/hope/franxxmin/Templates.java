package me.hope.franxxmin;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.text.DecimalFormat;


// Simple Template to make working with Embeds, Prefixes, etc easier :)
public class Templates {

    public static EmbedBuilder defaultembed() {
        System.out.println("getting default embed");

        EmbedBuilder prefembed = new EmbedBuilder().removeAllFields().setFooter("Mope | HopeDev | Version ID: " + Main.versionid);
        return prefembed;
    }

    public static EmbedBuilder higherroleerror(String type) {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.yellow).setTitle("\u26D4 Missing Authority");
        tempeb.setDescription("I cannot " + type + " this user, they have a higher Role than me!");

        return tempeb;
    }

    public static EmbedBuilder kickEmbed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.yellow).setTitle("\uD83E\uDD7E Kicked User");

        return tempeb;
    }

    public static EmbedBuilder punishmentEmbed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.red).setTitle("\u2757 Important Note");

        return tempeb;
    }

    public static EmbedBuilder missingperms(String... Permnames) {
        StringBuilder sb = new StringBuilder();
        for (String x : Permnames) {
            sb.append("```- " + x + "```\n");
        }
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.yellow).setTitle("\u26D4 Missing Permissions");
        tempeb.setDescription("The Bot is missing the following Permissions:\n" + sb.toString());

        return tempeb;
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
        tempeb.addField("mp> users", "Get a count of every user in your server.");
        tempeb.addField("mp> userinfo @mention", "Shows global and local informations about a mentioned user.");
        tempeb.addField("fmdebug>", "Only usable for Bot Owner");
        tempeb.addField("mp> about", "about this Bot.");


        return tempeb;
    }
    public static EmbedBuilder aboutembed() {
        EmbedBuilder tempeb = defaultembed();
        tempeb.setColor(Color.MAGENTA).setTitle("\uD83E\uDDED About this Bot");
        tempeb.setThumbnail(Main.api.getOwner().join().getAvatar());
        tempeb.setDescription("This Bot was made with \u2764 by HopeDev\n \nDiscord: Hope#1445\nEmail: hopedevmail@yahoo.com\n[Development Server](https://discord.gg/S3q8ryY)\n \n[Mope on top.gg](https://top.gg/bot/688561837020545080)\n");
        tempeb.addField("Planned Features", "- Custom Prefix (Done)\n- Automoderation\n- React Roles\n- Image Manipulation (Partially done)\n- Moderation\n- Raid Protection (partially implemented into Moderation)\n");


        return tempeb;
    }



}
