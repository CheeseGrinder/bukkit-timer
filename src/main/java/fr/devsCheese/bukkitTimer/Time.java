package fr.devsCheese.bukkitTimer;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum Time {

    SECOND_0_1(0.1f, 2),
    SECOND_0_2(0.2f, 4),
    SECOND_0_3(0.3f, 6),
    SECOND_0_4(0.4f, 8),
    SECOND_0_5(0.5f, 10),
    SECOND_0_6(0.6f, 12),
    SECOND_0_7(0.7f, 14),
    SECOND_0_8(0.8f, 16),
    SECOND_0_9(0.9f, 18),
    SECOND_1_0(1.0f, 20);

    @Getter
    private final float decrement;

    @Getter
    private final long ticks;

    Time(float decrement, long ticks) {
        this.decrement = decrement;
        this.ticks = ticks;
    }

}
