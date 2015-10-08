package net.ncguy.uml.global;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Base64Coder;

/**
 * Created by Nick on 30/09/2015 at 23:21,
 * Project: UMLEditor.
 */
public class Sprites {

    public static Sprite pixel;
    public static Sprite actor;
    public static Sprite useCase;

    public static void initSprites() {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.drawPixel(0, 0);
        pixel = new Sprite(new Texture(map));
        map.dispose();

        byte[] actorBytes = Base64Coder.decode(Base64Strings.ACTOR);
        actor = new Sprite(new Texture(new Pixmap(actorBytes, 0, actorBytes.length)));

        int mapSize = 2048;

        map = new Pixmap(mapSize, mapSize, Pixmap.Format.RGBA8888);
        map.setColor(0, 0, 0, 1);
        map.fillCircle(mapSize/2, mapSize/2, (mapSize/2));
        map.setColor(1, 1, 1, 1);
        map.fillCircle(mapSize/2, mapSize/2, (mapSize/2)-32);
        Texture useCaseReg = new Texture(map);
        useCaseReg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        useCase = new Sprite(useCaseReg);
        map.dispose();
    }

    public static Sprite newPixel() {
        return new Sprite(pixel);
    }

    public static class Base64Strings {
        public static final String ACTOR = "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";
    }

}
