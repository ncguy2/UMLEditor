package net.ncguy.uml.global;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Nick on 30/09/2015 at 23:21,
 * Project: UMLEditor.
 */
public class Sprites {

    public static Sprite pixel;

    public static void initSprites() {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.drawPixel(0, 0);
        pixel = new Sprite(new Texture(map));
    }

}
