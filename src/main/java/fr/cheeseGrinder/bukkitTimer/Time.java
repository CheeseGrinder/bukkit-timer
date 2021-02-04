package fr.cheeseGrinder.bukkitTimer;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @apiNote {@code EVERY_0_50} mean every 0.5 seconds (10 ticks) timer was run
 */
@ToString
public enum Time {

    EVERY_0_05(0.05f, 1),
    EVERY_0_10(0.10f, 2),
    EVERY_0_15(0.15f, 3),
    EVERY_0_20(0.20f, 4),
    EVERY_0_25(0.25f, 5),
    EVERY_0_30(0.30f, 6),
    EVERY_0_35(0.35f, 7),
    EVERY_0_40(0.40f, 8),
    EVERY_0_45(0.45f, 9),
    EVERY_0_50(0.50f, 10),
    EVERY_0_55(0.55f, 11),
    EVERY_0_60(0.60f, 12),
    EVERY_0_65(0.65f, 13),
    EVERY_0_70(0.70f, 14),
    EVERY_0_75(0.75f, 15),
    EVERY_0_80(0.80f, 16),
    EVERY_0_85(0.85f, 17),
    EVERY_0_90(0.90f, 18),
    EVERY_0_95(0.95f, 19),
    EVERY_1_00(1.00f, 20);

    @Getter
    private final float decrement;

    @Getter
    private final long ticks;

    Time(float decrement, long ticks) {
        this.decrement = decrement;
        this.ticks = ticks;
    }

}
