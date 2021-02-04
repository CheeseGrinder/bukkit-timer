package fr.cheeseGrinder.bukkitTimer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Logger;

public class Plugin extends JavaPlugin {

    Logger logger = Bukkit.getLogger();
    private final Timer timer = new Timer(this);

    private BukkitTask task;

    @Override
    public void onEnable() {
        timer.runLaterAsync(10, () -> {
            this.task = timer.runLoopAsync(0, () -> {
                logger.info("< LOOP >");
            });

           timer.runAsync(5f, Time.EVERY_0_05, (time) -> {
               if (time == 0) {
                   logger.info("< END LOOP >");
                   task.cancel();
                   this.task = null;
               }
           });
        });
    }
}
