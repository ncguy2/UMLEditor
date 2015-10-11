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
    public static Sprite solidCircle;
    public static Sprite actor;
    public static Sprite useCase;
    public static Sprite arrowHead;

    public static void initSprites() {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.drawPixel(0, 0);
        pixel = new Sprite(new Texture(map));
        map.dispose();

        byte[] actorBytes = Base64Coder.decode(Base64Strings.ACTOR);
        byte[] arrowBytes = Base64Coder.decode(Base64Strings.ARROWHEAD);
        actor = new Sprite(new Texture(new Pixmap(actorBytes, 0, actorBytes.length)));
        arrowHead = new Sprite(new Texture(new Pixmap(arrowBytes, 0, arrowBytes.length)));
//        arrowHead = new Sprite(new Texture("assets/utils/arrowhead.png"));

        int mapSize = 2048;

        map = new Pixmap(mapSize, mapSize, Pixmap.Format.RGBA8888);
        map.setColor(0, 0, 0, 1);
        map.fillCircle(mapSize / 2, mapSize / 2, (mapSize / 2));
        map.setColor(1, 1, 1, 1);
        map.fillCircle(mapSize / 2, mapSize / 2, (mapSize / 2) - 32);
        Texture useCaseReg = new Texture(map);
        useCaseReg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        useCase = new Sprite(useCaseReg);
        map.dispose();

        map = new Pixmap(mapSize, mapSize, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.fillCircle(mapSize / 2, mapSize / 2, (mapSize / 2));
        Texture circleReg = new Texture(map);
        circleReg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        solidCircle = new Sprite(circleReg);
        map.dispose();
    }

    public static Sprite newPixel() { return new Sprite(pixel); }
    public static Sprite newArrowHead() { return new Sprite(arrowHead); }
    public static Sprite newSolidCircle() { return new Sprite(solidCircle); }

    public static class Base64Strings {
        public static final String ACTOR = "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";
        public static final String ARROWHEAD = "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";
    }

}
