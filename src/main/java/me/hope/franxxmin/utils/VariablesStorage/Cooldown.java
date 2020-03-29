package me.hope.franxxmin.utils.VariablesStorage;

import me.hope.franxxmin.onStart.CooldownManager;

import java.util.HashMap;

public class Cooldown {

    //String> Cooldowntype --- Double> cooldown (10.0)
    public static HashMap<CooldownManager.TYPE, Double> def = new HashMap<>();

    public static HashMap<String/*The Server ID*/, HashMap<CooldownManager.TYPE/*Cooldowncommand*/,Double/*Time Left*/>> cooldowntrack = new HashMap<>();
}
