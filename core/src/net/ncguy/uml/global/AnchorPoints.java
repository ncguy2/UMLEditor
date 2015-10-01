package net.ncguy.uml.global;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static net.ncguy.uml.global.AnchorPoints.AnchorIndex.*;

/**
 * Created by Nick on 01/10/2015 at 21:08,
 * Project: UMLEditor.
 */
public class AnchorPoints {

    public Vector2[] points;

    public AnchorPoints() {
        points = new Vector2[AnchorIndex.values().length];

        points[TOPLEFT.ordinal()]   = new Vector2(0,     1);
        points[TOP.ordinal()]       = new Vector2(.5f,   1);
        points[TOPRIGHT.ordinal()]  = new Vector2(1,     1);
        points[MIDLEFT.ordinal()]   = new Vector2(0,     .5f);
        points[MID.ordinal()]       = new Vector2(.5f,   .5f);
        points[MIDRIGHT.ordinal()]  = new Vector2(1,     .5f);
        points[BOTLEFT.ordinal()]   = new Vector2(0,     0);
        points[BOT.ordinal()]       = new Vector2(.5f,   0);
        points[BOTRIGHT.ordinal()]  = new Vector2(1,     0);
    }

    public Vector2 getAnchorPointOfActor(AnchorIndex anchor, Actor a) {
        Vector2 pos = new Vector2();
        pos.x = a.getX()+(a.getWidth()*points[anchor.ordinal()].x);
        pos.y = a.getY()+(a.getHeight()*points[anchor.ordinal()].y);
        return pos;
    }

    public enum AnchorIndex {
        TOPLEFT, TOP, TOPRIGHT,
        MIDLEFT, MID, MIDRIGHT,
        BOTLEFT, BOT, BOTRIGHT,
        ;
    }

}
