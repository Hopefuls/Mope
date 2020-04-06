package me.hope.franxxmin.utils;

import me.hope.franxxmin.onStart.CooldownManager;
import me.hope.franxxmin.utils.VariablesStorage.Cooldown;
import me.hope.franxxmin.utils.VariablesStorage.ServerHashmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TimerThreadCooldown extends Thread {
    public void run() {
        while (true) {

            try {
                //1 milliseconds updating
                sleep(100);

                for (String x : ServerHashmaps.ID) {
                    HashMap<CooldownManager.TYPE, Double> temp = new HashMap<>(Cooldown.cooldowntrack.get(x));


                    for (int i = 0; i < temp.size(); i++) {

                        //Getting Set of entries from HashMap
                        Set<Map.Entry<CooldownManager.TYPE, Double>> entrySet = temp.entrySet();

                        //Creating an ArrayList of Entry objects

                        ArrayList<Map.Entry<CooldownManager.TYPE, Double>> listOfEntry = new ArrayList<>(entrySet);

                        for (Map.Entry<CooldownManager.TYPE, Double> entry : listOfEntry) {
                            if (entry.getValue() <= 0.0 || entry.getValue() == 0.0) {
                                if (entry.getValue() <= 0.0) {
                                    Cooldown.cooldowntrack.get(x).put(entry.getKey(), 0.0);
                                }
                                //Debug System.out.println("Not updating: Past limit on "+Main.api.getServerById(x).get().getName());

                            } else {
                                double updated = entry.getValue() - 0.1;
                                Cooldown.cooldowntrack.get(x).put(entry.getKey(), updated);
                                //  System.out.println("Updating value for help-command on "+Main.api.getServerById(x).get().getName()+" to "+updated);

                            }
                        }

                    }


                }
            } catch (InterruptedException e) {
            }


        }
    }
}
