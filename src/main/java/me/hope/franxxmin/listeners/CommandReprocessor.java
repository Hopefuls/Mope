package me.hope.franxxmin.listeners;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import me.hope.franxxmin.listeners.Commands.Defaults;
import me.hope.franxxmin.listeners.Commands.Moderation.Moderation;
import me.hope.franxxmin.listeners.Commands.NEKOBOTRES;
import me.hope.franxxmin.listeners.Commands.Status;
import me.hope.franxxmin.onStart.CooldownManager;
import me.hope.franxxmin.utils.RequestLibrary.APIAccess;
import me.hope.franxxmin.utils.RequestLibrary.OSU_PPY_SH;
import me.hope.franxxmin.utils.VariablesStorage.Cooldown;
import me.hope.franxxmin.utils.cooldownutility;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.io.IOException;
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

                        } else if (str[1].equalsIgnoreCase("image")) {
                            new Defaults(event).ImagehelpPage(Pstr);
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
                            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Current Prefix for**" + event.getServer().get().getName() + "** is ``" + Pstr + "``\n \n name a Prefix you want the bot to listen to\n \n**Usage example: fm> prefix** ``franxxmin>`` \nResult: ``franxxmin>`` help\n \n \n_Hint: You can always use fm> anyways._"));

                        } else {
                            if (str[1].equalsIgnoreCase("reset")) {
                                prefix.remove("prefix");
                                event.getChannel().sendMessage(Templates.defaultembed().setColor(Color.green).setDescription("Command Prefix for ``" + event.getServer().get().getName() + "`` has been set to the default ``fm> ``"));

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
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: fm> hug " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " hug " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " hugs " + actioninitializer.getMentionTag() + "! *I'm glad you love yourself!*", new APIAccess(event).hug());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " hugs " + actioninitializer.getMentionTag() + "! *Oh well, i need some love aswell!*", new APIAccess(event).hug());
                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " hugs " + actionreciever.getMentionTag() + "! *How cute~*", new APIAccess(event).hug());

                        }

                    }

                } else if (str[0].equalsIgnoreCase("kiss")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " kiss " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " kiss " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " kisses " + actioninitializer.getMentionTag() + "! *You can't give yourself kisses you dummy!*", new APIAccess(event).kiss());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " kisses " + actioninitializer.getMentionTag() + "! *Someone loves me?!*", new APIAccess(event).kiss());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " kisses " + actionreciever.getMentionTag() + "! *Mwah~*", new APIAccess(event).kiss());

                        }

                    }


                } else if (str[0].equalsIgnoreCase("pat")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " pat " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " pat " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " pats " + actioninitializer.getMentionTag() + "! *Do you need someone who gives you headpats?*", new APIAccess(event).pat());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " pats " + actioninitializer.getMentionTag() + "! *I get headpats too?!*", new APIAccess(event).pat());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " pats " + actionreciever.getMentionTag() + "! *Headpats are always nice!~*", new APIAccess(event).pat());

                        }

                    }


                } else if (str[0].equalsIgnoreCase("poke")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " poke " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " poke " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " pokes " + actioninitializer.getMentionTag() + "! *Giving yourself attention is important!*", new APIAccess(event).poke());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " pokes " + actioninitializer.getMentionTag() + "! *Stop, i'm always paying attention!!*", new APIAccess(event).poke());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " pokes " + actionreciever.getMentionTag() + "! *Hey, wake up!!*", new APIAccess(event).poke());

                        }

                    }


                } else if (str[0].equalsIgnoreCase("tickle")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " tickle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " tickle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " tickles " + actioninitializer.getMentionTag() + "! *Are you ticklish??*", new APIAccess(event).tickle());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " tickles " + actioninitializer.getMentionTag() + "! *Nooo stop i'm really really ticklish HAHAHA*", new APIAccess(event).tickle());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " tickles " + actionreciever.getMentionTag() + "! *Tickle tickle~*", new APIAccess(event).tickle());

                        }

                    }


                } else if (str[0].equalsIgnoreCase("cry")) {
                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is crying. _Don't be sad!! Everyone is here for you :)_", new APIAccess(event).cry());


                } else if (str[0].equalsIgnoreCase("smug")) {

                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " smugs", new APIAccess(event).smug());

                } else if (str[0].equalsIgnoreCase("feed")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " feed " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " feed " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " feeds " + actioninitializer.getMentionTag() + "! *I bet you're hungry.*", new APIAccess(event).feed());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " feeds " + actioninitializer.getMentionTag() + "! *Let me do that!*", new APIAccess(event).feed());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " feeds " + actionreciever.getMentionTag() + "! *Yummy~*", new APIAccess(event).feed());

                        }
                    }
                } else if (str[0].equalsIgnoreCase("cuddle")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " cuddle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " cuddle " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " cuddles " + actioninitializer.getMentionTag() + "! *Cuddles are always great!.*", new APIAccess(event).cuddle());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " cuddles " + actioninitializer.getMentionTag() + "! *Are you having your feelies?*", new APIAccess(event).cuddle());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " cuddles " + actionreciever.getMentionTag() + "! *Warmth!~*", new APIAccess(event).cuddle());

                        }
                    }

                } else if (str[0].equalsIgnoreCase("blush")) {
                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is blushing", new APIAccess(event).blush());

                } else if (str[0].equalsIgnoreCase("laugh")) {

                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is laughing", new APIAccess(event).laugh());


                } else if (str[0].equalsIgnoreCase("lick")) {
                    Object[] list = event.getMessage().getMentionedUsers().toArray();
                    if (list.length > 1) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You only need to mention one user!\n \n**Usage example: " + Pstr + " lick " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else if (list.length == 0) {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("You need to mention a user!\n \n**Usage example: " + Pstr + " lick " + event.getMessageAuthor().asUser().get().getMentionTag() + "**"));
                    } else {
                        User actionreciever = event.getMessage().getMentionedUsers().get(0);
                        User actioninitializer = event.getMessageAuthor().asUser().get();

                        if (actioninitializer.equals(actionreciever)) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " licks " + actioninitializer.getMentionTag() + "! *You're quite tasty!*", new APIAccess(event).lick());
                        } else if (actionreciever.isYourself()) {
                            new Defaults(event).ImageSender(Main.api.getYourself().getMentionTag() + " licks " + actioninitializer.getMentionTag() + "! *You're quite tasty!*", new APIAccess(event).lick());

                        } else {
                            new Defaults(event).ImageSender(actioninitializer.getMentionTag() + " licks " + actionreciever.getMentionTag() + "! *You're quite tasty!*", new APIAccess(event).lick());

                        }
                    }

                } else if (str[0].equalsIgnoreCase("happy")) {
                    new Defaults(event).ImageSender(event.getMessageAuthor().asUser().get().getMentionTag() + " is happy! *I'm glad you are.", new APIAccess(event).happy());

                } else if (str[0].equalsIgnoreCase("baguette")) {
                    new NEKOBOTRES(event).baguette();

                } else if (str[0].equalsIgnoreCase("magik")) {
                    new NEKOBOTRES(event).magik();


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

                } else if (str[0].equalsIgnoreCase("gitupdate")) {
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

                } else if (str[0].equalsIgnoreCase("kick")) {
                    if (event.getServer().get().getIdAsString().equals("655071198291689504")) {
                        new Moderation(event, str, Pstr).kickUser();
                    } else {
                        event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));

                    }


                } else if (str[0].equalsIgnoreCase("revert")) {
                    if (event.getServer().get().getIdAsString().equals("606652140475252756")) {

                        for (ServerTextChannel text : event.getServer().get().getTextChannels()) {
                            if (text.getIdAsString().equals("606767369825812481")) {

                            } else {
                                for (Message msg : text.getMessages(100).join()) {
                                    if (msg.getAuthor().isYourself()) {
                                        msg.delete();
                                    }
                                }
                            }
                        }
                    }
                    event.getChannel().sendMessage("Fixed");
                } else {

                    event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));
                }
            }

        } else {

            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("This command is unknown. Get a list of available commands by using `" + Pstr + " help`"));
        }
            }
        }








