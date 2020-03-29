package me.hope.franxxmin.utils.VariablesStorage;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.JSONObject;

import java.util.HashMap;

public class MiscVariables {
    public static HashMap<String, Message> osureactmessage = new HashMap<>();

    public static HashMap<String/*serverid*/, MessageCreateEvent/*osumessageevent*/> osuapieventholder = new HashMap<>();
    public static HashMap<String/*serverid*/, String/*UserID*/> osuuserid = new HashMap<>();
    public static HashMap<String/*serverid*/, Integer/*Embed Page Number (Default 1, Best Play is 2)*/> osuembedpage = new HashMap<>();
    public static HashMap<String/*Userid*/, JSONObject> jsonObjectHashMap = new HashMap<>();
    public static HashMap<String/*Userid*/, JSONObject> jsonObjectHashMapGetBest = new HashMap<>();
    public static HashMap<String/*messageid*/, Message/*Message for Edit OSUREACTOR*/> editosureactor = new HashMap<>();

}
