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
import org.javacord.api.entity.message.embed.EmbedBuilder;
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
                log.addField("Server (ID)", event.getServer().get().getName() + " (" + event.getServer().get().getIdAsString() + ")");
                log.addInlineField("Channel (ID)", event.getChannel().asServerTextChannel().get().getName() + " (" + event.getChannel().getIdAsString() + ")");
                log.addInlineField("Message Author (ID)", event.getMessageAuthor().getDiscriminatedName() + " (" + event.getMessageAuthor().getIdAsString() + " )");
                log.addField("Message ID", event.getMessage().getIdAsString());
                log.addInlineField("Message URL", "https://discordapp.com/channels/" + event.getServer().get().getIdAsString() + "/" + event.getChannel().getIdAsString() + "/" + event.getMessage().getIdAsString());
                log.addField("Commandtext", event.getMessageContent());
            if (Main.debug.getBoolean("enabled", true)) {
                Main.logging.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(log);

            }

            if (str.length == 0) {

                new Defaults(event).allHelpPages(Pstr);
                event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));

            } else {

                if (str[0].equalsIgnoreCase("help")) {

                    if (str.length == 1) {
                        // Send help pages obv
                        new Defaults(event).allHelpPages(Pstr);
                        event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));


                    } else {
                        // Seperate Help Pages (made it very complicated for unknown reasons) :)
                        if (str[1].equalsIgnoreCase("interactive")) {
                            new Defaults(event).interactivehelpPage(Pstr);
                            event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));

                        } else if (str[1].equalsIgnoreCase("general")) {
                            new Defaults(event).generalhelpPage(Pstr);
                            event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));

                        } else if (str[1].equalsIgnoreCase("moderation")) {
                            new Defaults(event).moderationhelpPage(Pstr);
                            event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));


                        } else if (str[1].equalsIgnoreCase("osu")) {
                            new Defaults(event).osuhelpPage(Pstr);
                        } else {
                            new Defaults(event).allHelpPages(Pstr);
                            event.getChannel().sendMessage(Templates.defaultembed().setDescription("Prefix for **" + event.getServer().get().getName() + "** is ``" + Pstr + "``"));

                        }
                    }


                } else if (str[0].equalsIgnoreCase("users")) {
                    if (new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.USERS) != 0.0) {

                        event.getChannel().sendMessage(Templates.cooldownerrorembed("users", Cooldown.def.get(CooldownManager.TYPE.USERS), new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.USERS)));

                    } else {
                        //Usercount
                        new cooldownutility(serverid).cooldownreset(CooldownManager.TYPE.USERS);
                        new Defaults(event).usercount();
                    }
                } else if (str[0].equalsIgnoreCase("userinfo")) {
                    if (new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.STATUS) != 0.0) {

                        event.getChannel().sendMessage(Templates.cooldownerrorembed("users", Cooldown.def.get(CooldownManager.TYPE.USERS), new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.USERS)));

                    } else {

                        new cooldownutility(serverid).cooldownreset(CooldownManager.TYPE.USERS);
                        new Defaults(event).userinfo();
                    }
                    // Userinfo

                } else if (str[0].equalsIgnoreCase("about")) {
                    // About page of the Bot
                    event.getChannel().sendMessage(Templates.aboutembed());
                } else if (str[0].equalsIgnoreCase("prefix")) {
                    if (event.getMessageAuthor().isServerAdmin()) {


                        if (str.length == 1) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Current Prefix for**" + event.getServer().get().getName() + "** is ``" + Pstr + "``\n \n name a Prefix you want the bot to listen to\n \n**Usage example: mp> prefix** ``Mope>`` \nResult: ``Mope>`` help\n \n \n_Hint: You can always use mp> anyways._"));

                        } else {
                            if (str[1].equalsIgnoreCase("reset")) {
                                prefix.remove("prefix");
                                event.getChannel().sendMessage(Templates.defaultembed().setColor(Color.green).setDescription("Command Prefix for ``" + event.getServer().get().getName() + "`` has been set to the default ``mp> ``"));

                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i < str.length; i++) {
                                    sb.append(str[i]);
                                }
                                prefix.put("prefix", sb.toString());
                                event.getChannel().sendMessage(Templates.defaultembed().setColor(Color.green).setDescription("Command Prefix for ``" + event.getServer().get().getName() + "`` successfully set to ``" + sb.toString() + "``"));
                            }
                        }
                    } else {
                        event.getChannel().sendMessage(Templates.permerrorembed().setDescription("This command can only be used by server admins."));
                    }
                } else if (str[0].equalsIgnoreCase("status")) {
                    if (new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.STATUS) != 0.0) {

                        event.getChannel().sendMessage(Templates.cooldownerrorembed("status", Cooldown.def.get(CooldownManager.TYPE.STATUS), new cooldownutility(serverid).chkcooldown(CooldownManager.TYPE.STATUS)));

                    } else {
                        new cooldownutility(serverid).cooldownreset(CooldownManager.TYPE.STATUS);

                        // Get the API Status like APIRT or Resources count(gifs and such on api)
                        //todo extra Klasse dafuer --> Erledigt!!
                        event.getChannel().sendMessage(Status.request(event.getServer().get().getIdAsString(), event));
                    }
                } else if (str[0].equalsIgnoreCase("hug")) {

                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " hug " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot hug yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }

                    }

                } else if (str[0].equalsIgnoreCase("kiss")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to matleast mention one user!\n \n**Usage example: " + Pstr + " kiss " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot kiss yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }

                    }


                } else if (str[0].equalsIgnoreCase("pat")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " pat " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot pat yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }

                    }


                } else if (str[0].equalsIgnoreCase("poke")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " poke " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot poke yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }

                    }


                } else if (str[0].equalsIgnoreCase("tickle")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " tickle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot tickle yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }

                    }


                } else if (str[0].equalsIgnoreCase("cry")) {
                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is crying. _Don't be sad!! Everyone is here for you :)_", new APIAccess(event).cry());


                } else if (str[0].equalsIgnoreCase("smug")) {

                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " smugs", new APIAccess(event).smug());

                } else if (str[0].equalsIgnoreCase("feed")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " feed " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot feed yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }
                    }
                } else if (str[0].equalsIgnoreCase("cuddle")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " cuddle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot cuddle yourself :("));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }
                    }

                } else if (str[0].equalsIgnoreCase("blush")) {
                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is blushing", new APIAccess(event).blush());

                } else if (str[0].equalsIgnoreCase("laugh")) {

                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is laughing", new APIAccess(event).laugh());


                } else if (str[0].equalsIgnoreCase("lick")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to atleast mention one user!\n \n**Usage example: " + Pstr + " lick " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        List<User> actionrecievers = event.getMessage().getMentionedUsers();
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actionrecievers.contains(actioninitializer)) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot lick yourself!"));
                        } else if (actionrecievers.contains(Main.api.getYourself())) {
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You cannot include me in the Command!"));

                        } else {
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

                        }
                    }

                } else if (str[0].equalsIgnoreCase("happy")) {
                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is happy! *I'm glad you are.", new APIAccess(event).happy());

                } else if (str[0].equalsIgnoreCase("baguette")) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));

                    return;
                    //  new NEKOBOTRES(event).baguette();

                } else if (str[0].equalsIgnoreCase("magik")) {
                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));
                    return;
                    // new NEKOBOTRES(event).magik();


                } else if (str[0].equalsIgnoreCase("osu")) {
                    if (str.length == 1) {
                        new Defaults(event).osuhelpPage(Pstr);

                    } else {

                        if (str[1].equalsIgnoreCase("user")) {
                            if (str.length == 2) {
                                event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to either enter a username or a userid!\n \n**Example: **_" + Pstr + " osu user HopeDev_"));
                            } else {
                                if (new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU) != 0.0) {

                                    event.getChannel().sendMessage(Templates.cooldownerrorembed("osu", Cooldown.def.get(CooldownManager.TYPE.OSU), new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU)));


                                } else {
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
                                }
                            }
                        } else if (str[1].equalsIgnoreCase("recent")) {
                            if (str.length == 2) {
                                event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to either enter a username or a userid!\n \n**Example: **_" + Pstr + " osu recent HopeDev_"));
                            } else {
                                if (new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU) != 0.0) {

                                    event.getChannel().sendMessage(Templates.cooldownerrorembed("osu", Cooldown.def.get(CooldownManager.TYPE.OSU), new cooldownutility(event.getServer().get().getIdAsString()).chkcooldown(CooldownManager.TYPE.OSU)));


                                } else {
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
                                }
                            }

                        } else {
                            new Defaults(event).osuhelpPage(Pstr);
                        }
                    }
                } else if (str[0].equalsIgnoreCase("debug")) {
                    if (event.getMessageAuthor().isBotOwner()) {
                        if (Main.debug.getBoolean("enabled", true)) {
                            Main.debug.putBoolean("enabled", false);
                            event.getChannel().sendMessage("Debug disabled.");
                            System.out.println("Debug being toggled");
                            EmbedBuilder eb = new Templates().infoembed().setDescription("Debugging toggled.");
                            Main.logging.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(eb);


                        } else {
                            event.getChannel().sendMessage("Debug enabled.");
                            Main.debug.putBoolean("enabled", true);
                            EmbedBuilder eb = new Templates().infoembed().setDescription("Debugging toggled.");
                            Main.logging.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(eb);
                            System.out.println("Debug being toggled");
                        }

                    } else {


                    }

                } else if (str[0].equalsIgnoreCase("pushupdate")) {
                    if (event.getMessageAuthor().isBotOwner()) {
                        if (Main.localmode) {

                        } else {
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
                        }


                    }

                } else if (str[0].equalsIgnoreCase("clearchat")) {
                    new Moderation(event, str, Pstr).clearChat();
                } else if (str[0].equalsIgnoreCase("kick")) {
                    new Moderation(event, str, Pstr).kickUser();
                } else if (str[0].equalsIgnoreCase("ban")) {
                    new Moderation(event, str, Pstr).banUser();
                } else if (str[0].equalsIgnoreCase("music")) {

                    if (str.length == 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Commands:\n \n" + Pstr + " music play <URL or Keyword>\n" + Pstr + " music pause/unpause\n\n\n**PLEASE DM ME ON ANY ERRORS! --> Hope#1445**").setTitle("FEATURE IN BETA!!!"));
                    }
                    Music.MusicHandler(event, str, strraw);
                } else if (str[0].equalsIgnoreCase("blacklist")) {
                    if (!event.getMessageAuthor().isBotOwner()) {
                        return;
                    }
                    Preferences pref = ServerHashmaps.blacklist;
                    if (pref.getBoolean(str[1], false)) {
                        pref.putBoolean(str[1], false);
                        event.getChannel().sendMessage("Server ID " + str[1] + " unblacklisted");
                    } else if (!pref.getBoolean(str[1], false)) {
                        pref.putBoolean(str[1], true);
                        event.getChannel().sendMessage("Server ID " + str[1] + " blacklisted");
                        if (Main.api.getServerById(str[1]).get().getMembers().contains(Main.api.getYourself())) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < str.length; i++) {
                                sb.append(str[i] + " ");
                            }

                            Main.api.getServerById(str[1]).get().getOwner().openPrivateChannel().join().sendMessage("Hello " + Main.api.getServerById(str[1]).get().getOwner().getName() + ", \n \nYour Server has been blacklisted from using this Bot by \"BOT OWNER\": \n \n **Reason:** _" + sb.toString() + "_\n \n This Bot cannot be reinvited until unblacklisted!\n \nAppealing a Server blacklist is only possible on special occasions.\n \nQuestions? -> hopedevmail@yahoo.com");
                            Main.api.getServerById(str[1]).get().getSystemChannel().get().sendMessage("Server has been blacklisted from using this Bot by \"BOT OWNER\": \n\n **Reason:** _" + sb.toString() + "_\n \n This Bot cannot be reinvited until unblacklisted!\n \nAppealing a Server blacklist is only possible on special occasions.\n \nQuestions? -> hopedevmail@yahoo.com");
                            Main.api.getServerById(str[1]).get().leave();

                        }

                    } else {

                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));
                    }

                }
            }
        }
    }
}









