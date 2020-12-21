package fr.devsCheese.bukkitTimer;

import javafx.util.Callback;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timer {

    private final Map<UUID, Float> timers = new HashMap<>();
    private final Plugin plugin;
    private final DecimalFormat decimal;

    public Timer(Plugin plugin) {
        this.plugin = plugin;
        this.decimal = new DecimalFormat("0.0");
    }

    public Timer(Plugin plugin, String decimalFormat) {
        this.plugin = plugin;
        this.decimal = new DecimalFormat(decimalFormat);
    }

    public void run(float duration, Time time) {
        long ticks = Time.valueOf(time.name()).getTicks();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    this.timeLeft = formatTimeLeft(timeLeft);

                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, EmptyCallback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        callback.call();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    this.timeLeft = formatTimeLeft(timeLeft);

                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call();
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, Callback<Float, Float> callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    this.timeLeft = formatTimeLeft(timeLeft);

                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call(timeLeft);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, UUID uuid) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = formatTimeLeft(getCoolDown(uuid));

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }

            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, UUID uuid, EmptyCallback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.call();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = formatTimeLeft(getCoolDown(uuid));

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call();
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, UUID uuid, Callback<Float, Float> callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = formatTimeLeft(getCoolDown(uuid));

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call(timeLeft);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time) {
        long ticks = Time.valueOf(time.name()).getTicks();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    this.timeLeft = formatTimeLeft(timeLeft);

                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, EmptyCallback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        callback.call();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    this.timeLeft = formatTimeLeft(timeLeft);

                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call();
                    }
                    if (timeLeft <= 0) {
                        callback.call();
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, Callback<Float, Float> callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    this.timeLeft = formatTimeLeft(timeLeft);

                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call(timeLeft);
                    }
                    if (timeLeft <= 0) {
                        callback.call(timeLeft);
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, UUID uuid) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = formatTimeLeft(getCoolDown(uuid));

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }

            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, UUID uuid, EmptyCallback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.call();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                float timeLeft = formatTimeLeft(getCoolDown(uuid));

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.call();
                    setCoolDown(uuid, timeLeft);
                }
                if (timeLeft <= 0) {
                    callback.call();
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, UUID uuid, Callback<Float, Float> callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                float timeLeft = formatTimeLeft(getCoolDown(uuid));

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.call(timeLeft);
                    setCoolDown(uuid, timeLeft);
                }
                if (timeLeft <= 0) {
                    callback.call(0f);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runLater(long delay, EmptyCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                callback.call();
            }
        }.runTaskLater(plugin, delay);
    }

    public void runLaterAsync(long delay, EmptyCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                callback.call();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public String formatTime(float time) {
        return decimal.format(time);
    }

    public String formatTime(float time, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(time);
    }

    public float getCoolDown(UUID uuid) {
        return timers.getOrDefault(uuid, 0f);
    }

    private void setCoolDown(UUID uuid, float time) {
        if (time < 0.1f) {
            timers.remove(uuid);
        } else {
            timers.put(uuid, time);
        }
    }

    private float formatTimeLeft(float timeLeft) {
        return Math.round(timeLeft * 10f) / 10f;
    }

}
