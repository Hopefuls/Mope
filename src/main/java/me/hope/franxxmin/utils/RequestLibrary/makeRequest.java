package me.hope.franxxmin.utils.RequestLibrary;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class makeRequest {

    public static String getResponse(String URL, Class callerClass, MessageCreateEvent event) {
        String fresponse = null;
        try {

            HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(URL).openConnection();

            // optional default is GET
            httpClient.setRequestMethod("GET");


            //add request header
            httpClient.setRequestProperty("User-Agent", "Franxxmin Bot | Contact Hope#1445");

            int responseCode = httpClient.getResponseCode();

            System.out.println("Making Request to "+URL);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Called in class "+callerClass.getName());

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                fresponse = response.toString();


            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (event == null) {
            return fresponse;
        }
        EmbedBuilder eb = Templates.infoembed();
        eb.setTitle("\uD83D\uDCDD Logging");
        eb.setDescription("APIAccess Request " + callerClass.getName());
        eb.addField("Server Name", event.getServer().get().getName(), true);
        eb.addField("Server ID", event.getServer().get().getName(), true);
        eb.addField("Server Owner", event.getServer().get().getOwner().getDiscriminatedName() + " (" + event.getServer().get().getOwner().getIdAsString() + ")", true);
        eb.addField("Author", event.getMessageAuthor().getDiscriminatedName() + " (" + event.getMessageAuthor().getIdAsString() + ")");
        eb.setThumbnail(event.getMessageAuthor().getAvatar());
        eb.addField("Channel", event.getChannel().asServerChannel().get().getName() + " (" + event.getChannel().getIdAsString() + ")");
        eb.addField("Response of API Request", fresponse);
        eb.setThumbnail(Main.api.getYourself().getAvatar());

        // Remove Server from cooldown refereer (might not be needed)
        // Main.cooldownref.remove(event.getServer().getIdAsString());

        if (Main.debug.getBoolean("enabled", true)) {
            Main.logging.getUserById("245225589332639747").join().openPrivateChannel().join().sendMessage(eb);

        }
        return fresponse;
    }
}
