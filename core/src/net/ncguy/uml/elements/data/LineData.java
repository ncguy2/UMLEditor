package net.ncguy.uml.elements.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.global.AnchorPoints;
import net.ncguy.uml.global.Helpers;
import net.ncguy.uml.global.Sprites;

/**
 * Created by Nick on 01/10/2015 at 22:19,
 * Project: UMLEditor.
 */
public class LineData {

    public String name;
    public Vector2 localAnchor, remoteAnchor;
    public String remoteActorName;
    public LineType lineType;

    public transient EditorElement parentActor;
    public transient EditorElement remoteActor;

    public static transient Sprite sprite;
    public Color colour;

    private static ShapeRenderer shapeRenderer;

    public LineData() {
        remoteAnchor = AnchorPoints.MID.offset();
        remoteActor = null;
        remoteActorName = "";
        lineType = LineType.ASSOCIATE;
        shapeRenderer = new ShapeRenderer();
    }

    public void draw(Batch batch, float alpha) {
        if(sprite == null) sprite = new Sprite(Sprites.pixel);
        if(colour == null) colour = Color.WHITE;

        if(remoteAnchor == null) remoteAnchor = AnchorPoints.MID.offset();

        try {
            switch(lineType) {
                case EXTEND: case INCLUDE:
                    Helpers.dashedLineDraw(sprite, batch, colour,
                            getLocalAnchorX(), getLocalAnchorY(),
                            getRemoteAnchorX(), getRemoteAnchorY(),
                            2, .5f);
                    Helpers.drawArrowHead(batch, colour,
                            new Vector2(getLocalAnchorX(), getLocalAnchorY()),
                            new Vector2(getRemoteAnchorX(), getRemoteAnchorY()));
                    break;
                default:
                    Helpers.lineDraw(sprite, batch, colour,
                            getLocalAnchorX(), getLocalAnchorY(),
                            getRemoteAnchorX(), getRemoteAnchorY(), 2);
            }
        }catch(Exception e) {}
    }

    public float getLocalAnchorX() {
        return parentActor.getX()+(parentActor.getWidth()*localAnchor.x);
    }
    public float getLocalAnchorY() {
        return parentActor.getY()+(parentActor.getHeight()*localAnchor.y);
    }

    public float getRemoteAnchorX() {
        return remoteActor.getX()+(remoteActor.getWidth()*remoteAnchor.x);
    }
    public float getRemoteAnchorY() {
        return remoteActor.getY()+(remoteActor.getHeight()*remoteAnchor.y);
    }

    @Override
    public String toString() {
        if(name != null) {
            if(name.replaceAll("/ /", "").length() >= 1) return name;
        }
        String out = "";
        out += parentActor != null ? parentActor.data.name : "None";
        out += " | ";
        out += remoteActor != null ? remoteActor.data.name : "None";
        return out;
    }

    public enum LineType {
        ASSOCIATE,
        INCLUDE,
        EXTEND,
        ;

        @Override
        public String toString() {
            return name().toUpperCase().toCharArray()[0] + name().toLowerCase().substring(1);
        }
    }

}
