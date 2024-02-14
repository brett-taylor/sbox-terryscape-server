package com.terryscape.world;

import com.terryscape.world.coordinate.WorldCoordinate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorldCoordinateTest {

    @Test
    public void testAdd() {
        assertThat(new WorldCoordinate(5, 4).add(new WorldCoordinate(2, 1))).isEqualTo(new WorldCoordinate(7, 5));
    }

    @Test
    public void testDistanceSquareRoot() {
        assertThat(new WorldCoordinate(1, 1).distance(new WorldCoordinate(2, 1))).isEqualTo(1f);
        assertThat(new WorldCoordinate(1, 1).distance(new WorldCoordinate(2, 2))).isEqualTo(1.4142135f);
        assertThat(new WorldCoordinate(1, 1).distance(new WorldCoordinate(3, 3))).isEqualTo(2.828427f);
    }

    @Test
    public void testIsCardinal() {
        var worldCoordinate = new WorldCoordinate(5, 5);

        assertThat(worldCoordinate.isCardinal(worldCoordinate.north())).isTrue();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.east())).isTrue();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.south())).isTrue();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.west())).isTrue();

        assertThat(worldCoordinate.isCardinal(worldCoordinate.north().north())).isFalse();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.east().east())).isFalse();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.south().south())).isFalse();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.west().west())).isFalse();

        assertThat(worldCoordinate.isCardinal(worldCoordinate.northEast())).isFalse();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.southEast())).isFalse();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.southWest())).isFalse();
        assertThat(worldCoordinate.isCardinal(worldCoordinate.northWest())).isFalse();
    }

    @Test
    public void testIsIntercardinal() {
        var worldCoordinate = new WorldCoordinate(5, 5);

        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.northEast())).isTrue();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.southEast())).isTrue();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.southWest())).isTrue();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.northWest())).isTrue();

        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.northEast().northEast())).isFalse();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.southEast().southEast())).isFalse();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.southWest().southWest())).isFalse();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.northWest().northWest())).isFalse();

        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.north())).isFalse();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.east())).isFalse();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.south())).isFalse();
        assertThat(worldCoordinate.isIntercardinal(worldCoordinate.west())).isFalse();
    }

    @Test
    public void testNorth() {
        assertThat(new WorldCoordinate(5, 5).north()).isEqualTo(new WorldCoordinate(5, 6));
    }

    @Test
    public void testNorthEast() {
        assertThat(new WorldCoordinate(5, 5).northEast()).isEqualTo(new WorldCoordinate(6, 6));
    }

    @Test
    public void testEast() {
        assertThat(new WorldCoordinate(5, 5).east()).isEqualTo(new WorldCoordinate(6, 5));
    }

    @Test
    public void testSouthEast() {
        assertThat(new WorldCoordinate(5, 5).southEast()).isEqualTo(new WorldCoordinate(6, 4));
    }

    @Test
    public void testSouth() {
        assertThat(new WorldCoordinate(5, 5).south()).isEqualTo(new WorldCoordinate(5, 4));
    }

    @Test
    public void testSouthWest() {
        assertThat(new WorldCoordinate(5, 5).southWest()).isEqualTo(new WorldCoordinate(4, 4));
    }

    @Test
    public void testWest() {
        assertThat(new WorldCoordinate(5, 5).west()).isEqualTo(new WorldCoordinate(4, 5));
    }

    @Test
    public void testNorthWest() {
        assertThat(new WorldCoordinate(5, 5).northWest()).isEqualTo(new WorldCoordinate(4, 6));
    }

}