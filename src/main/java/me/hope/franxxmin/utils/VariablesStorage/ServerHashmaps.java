package me.hope.franxxmin.utils.VariablesStorage;

import me.hope.franxxmin.Main;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class ServerHashmaps {

    public static ArrayList<String> ID = new ArrayList<>();
    public static Preferences blacklist = Preferences.userNodeForPackage(Main.class).node("blocklist");


    public ServerHashmaps() {

    }
}
