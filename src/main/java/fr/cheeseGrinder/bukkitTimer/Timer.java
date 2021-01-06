package fr.cheeseGrinder.bukkitTimer;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class Timer {

    private final Pattern POINT_TO_UNDERSCORE = Pattern.compile("\\.");
    private final Map<String, DecimalFormat> formatter = new HashMap<>();
    private final Map<UUID, Float> timers = new HashMap<>();
    private final Plugin plugin;
    private final String defaultDecimalFormat;

    public Timer(Plugin plugin) {
        this.plugin = plugin;
        this.defaultDecimalFormat = "0.0";
        formatter.put("0.0", new DecimalFormat("0.0"));
    }

    public Timer(Plugin plugin, String decimalFormat) {
        this.plugin = plugin;
        this.defaultDecimalFormat = decimalFormat;
        formatter.put(decimalFormat, new DecimalFormat(decimalFormat));
    }

    public void run(float duration, Time time) {
        long ticks = Time.valueOf(time.name()).getTicks();

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.run(timeLeft, getTimeValue(timeLeft));
                        cancel();
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
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call();
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.run(timeLeft, getTimeValue(timeLeft), callback);
                        cancel();
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, Callback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call(timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.run(duration, getTimeValue(duration), (overTime) -> {
                            if (overTime == 0) callback.call(overTime);
                        });
                        cancel();
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
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.run(timeLeft, getTimeValue(timeLeft), uuid);
                        cancel();
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
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call();
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.run(timeLeft, getTimeValue(timeLeft), uuid, callback);
                        cancel();
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, ticks, ticks);
    }

    public void run(float duration, Time time, UUID uuid, Callback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call(timeLeft);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.run(timeLeft, getTimeValue(timeLeft), uuid, (overTime) -> {
                            if (overTime == 0) callback.call(overTime);
                        });
                        cancel();
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
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runAsync(timeLeft, getTimeValue(timeLeft));
                        cancel();
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
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call();
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), callback);
                        cancel();
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, Callback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call(timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), (overTime) -> {
                            if (overTime == 0) callback.call(overTime);
                        });
                        cancel();
                    }
                    if (timeLeft <= 0) {
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
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), uuid);
                        cancel();
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
                float timeLeft = getCoolDown(uuid);

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.call();
                    setCoolDown(uuid, timeLeft);
                }
                if (timeLeft < decrement && timeLeft > 0f) {
                    Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), uuid, callback);
                    cancel();
                }
                if (timeLeft <= 0) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    public void runAsync(float duration, Time time, UUID uuid, Callback callback) {
        long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.call(duration);

        new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                float timeLeft = getCoolDown(uuid);

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.call(timeLeft);
                    setCoolDown(uuid, timeLeft);
                }
                if (timeLeft < decrement && timeLeft > 0f) {
                    Timer.this.runAsync(duration, getTimeValue(duration), uuid, (overTime) -> {
                        if (overTime == 0) callback.call(overTime);
                    });
                    cancel();
                }
                if (timeLeft <= 0) {
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
        return formatter.get(defaultDecimalFormat).format(time);
    }

    public String formatTime(float time, String format) {
        if (formatter.get(format) == null) {
            formatter.put(format, new DecimalFormat(format));
        }

        return formatter.get(format).format(time);
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

    private Time getTimeValue(float duration) {
        String rest = POINT_TO_UNDERSCORE
                .matcher(String.valueOf(duration))
                .replaceAll("_");

        return Time.valueOf("SECOND_" + rest);
    }

}