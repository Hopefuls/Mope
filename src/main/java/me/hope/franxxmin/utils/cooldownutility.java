package me.hope.franxxmin.utils;

import me.hope.franxxmin.onStart.CooldownManager;
import me.hope.franxxmin.utils.VariablesStorage.Cooldown;

import java.util.HashMap;

public class cooldownutility {
    private static String ID = null;

    public cooldownutility(String ID) {
        cooldownutility.ID = ID;
    }

    public static Double chkcooldown(CooldownManager.TYPE cooldowntype) {
        double cooldownsecs = 0;

        //Get fixed cooldown first
        HashMap<CooldownManager.TYPE, Double> fixedcooldown = new HashMap<>(Cooldown.def);

        // Then, get the cooldown references from ID
        HashMap<CooldownManager.TYPE, Double> temp = Cooldown.cooldowntrack.get(ID);

        cooldownsecs = temp.get(cooldowntype);


        return cooldownsecs;
    }

    public static void cooldownreset(CooldownManager.TYPE cooldowntype) {
        //Get fixed cooldown first
        HashMap<CooldownManager.TYPE, Double> fixedcooldown = new HashMap<>(Cooldown.def);

        // Then, get the cooldown references from ID
        HashMap<CooldownManager.TYPE, Double> temp = Cooldown.cooldowntrack.get(ID);
        temp.put(cooldowntype, fixedcooldown.get(cooldowntype));
        System.out.println("Cooldownrs for " + ID);
    }
}
