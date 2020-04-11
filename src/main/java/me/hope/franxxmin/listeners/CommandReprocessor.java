package me.hope.franxxmin.listeners;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.MusicPack.Music;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.listeners.Commands.Defaults;
import me.hope.franxxmin.listeners.Commands.Moderation.Moderation;
import me.hope.franxxmin.listeners.Commands.Status;
import me.hope.franxxmin.onStart.CooldownManager;
import me.hope.franxxmin.utils.RequestLibrary.APIAccess;
import me.hope.franxxmin.utils.RequestLibrary.OSU_PPY_SH;
import me.hope.franxxmin.utils.VariablesStorage.Cooldown;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;
import me.hope.franxxmin.utils.cooldownutility;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

public class CommandReprocessor {

//todo make everything look better


    /* Changes made:
    - 23.03.20 made the "isYourself" check on event initialization
    - 23.03.20 made the "String[] str" variable on event initialization
    - 23.03.20 easier usability of "str" by removing the prefix from the actual string list :) I hate myself
    - 23.03.20 added Caller Information for API Requesting
    - 29.03.20 Reworked everything with enums for better visibility
    - DONE 24.03.20 todo Cooldowns. Very important to avoid abuse (Use a hashmap with <String(Serverid)> <ArrayList(each type of cooldown for explample status or so)> (Done)
     */
    public CommandReprocessor(MessageCreateEvent event, String[] str, String[] strraw) {

        String serverid = event.getServer().get().getIdAsString();
        Preferences prefix = Main.pref.node(serverid);
        String Pstr = prefix.get("prefix", Main.dfprfx);

        if (strraw[0].equalsIgnoreCase(Pstr) || strraw[0].equalsIgnoreCase(Main.dfprfx)) {
            EmbedBuilder log = Templates.debugembed();

            log.setDescription("Command executed");
            log.addField("Guild (ID)", event.getServer().get().getName() + " (" + event.getServer().get().getIdAsString() + ")");
            log.addInlineField("Channel (ID)", event.getChannel().asServerTextChannel().get().getName() + " (" + event.getChannel().getIdAsString() + ")");
            log.addInlineField("Message Author (ID)", event.getMessageAuthor().getDiscriminatedName() + " (" + event.getMessageAuthor().getIdAsString() + " )");
            log.addField("Message ID", event.getMessage().getIdAsString());
            log.addInlineField("Message URL", "https://discordapp.com/channels/" + event.getServer().get().getIdAsString() + "/" + event.getChannel().getIdAsString() + "/" + event.getMessage().getIdAsString());
            log.addField("Commandtext", event.getMessageContent());
            if (Main.debug.getBoolean("enabled", true)) {
                Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(log);

            }

            if (str.length == 0) {

                new Defaults(event).allHelpPages(Pstr);
                event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));
                return;
            }

