package me.hope.franxxmin.MusicPack;

import me.hope.franxxmin.Main;
import me.hope.franxxmin.utils.RequestLibrary.makeRequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class YouTubeGatherer {

    public static String getYouTubeVideo(String keyword) {
        String requestres = makeRequest.getResponse("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + keyword.replace(" ", "%20") + "&type=video&key=" + Main.googleconsoletoken, YouTubeGatherer.class, null);
        JSONObject result = new JSONObject(requestres);
        JSONObject pageinfo = result.getJSONObject("pageInfo");
        if (pageinfo.getInt("totalResults") == 0) {
            return "0Results";
        }
        System.out.println(result);

        JSONArray array = result.getJSONArray("items");
        JSONObject obj = array.getJSONObject(0);
        JSONObject obj2 = obj.getJSONObject("id");
        return obj2.getString("videoId");


    }
}
