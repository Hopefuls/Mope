package me.hope.franxxmin;

import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class ThreadReconnector extends Thread {
    private int count = 0;

    public void run() {
        while (true) {
            try {
                System.out.println("Starting OPCODE Thread listener Timer");
                Thread.sleep(6000000);
                System.out.println("[OPCODE] Reconnecting API..");
                Main.logging = new DiscordApiBuilder().setToken(Main.loggingtoken).login().join();
                Main.api = new DiscordApiBuilder().setToken(Main.botdcapitoken).login().join();
                System.out.println("[OPCODE] Successfully reconnected!");
                EmbedBuilder eb = Templates.debugembed();
                count++;
                eb.setDescription("[OPCODE] Reconnect Notification");
                eb.addField("INFO", "Reconnected successfully!");
                eb.addField("AMS", count + "");
                Main.logging.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(eb);

                System.out.println("[OPCODE] Repeat!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
