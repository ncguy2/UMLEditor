package net.ncguy.uml.drawable;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Nick on 01/10/2015 at 16:36,
 * Project: UMLEditor.
 */
public class Assets {
    public static TextureAtlas icons;
    public static TextureAtlas misc;

    public static ShaderProgram distanceFieldShader;

    public static void load () {
        icons = new TextureAtlas("assets/icons/icons.atlas");
//        misc = new TextureAtlas("icons/misc.atlas");
    }

    public static void dispose () {
        icons.dispose();
        misc.dispose();
    }

    public static Drawable getIcon (Icons icon) {
        return new TextureRegionDrawable(getIconRegion(icon));
    }

    public static TextureRegion getIconRegion (Icons icon) {
        return icons.findRegion(icon.getIconName());
    }

    public static Drawable getIcon(String key) {
        return new TextureRegionDrawable(getIconRegion(key));
    }
    public static TextureRegion getIconRegion(String key) {
        return icons.findRegion(key);
    }

    public static Drawable getMisc (String name) {
        return new TextureRegionDrawable(getMiscRegion(name));
    }

    public static TextureRegion getMiscRegion (String name) {
        return misc.findRegion(name);
    }
}
