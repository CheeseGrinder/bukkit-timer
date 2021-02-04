package fr.cheeseGrinder.bukkitTimer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class Timer {

    private final Pattern POINT_TO_UNDERSCORE = Pattern.compile("\\.");
    private final Map<String, DecimalFormat> formatter = new HashMap<>();
    private final Map<UUID, Float> timers = new HashMap<>();
    private final JavaPlugin plugin;
    private final String defaultDecimalFormat;

    /**
     * Create a timer with default decimalFormat {@code "0.00"}
     * @param plugin our plugin
     * @see #Timer(T, String)
     */
    public <T extends JavaPlugin> Timer(T plugin) {
        this(plugin, "0.00");
    }

    /**
     * Create a timer with custom decimal format
     * @param plugin our plugin
     * @param decimalFormat custom decimal format
     *
     */
    public <T extends JavaPlugin> Timer(T plugin, String decimalFormat) {
        this.plugin = plugin;
        this.defaultDecimalFormat = decimalFormat;
        formatter.put(decimalFormat, new DecimalFormat(decimalFormat));
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @return a task that can be stopped
     */
    public BukkitTask runSync(float duration, Time time) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runSync(timeLeft, getTimeValue(timeLeft));
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param callback action called after each decrement
     * @return a task that can be stopped
     */
    public BukkitTask runSync(float duration, Time time, Runnable callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        callback.run();

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.run();
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runSync(timeLeft, getTimeValue(timeLeft), callback);
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param callback action called after each decrement with the remaining {@code time} as a parameter.
     * @return a task that can be stopped
     */
    public BukkitTask runSync(float duration, Time time, Consumer<Float> callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        callback.accept(duration);

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.accept(timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runSync(timeLeft, getTimeValue(timeLeft), (overTime) -> {
                            if (overTime == 0) callback.accept(overTime);
                        });
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param uuid of an entity to which you want to assign the timer
     * @return a task that can be stopped
     */
    public BukkitTask runSync(float duration, Time time, UUID uuid) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);

        final BukkitRunnable runnable = new BukkitRunnable() {
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
                        Timer.this.runSync(timeLeft, getTimeValue(timeLeft), uuid);
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param uuid of an entity to which you want to assign the timer
     * @param callback action called after each decrement
     * @return a task that can be stopped
     */
    public BukkitTask runSync(float duration, Time time, UUID uuid, Runnable callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.run();

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.run();
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runSync(timeLeft, getTimeValue(timeLeft), uuid, callback);
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param uuid of an entity to which you want to assign the timer
     * @param callback action called after each decrement with the remaining {@code time} as a parameter.
     * @return a task that can be stopped
     */
    public BukkitTask runSync(float duration, Time time, UUID uuid, Consumer<Float> callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.accept(duration);

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                if (!isCancelled()) {
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.accept(timeLeft);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runSync(timeLeft, getTimeValue(timeLeft), uuid, (overTime) -> {
                            if (overTime == 0) callback.accept(overTime);
                        });
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @return a task that can be stopped
     */
    public BukkitTask runAsync(float duration, Time time) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        final BukkitRunnable runnable = new BukkitRunnable() {
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
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param callback action called after each decrement
     * @return a task that can be stopped
     */
    public BukkitTask runAsync(float duration, Time time, Runnable callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        callback.run();

        final BukkitRunnable runnable =new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.run();
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), callback);
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param callback action called after each decrement with the remaining {@code time} as a parameter.
     * @return a task that can be stopped
     */
    public BukkitTask runAsync(float duration, Time time, Consumer<Float> callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        callback.accept(duration);

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();
            private float timeLeft = duration;

            @Override
            public void run() {
                if (!isCancelled()) {
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.accept(timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), (overTime) -> {
                            if (overTime == 0) callback.accept(overTime);
                        });
                        cancel();
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }
            }
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param uuid of an entity to which you want to assign the timer
     * @return a task that can be stopped
     */
    public BukkitTask runAsync(float duration, Time time, UUID uuid) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);

        final BukkitRunnable runnable = new BukkitRunnable() {
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
                        return;
                    }
                    if (timeLeft <= 0) {
                        cancel();
                    }
                }

            }
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param uuid of an entity to which you want to assign the timer
     * @param callback action called after each decrement
     * @return a task that can be stopped
     */
    public BukkitTask runAsync(float duration, Time time, UUID uuid, Runnable callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.run();

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                float timeLeft = getCoolDown(uuid);

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.run();
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
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    /**
     * @param duration total timer time
     * @param time before each decrement
     * @param uuid of an entity to which you want to assign the timer
     * @param callback action called after each decrement with the remaining {@code time} as a parameter.
     * @return a task that can be stopped
     */
    public BukkitTask runAsync(float duration, Time time, UUID uuid, Consumer<Float> callback) {
        final long ticks = Time.valueOf(time.name()).getTicks();

        setCoolDown(uuid, duration);
        callback.accept(duration);

        final BukkitRunnable runnable = new BukkitRunnable() {
            private final float decrement = Time.valueOf(time.name()).getDecrement();

            @Override
            public void run() {
                float timeLeft = getCoolDown(uuid);

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.accept(timeLeft);
                    setCoolDown(uuid, timeLeft);
                }
                if (timeLeft < decrement && timeLeft > 0f) {
                    Timer.this.runAsync(timeLeft, getTimeValue(timeLeft), uuid, (overTime) -> {
                        if (overTime == 0) callback.accept(overTime);
                    });
                    cancel();
                    return;
                }
                if (timeLeft <= 0) {
                    cancel();
                }
            }
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
    }

    /**
     * @param ticks time before callback call
     * @param callback action
     * @return a task that can be stopped
     */
    public BukkitTask runLaterSync(long ticks, Runnable callback) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                callback.run();
            }
        };
        return runnable.runTaskLater(plugin, ticks);
    }

    /**
     * @param ticks time before callback call
     * @param callback action
     * @return a task that can be stopped
     */
    public BukkitTask runLaterAsync(long ticks, Runnable callback) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                callback.run();
            }
        };
        return runnable.runTaskLaterAsynchronously(plugin, ticks);
    }

    /**
     * <b>This method will create an infinite loop, be careful when using it.</b>
     * @param ticks time before each action
     * @param callback action
     * @return a task that can be stopped
     */
    public BukkitTask runLoopSync(long ticks, Runnable callback) {
        final BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                if (!isCancelled()) callback.run();
            }
        };
        return runnable.runTaskTimer(plugin, ticks, ticks);
    }

    /**
     * <b>This method will create an infinite loop, be careful when using it.</b>
     * @param ticks time before each action
     * @param callback action
     * @return a task that can be stopped
     */
    public BukkitTask runLoopAsync(long ticks, Runnable callback) {
        final BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                if (!isCancelled()) callback.run();
            }
        };
        return runnable.runTaskTimerAsynchronously(plugin, ticks, ticks);
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
        return Math.round(timeLeft * 100f) / 100f;
    }

    private Time getTimeValue(float duration) {
        final String rest = POINT_TO_UNDERSCORE
                .matcher(String.valueOf(duration))
                .replaceAll("_");
        
        return Time.valueOf("EVERY_" + rest);
    }

}
