package fr.devsCheese.bukkitTimer;

import javafx.util.Callback;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Timer {

    private final Map<UUID, Float> timers = new HashMap<>();
    private final Plugin plugin;
    private final DecimalFormat decimal;
    private final Pattern POINT_TO_UNDERSCORE = Pattern.compile("\\.");

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
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runRest(timeLeft);
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
                        Timer.this.runRest(timeLeft, callback);
                        cancel();
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
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call(timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runRest(timeLeft, callback);
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
                        Timer.this.runRest(timeLeft, uuid);
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
                        Timer.this.runRest(timeLeft, uuid, callback);
                        cancel();
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
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);

                        callback.call(timeLeft);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runRest(timeLeft, uuid, callback);
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
                        Timer.this.runRestAsync(timeLeft);
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
                        Timer.this.runRestAsync(timeLeft, callback);
                        cancel();
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
                    if (timeLeft >= decrement) {
                        this.timeLeft = formatTimeLeft(timeLeft - decrement);
                        callback.call(timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runRestAsync(timeLeft, callback);
                        cancel();
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
                    float timeLeft = getCoolDown(uuid);

                    if (timeLeft >= decrement) {
                        timeLeft = formatTimeLeft(timeLeft - decrement);
                        setCoolDown(uuid, timeLeft);
                    }
                    if (timeLeft < decrement && timeLeft > 0f) {
                        Timer.this.runRestAsync(timeLeft, uuid);
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
                    Timer.this.runRestAsync(timeLeft, uuid, callback);
                    cancel();
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
                float timeLeft = getCoolDown(uuid);

                if (timeLeft >= decrement) {
                    timeLeft = formatTimeLeft(timeLeft - decrement);

                    callback.call(timeLeft);
                    setCoolDown(uuid, timeLeft);
                }
                if (timeLeft < decrement && timeLeft > 0f) {
                    Timer.this.runRestAsync(timeLeft, uuid, callback);
                    cancel();
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

    private void runRest(float duration) {
        run(duration, getTimeValue(duration));
    }

    private void runRest(float duration, EmptyCallback callback) {
        run(duration, getTimeValue(duration), callback);
    }

    private void runRest(float duration, Callback<Float, Float> callback) {
        run(duration, getTimeValue(duration), (Float overTime) -> {
            if (overTime == 0) {
                callback.call(overTime);
            }
            return null;
        });
    }

    private void runRest(float duration, UUID uuid) {
        run(duration, getTimeValue(duration), uuid);
    }

    private void runRest(float duration, UUID uuid, EmptyCallback callback) {
        run(duration, getTimeValue(duration), uuid, callback);
    }

    private void runRest(float duration, UUID uuid, Callback<Float, Float> callback) {
        run(duration, getTimeValue(duration), uuid, (Float overTime) -> {
            if (overTime == 0) {
                callback.call(overTime);
            }
            return null;
        });
    }

    private void runRestAsync(float duration) {
        runAsync(duration, getTimeValue(duration));
    }

    private void runRestAsync(float duration, EmptyCallback callback) {
        Timer.this.runAsync(duration, getTimeValue(duration), callback);
    }

    private void runRestAsync(float duration, Callback<Float, Float> callback) {
        Timer.this.runAsync(duration, getTimeValue(duration), (Float overTime) -> {
            if (overTime == 0) {
                callback.call(overTime);
            }
            return null;
        });
    }

    private void runRestAsync(float duration, UUID uuid) {
        Timer.this.runAsync(duration, getTimeValue(duration), uuid);
    }

    private void runRestAsync(float duration, UUID uuid, EmptyCallback callback) {
        Timer.this.runAsync(duration, getTimeValue(duration), uuid, callback);
    }

    private void runRestAsync(float duration, UUID uuid, Callback<Float, Float> callback) {
        Timer.this.runAsync(duration, getTimeValue(duration), uuid, (Float overTime) -> {
            if (overTime == 0) {
                callback.call(overTime);
            }
            return null;
        });
    }

    private Time getTimeValue(float duration) {
        String rest = POINT_TO_UNDERSCORE
                .matcher(String.valueOf(duration))
                .replaceAll("_");

        return Time.valueOf("SECOND_" + rest);
    }

}
