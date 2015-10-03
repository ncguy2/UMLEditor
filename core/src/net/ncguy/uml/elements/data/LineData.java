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

    private void drawDottedLine(Color col, int dotDist, float x1, float y1, float x2, float y2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        shapeRenderer.setColor(col);
        Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
        float length = vec2.len();
        for(int i = 0; i < length; i += dotDist) {
            vec2.clamp(length - i, length - i);
            shapeRenderer.point(x1 + vec2.x, y1 + vec2.y, 0);
        }
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();
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