            if (str[0].equalsIgnoreCase("help")) {

                if (str.length == 1) {
                    // Send help pages obv
                    new Defaults(event).allHelpPages(Pstr);
                    event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));
                    return;
                }

                // Seperate Help Pages (made it very complicated for unknown reasons) :)
                if (str[1].equalsIgnoreCase("interactive")) {
                    new Defaults(event).interactivehelpPage(Pstr);
                    event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));
                    return;
                }
                if (str[1].equalsIgnoreCase("general")) {
                    new Defaults(event).generalhelpPage(Pstr);
                    event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));
                    return;
                }
                if (str[1].equalsIgnoreCase("moderation")) {
                    new Defaults(event).moderationhelpPage(Pstr);
                    event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));
                    return;
                }
                if (str[1].equalsIgnoreCase("osu")) {
                    new Defaults(event).osuhelpPage(Pstr);
                    return;
                }
                new Defaults(event).allHelpPages(Pstr);
                event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));
                return;
            }

            if (str[0].equalsIgnoreCase("users")) {
                if (new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.USERS) != 0.0) {

                    event.getChannel().sendMessage(Templates.cooldownerrorembed("users", Cooldown.def.get(CooldownManager.TYPE.USERS), new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.USERS)));
                    return;
                }
                //Usercount
                new cooldownutility(serverid).cooldownreset(CooldownManager.TYPE.USERS);
                new Defaults(event).usercount();
                return;
            }


            if (str[0].equalsIgnoreCase("userinfo")) {
                if (new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.STATUS) != 0.0) {

                    event.getChannel().sendMessage(Templates.cooldownerrorembed("users", Cooldown.def.get(CooldownManager.TYPE.USERS), new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.USERS)));
                    return;
                }

                new cooldownutility(serverid).cooldownreset(CooldownManager.TYPE.USERS);
                new Defaults(event).userinfo();
                return;
            }
            // Userinfo
            if (str[0].equalsIgnoreCase("about")) {
                // About page of the Bot
                event.getChannel().sendMessage(Templates.aboutembed());
                return;
            }
            if (str[0].equalsIgnoreCase("prefix")) {

                if (!event.getMessageAuthor().isServerAdmin()) {
                    event.getChannel().sendMessage(Templates.permerrorembed().setDescription("This command can only be used by Guild admins."));
                    return;
                }
                if (str.length == 1) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Current Prefix for**" + event.getServer().get().getName() + "** is ``" + Pstr + "``\n \n name a Prefix you want the bot to listen to\n \n**Usage example: mp> prefix** ``Mope>`` \nResult: ``Mope>`` help\n \n \n_Hint: You can always use mp> anyways._"));
                    return;
                }
                if (str[1].equalsIgnoreCase("reset")) {
                    prefix.remove("prefix");
                    event.getChannel().sendMessage(Templates.defaultembed().setColor(Color.green).setDescription("Command Prefix for ``" + event.getServer().get().getName() + "`` has been set to the default ``mp> ``"));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < str.length; i++) {
                    sb.append(str[i]);
                }
                prefix.put("prefix", sb.toString());
                event.getChannel().sendMessage(Templates.defaultembed().setColor(Color.green).setDescription("Command Prefix for ``" + event.getServer().get().getName() + "`` successfully set to ``" + sb.toString() + "``"));
                return;
            }
            if (str[0].equalsIgnoreCase("status")) {
                if (new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.STATUS) != 0.0) {

                    event.getChannel().sendMessage(Templates.cooldownerrorembed("status", Cooldown.def.get(CooldownManager.TYPE.STATUS), new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.STATUS)));
                    return;
                }

                new cooldownutility(serverid).cooldownreset(CooldownManager.TYPE.STATUS);

                // Get the API Status like APIRT or Resources count(gifs and such on api)
                //todo extra Klasse dafuer --> Erledigt!!
                event.getChannel().sendMessage(Status.request(event.getServer().get().getIdAsString(), event));
                return;
            }

            if (str[0].equalsIgnoreCase("hug")) {

                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " hug " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot hug yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }


                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " hugs " + sb.toString() + "! *How cute~*", new APIAccess(event).hug());
                return;
            }

            if (str[0].equalsIgnoreCase("kiss")) {
                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to matleast mention one user!\n \n**Usage example: " + Pstr + " kiss " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot kiss yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " kisses " + sb.toString() + "! *Mwah~*", new APIAccess(event).kiss());
                return;


            }
            if (str[0].equalsIgnoreCase("pat")) {
                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " pat " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot pat yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " pats " + sb.toString() + "! *Headpats are always nice!~*", new APIAccess(event).pat());
                return;

            }
            if (str[0].equalsIgnoreCase("poke")) {

                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " poke " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot poke yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " pokes " + sb.toString() + "! *Hey, wake up!!*", new APIAccess(event).poke());
                return;

            }
            if (str[0].equalsIgnoreCase("tickle")) {
                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " tickle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot tickle yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " tickles " + sb.toString() + "! *Tickle tickle~*", new APIAccess(event).tickle());
                return;

            }
            if (str[0].equalsIgnoreCase("cry")) {
                new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is crying. _Don't be sad!! Everyone is here for you :)_", new APIAccess(event).cry());
                return;

            }
            if (str[0].equalsIgnoreCase("smug")) {

                new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " smugs", new APIAccess(event).smug());
                return;
            }
            if (str[0].equalsIgnoreCase("feed")) {
                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " feed " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot feed yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " feeds " + sb.toString() + "! *Yummy~*", new APIAccess(event).feed());
                return;


            }
            if (str[0].equalsIgnoreCase("cuddle")) {
                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " cuddle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot cuddle yourself :("));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " cuddles " + sb.toString() + "! *Warmth!~*", new APIAccess(event).cuddle());
                return;


            }
            if (str[0].equalsIgnoreCase("blush")) {
                new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is blushing", new APIAccess(event).blush());
                return;
            }
            if (str[0].equalsIgnoreCase("laugh")) {

                new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is laughing", new APIAccess(event).laugh());
                return;

            }
            if (str[0].equalsIgnoreCase("lick")) {
                Object[] list = event.getMessage().getMentionedUsers().toArray();
                if (list.length == 0) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " lick " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    return;
                }
                List<User> actionrecievers = event.getMessage().getMentionedUsers();
                User actioninitializer = event.getMessageAuthor().asUser().get();

                if (actionrecievers.contains(actioninitializer)) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot lick yourself!"));
                    return;
                }
                if (actionrecievers.contains(Main.api.getYourself())) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));
                    return;
                }
                int i = actionrecievers.size();
                int fin = 0;
                StringBuilder sb = new StringBuilder();
                if (actionrecievers.size() == 1) {
                    sb.append("" + actionrecievers.get(0).getMentionTag());
                } else {
                    for (int n = 0; n < i - 1; n++) {
                        sb.append(actionrecievers.get(n).getMentionTag() + " ");
                        fin++;
                    }
                    sb.append(" and " + actionrecievers.get(fin).getMentionTag());
                }
                new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " licks " + sb.toString() + "! *You're quite tasty!*", new APIAccess(event).lick());
                return;

            }
            if (str[0].equalsIgnoreCase("happy")) {
                new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is happy! *I'm glad you are.", new APIAccess(event).happy());
                return;
            }
            if (str[0].equalsIgnoreCase("baguette")) {
                event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));

                return;
                //  new NEKOBOTRES(event).baguette();

            }
            if (str[0].equalsIgnoreCase("magik")) {

                event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));
                return;
                // new NEKOBOTRES(event).magik();


            }
            if (str[0].equalsIgnoreCase("osu")) {
                if (str.length == 1) {
                    new Defaults(event).osuhelpPage(Pstr);
                    return;
                }

                if (str[1].equalsIgnoreCase("user")) {
                    if (str.length == 2) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to either enter a username or a userid!\n \n**Example: **_" + Pstr + " osu user HopeDev_"));
                        return;
                    }
                    if (new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU) != 0.0) {

                        event.getChannel().sendMessage(Templates.cooldownerrorembed("osu", Cooldown.def.get(CooldownManager.TYPE.OSU), new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU)));

                        return;
                    }
                    OSU_PPY_SH timer;
                    StringBuilder sb = new StringBuilder();
                    int i = 2;
                    for (i = 2; i < str.length; i++) {
                        sb.append(str[i] + "_");
                    }
                    String sendthis = sb.toString().substring(0, sb.toString().length() - 1);

                    // Ab jetzt wird "Timer"
                    // im Hintergrund ausgegeben:
                    timer = new OSU_PPY_SH(sendthis, event, null, OSU_PPY_SH.WhatIWant.GET_USER);
                    timer.setName(UUID.randomUUID() + "");
                    timer.start();

                    return;

                }
                if (str[1].equalsIgnoreCase("recent")) {
                    if (str.length == 2) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to either enter a username or a userid!\n \n**Example: **_" + Pstr + " osu recent HopeDev_"));
                        return;

                    }
                    if (new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU) != 0.0) {

                        event.getChannel().sendMessage(Templates.cooldownerrorembed("osu", Cooldown.def.get(CooldownManager.TYPE.OSU), new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU)));

                        return;
                    }
                    OSU_PPY_SH timer;
                    StringBuilder sb = new StringBuilder();
                    int i = 2;
                    for (i = 2; i < str.length; i++) {
                        sb.append(str[i] + "_");
                    }
                    String sendthis = sb.toString().substring(0, sb.toString().length() - 1);

                    // Ab jetzt wird "Timer"
                    // im Hintergrund ausgegeben:
                    timer = new OSU_PPY_SH(sendthis, event, null, OSU_PPY_SH.WhatIWant.GET_RECENT);
                    timer.setName(UUID.randomUUID() + "");
                    timer.start();


                    return;
                }
                new Defaults(event).osuhelpPage(Pstr);
                return;

            }
            if (str[0].equalsIgnoreCase("debug")) {
                if (event.getMessageAuthor().isBotOwner()) {
                    if (Main.debug.getBoolean("enabled", true)) {
                        Main.debug.putBoolean("enabled", false);
                        event.getChannel().sendMessage("Debug disabled.");
                        System.out.println("Debug being toggled [FALSE]");
                        EmbedBuilder eb = new Templates().infoembed().setDescription("Debugging toggled.");
                        Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb).join();

                    } else {
                        event.getChannel().sendMessage("Debug enabled.");
                        Main.debug.putBoolean("enabled", true);
                        EmbedBuilder eb = new Templates().infoembed().setDescription("Debugging toggled.");
                        Main.api.getChannelById("698308561733812274").get().asServerTextChannel().get().sendMessage(eb).join();
                        System.out.println("Debug being toggled [TRUE]");
                    }
                    return;

                }
                return;
            }
            if (str[0].equalsIgnoreCase("pushupdate")) {
                if (event.getMessageAuthor().isBotOwner()) {
                    if (Main.localmode) {
                        return;
                    }
                    EmbedBuilder eb = Templates.debugembed();
                    eb.setDescription("Shutting down Bot and updating through Github..");
                    event.getChannel().sendMessage(eb);
                    Main.UpdatedServerID.put("id", event.getChannel().getIdAsString());

                    try {
                        Runtime.getRuntime().exec("initupdate");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);


                    return;
                }


                //TODO MODERATION SHIT HERE LMAOOO
                return;
            }
            if (str[0].equalsIgnoreCase("clearchat")) {
                new Moderation(event, str, Pstr).clearChat();
                return;
            }
            if (str[0].equalsIgnoreCase("kick")) {
                new Moderation(event, str, Pstr).kickUser();
                return;
            }
            if (str[0].equalsIgnoreCase("ban")) {
                new Moderation(event, str, Pstr).banUser();
                return;
            }
            if (str[0].equalsIgnoreCase("mute")) {
                new Moderation(event, str, Pstr).permMuteUser();
                return;
            }
            if (str[0].equalsIgnoreCase("unmute")) {
                new Moderation(event, str, Pstr).unmuteUser();
                return;
            }
            if (str[0].equalsIgnoreCase("music")) {

                if (str.length == 1) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Commands:\n \n" + Pstr + " music play <URL or Keyword>\n" + Pstr + " music pause/unpause\n\n\n**PLEASE DM ME ON ANY ERRORS! --> Hope#1445**").setTitle("FEATURE IN BETA!!!"));
                }
                Music.MusicHandler(event, str, strraw);
                return;
            }
            if (str[0].equalsIgnoreCase("blacklist")) {
                if (!event.getMessageAuthor().isBotOwner()) {
                    return;
                }
                Preferences pref = ServerHashmaps.blacklist;
                if (pref.getBoolean(str[1], false)) {
                    pref.putBoolean(str[1], false);
                    event.getChannel().sendMessage("Guild ID " + str[1] + " unblacklisted");
                    return;
                } else if (!pref.getBoolean(str[1], false)) {
                    pref.putBoolean(str[1], true);
                    boolean couldmsg = false;
                    boolean couldmessage = false;
                    boolean farmcheck = false;
                    if (Main.api.getServerById(str[1]).get().getMembers().contains(Main.api.getYourself())) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 2; i < str.length; i++) {
                            sb.append(str[i] + " ");
                        }
                        try {
                            Main.api.getServerById(str[1]).get().getOwner().sendMessage("Hello " + Main.api.getServerById(str[1]).get().getOwner().getName() + ", \n \nYour Guild, " + event.getServer().get().getName() + " has been blacklisted from using this Bot by \"BOT OWNER\": \n \n **Reason:** _" + sb.toString() + "_\n \n This Bot cannot be reinvited until unblacklisted!\n \nAppealing a Server blacklist is only possible on special occasions.\n \nQuestions? -> hopedevmail@yahoo.com");
                            couldmsg = true;
                        } catch (Exception e) {
                            // lmao literally everyone disables dms nowadays because of scams pog
                        }
                        try {

                            for (ServerTextChannel x : Main.api.getServerById(str[1]).get().getTextChannels()) {
                                if (x.asServerTextChannel().get().canYouWrite()) {
                                    x.sendMessage("Guild has been blacklisted from using this Bot by \"BOT OWNER\": \n\n **Reason:** _" + sb.toString() + "_\n \n This Bot cannot be reinvited until unblacklisted!\n \nAppealing a Server blacklist is only possible on special occasions.\n \nQuestions? -> hopedevmail@yahoo.com");
                                    farmcheck = true;
                                    break;
                                }
                            }
                            couldmessage = true;
                        } catch (Exception e) {
                            // bruh no system channel lmao
                        }

                        Main.api.getServerById(str[1]).get().leave();

                    }
                    StringBuilder sb = new StringBuilder();
                    if (!couldmsg) {
                        sb.append("\nCould not Message Guild Owner");

                    }
                    if (!couldmessage) {
                        sb.append("\nCould not Inform Guild about it");

                    }
                    if (!farmcheck) {
                        sb.append("\nGuild was suspected Botfarm Guild");
                    }
                    event.getChannel().sendMessage("Guild ID " + str[1] + " blacklisted\n" + sb.toString());


                    return;
                }
                return;
            }
            if (str[0].equalsIgnoreCase("servers")) {
                if (!event.getMessageAuthor().isBotOwner()) {
                    event.getChannel().sendMessage(Templates.permerrorembed().setDescription("This command is restricted to the Bot Owner"));
                    return;
                }
                EmbedBuilder eb = Templates.defaultembed().setColor(Main.blurple);
                eb.setTitle("current Servers");


                for (Server server : Main.api.getServers()) {
                    int all = server.getMemberCount();
                    int botcount = 0;
                    int usercount = 0;

                    for (User x : server.getMembers()) {
                        if (x.isBot()) {
                            botcount++;
                        }


                        if (!x.isBot()) {
                            usercount++;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Members [BOTS+USERS]: " + all + "\n");
                    sb.append("Members [BOTS]: " + botcount + "\n");
                    sb.append("Members [USERS]: " + usercount + "\n");
                    eb.addField(server.getName(), "Server ID: " + server.getIdAsString() + "\n Server Owner: " + server.getOwner().getDiscriminatedName() + " (" + server.getOwner().getIdAsString() + ")\n \n" + sb.toString());

                }
                event.getChannel().sendMessage(eb);

                return;
            }
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));


        }
    }
}











