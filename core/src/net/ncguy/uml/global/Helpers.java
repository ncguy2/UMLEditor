package net.ncguy.uml.global;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Nick on 30/09/2015 at 23:24,
 * Project: UMLEditor.
 */
public class Helpers {

    public static Sprite arrowHead;

    public static void lineDraw(Sprite dot, Batch batch, Color colour, float x1, float y1, float x2, float y2, float width) {
        double xDiff = x2 - x1; double yDiff = y2 - y1;
        float zLength = (float)Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        float angle = ((float)Math.toDegrees(Math.atan(yDiff/xDiff)));
        dot.setColor(colour);
        dot.setPosition(x1, y1);
        dot.setSize(zLength, width);
        if(xDiff < 0) angle += 180;
        dot.setRotation(angle);
        dot.setBounds(dot.getX(), dot.getY(), dot.getWidth(), dot.getHeight());
        dot.draw(batch);
    }

    public static void dashedLineDraw(Sprite dot, Batch batch, Color colour, float x1, float y1, float x2, float y2, float width, float dashSize) {
        double xDiff = x2 - x1; double yDiff = y2 - y1;
        float zLength = (float)Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        float numDashes = (zLength*dashSize)/10;
        Vector2 p1 = new Vector2(x1, y1);
        Vector2 p2 = new Vector2(x2, y2);
        for (int i = 0; i < numDashes; i++) {
            float start = (float)i / (float)numDashes;
            float end = (i + dashSize) / (float)numDashes;
            lineDraw(dot, batch, colour,
                    p1.x+(p2.x-p1.x)*start, p1.y+(p2.y-p1.y)*start,
                    p1.x+(p2.x-p1.x)*end,   p1.y+(p2.y-p1.y)*end, width);
        }
    }

    public static void drawArrowHead(Batch batch, Color colour, Vector2 pos1, Vector2 pos2) {
        if(arrowHead == null) arrowHead = Sprites.newArrowHead();
        double xDiff = pos2.x - pos1.x; double yDiff = pos2.y - pos1.y;
//        float zLength = (float)Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        float angle = ((float)Math.toDegrees(Math.atan(yDiff/xDiff)));
//        arrowHead.setOrigin(arrowHead.getWidth(), arrowHead.getHeight());
        arrowHead.setSize(16, 16);
        arrowHead.setOrigin(arrowHead.getWidth()/2, arrowHead.getHeight()/2);
//        arrowHead.setOrigin(0, 0);
        arrowHead.setColor(colour);
        arrowHead.setPosition(pos2.x-(arrowHead.getWidth()/2), pos2.y-(arrowHead.getHeight()/2));
        if(xDiff < 0) angle += 180;
        arrowHead.setRotation(angle);
        arrowHead.draw(batch);
    }

}
