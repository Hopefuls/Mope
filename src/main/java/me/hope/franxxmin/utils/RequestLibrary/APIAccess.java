package me.hope.franxxmin.utils.RequestLibrary;

import me.hope.franxxmin.Main;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.JSONObject;

public class APIAccess {


    private static String key = Main.apistring;
    private static String requrl = "https://franxx.ml";
    private static MessageCreateEvent event;
    public APIAccess(MessageCreateEvent event) {
    APIAccess.event = event;
    }
    public static String resolveImageURL(String PLAINJSON) {

        JSONObject jsonobject = new JSONObject(PLAINJSON);

        return jsonobject.getString("url");
       // return jsonobject.getJSONArray("results").getJSONObject(0).getJSONArray("media").getJSONObject(0).getJSONObject("gif").getString("url");
    }


        //Done
    public static String hug() {
        return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=hug&key="+key, APIAccess.class, event));
    }
    //Done
    public static String cry() {
        return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=cry&key="+key, APIAccess.class, event));
    }
    //Done
        public static String pat() {

            return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=pat&key="+key, APIAccess.class, event));
    }
    //Done
        public static String kiss() {

            return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=kiss&key="+key, APIAccess.class, event));

    }
    //Done
    public static String poke() {

        return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=poke&key="+key, APIAccess.class, event));

    }
    //Done
    public static String tickle() {
        return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=tickle&key="+key, APIAccess.class, event));

    }
    public static String cuddle() {
        return resolveImageURL(makeRequest.getResponse(requrl+"/api.php?type=cuddle&key="+key, APIAccess.class, event));

    }
    //Done
    public static String smug() {
        return resolveImageURL(makeRequest.getResponse(requrl + "/api.php?type=smug&key=" + key, APIAccess.class, event));

    }

    public static String feed() {
        return resolveImageURL(makeRequest.getResponse(requrl + "/api.php?type=feed&key=" + key, APIAccess.class, event));


    }

    public static String blush() {
        return resolveImageURL(makeRequest.getResponse(requrl + "/api.php?type=blush&key=" + key, APIAccess.class, event));


    }

    public static String laugh() {
        return resolveImageURL(makeRequest.getResponse(requrl + "/api.php?type=laugh&key=" + key, APIAccess.class, event));


    }

    public static String lick() {
        return resolveImageURL(makeRequest.getResponse(requrl + "/api.php?type=lick&key=" + key, APIAccess.class, event));


    }

    public static String happy() {
        return resolveImageURL(makeRequest.getResponse(requrl + "/api.php?type=happy&key=" + key, APIAccess.class, event));


    }

}
