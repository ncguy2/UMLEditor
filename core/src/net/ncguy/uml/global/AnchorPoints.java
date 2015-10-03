package net.ncguy.uml.global;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Nick on 01/10/2015 at 21:08,
 * Project: UMLEditor.
 */
public enum AnchorPoints {
    TOPLEFT(0, 1),      TOP(.5f, 1),    TOPRIGHT(1, 1),
    MIDLEFT(0, .5f),    MID(.5f, .5f),  MIDRIGHT(1,.5f),
    BOTLEFT(0, 0),      BOT(.5f, 0),    BOTRIGHT(1, 0),
    ;

    AnchorPoints(float x, float y) {
        this.x = x;
        this.y = y;
    }

    float x, y;

    public Vector2 offset() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return name().toUpperCase().toCharArray()[0] + name().toLowerCase().substring(1);
    }

    public static AnchorPoints getPointFromValues(float x, float y) {
        for(AnchorPoints point : values()) {
            if(x >= (point.x-.25f) && x < (point.x+.25f)) {
                if(y >= (point.y-.25f) && y < (point.y+.25f)) {
                    return point;
                }
            }
        }
        return MID;
    }
    public static AnchorPoints getPointFromVector(Vector2 pos) {
        return getPointFromValues(pos.x, pos.y);
    }

}
