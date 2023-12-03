package com.terryscape.maths;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Vector2IntTest {

    @Test
    public void testAdd() {
        assertThat(new Vector2Int(5, 4).add(new Vector2Int(2, 1))).isEqualTo(new Vector2Int(7, 5));
        assertThat(new Vector2Int(-3, -10).add(new Vector2Int(2, 5))).isEqualTo(new Vector2Int(-1, -5));
    }

    @Test
    public void testDistance() {
        assertThat(new Vector2Int(1, 1).distance(new Vector2Int(2, 1))).isEqualTo(1f);
        assertThat(new Vector2Int(1, 1).distance(new Vector2Int(2, 2))).isEqualTo(1.4142135f);
        assertThat(new Vector2Int(1, 1).distance(new Vector2Int(3, 3))).isEqualTo(2.828427f);
        assertThat(new Vector2Int(1, 1).distance(new Vector2Int(3, 1))).isEqualTo(2f);
    }
}
