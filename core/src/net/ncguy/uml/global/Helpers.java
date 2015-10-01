package net.ncguy.uml.global;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Nick on 30/09/2015 at 23:24,
 * Project: UMLEditor.
 */
public class Helpers {

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

}
