package me.hope.franxxmin.MusicPack;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.Templates;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.lavaplayerwrapper.youtube.YouTubeAudioSource;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Music {


    public static HashMap<Server/*ServerID*/, AudioSource/*Well obv audiosource*/> audioSourceHashMap = new HashMap<>();
    public static HashMap<Server/*ServerID*/, AudioConnection/*Well obv audioconnection*/> audioConnectionHashMap = new HashMap<>();

    public static void MusicHandler(MessageCreateEvent event, String[] str, String[] strraw) {
        if (str[1].equalsIgnoreCase("play")) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Commands:\n \n" + strraw[0] + " music play <URL or Keyword>\n" + strraw[0] + " music pause/unpause\n\n\n**PLEASE DM ME ON ANY ERRORS! --> Hope#1445**").setTitle("FEATURE IN BETA!!!"));

            ServerTextChannel channel = event.getServerTextChannel().orElseThrow(AssertionError::new);
            User author = event.getMessageAuthor().asUser().orElseThrow(AssertionError::new);

            ServerVoiceChannel voiceChannel = author.getConnectedVoiceChannel(channel.getServer()).orElse(null);
            if (voiceChannel == null) {
                event.getChannel().sendMessage("You have to join a voicechannel");
                return;
            }
            AudioConnection audio = voiceChannel.connect().join();
            event.getChannel().sendMessage("Joined channel!");

            YouTubeAudioSource yts = null;
            String shorturl = null;
            if (str[2].toLowerCase().startsWith("https://")) {
                yts = YouTubeAudioSource.of(Main.api, str[2]).join();
                shorturl = str[2].replace("https://www.youtube.com/watch?v=", "");

            } else {

                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < str.length; i++) {
                    sb.append(str[i]);
                }
                String result = YouTubeGatherer.getYouTubeVideo(sb.toString());
                if (result.equalsIgnoreCase("0Results")) {
                    event.getChannel().sendMessage("No Videos found with the Title ``" + sb.toString() + "``");
                    return;
                } else {
                    event.getChannel().sendMessage("Successfully resolved Video: https://youtube.com/watch?v=" + result);
                    yts = YouTubeAudioSource.of(Main.api, "https://youtube.com/watch?v=" + result).join();
                    shorturl = result;

                }
            }
            audioConnectionHashMap.put(event.getServer().get(), audio);

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Playing now:");
            eb.addField("Name", yts.getTitle());
            eb.addField("Uploaded by", yts.getCreatorName());
            eb.addField("URL", yts.getUrl());
            eb.addField("Length", "" + yts.getDuration(TimeUnit.MINUTES));
            eb.setImage("https://img.youtube.com/vi/" + shorturl + "/mqdefault.jpg");
            eb.setThumbnail(Main.api.getYourself().getAvatar());
            eb.setColor(Color.green);
            audio.queue(yts);
            event.getChannel().sendMessage(eb);
            audioSourceHashMap.put(event.getServer().get(), audio.getCurrentAudioSource().get());

        } else if (str[1].equalsIgnoreCase("pause") || str[1].equalsIgnoreCase("unpause")) {
            event.getChannel().sendMessage(Templates.argerrorembed().setDescription("Commands:\n \n" + strraw[0] + " music play <URL or Keyword>\n" + strraw[0] + " music pause/unpause\n\n\n**PLEASE DM ME ON ANY ERRORS! --> Hope#1445**").setTitle("FEATURE IN BETA!!!"));

            AudioSource audioSource = audioSourceHashMap.getOrDefault(event.getServer().get(), null);
            if (audioSource == null) {
                event.getChannel().sendMessage("You need to play music before pausing/unpausing it!");
                return;
            }
            if (audioSource.asPauseableAudioSource().get().isPaused()) {
                audioSource.asPauseableAudioSource().get().unpause();
                event.getChannel().sendMessage(":pause_button: " + event.getMessageAuthor().asUser().get().getMentionTag() + " continuing playback");
            } else {
                audioSource.asPauseableAudioSource().get().pause();
                event.getChannel().sendMessage(":arrow_forward: " + event.getMessageAuthor().asUser().get().getMentionTag() + " paused playback!");
            }
        } else if (str[1].equalsIgnoreCase("stop") || str[1].equalsIgnoreCase("leave")) {
            audioConnectionHashMap.get(event.getServer().get()).close();
            event.getChannel().sendMessage("Successfully stopped playback!");
            audioConnectionHashMap.remove(event.getServer().get());
            audioSourceHashMap.remove(event.getServer().get());
        }
    }
}
