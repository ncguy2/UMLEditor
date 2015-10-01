package net.ncguy.uml.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Nick on 30/09/2015 at 21:01,
 * Project: UMLEditor.
 */
public class Tracker {

    public static boolean isCursorOver(float x, float y, float w, float h) {
        if(Gdx.input.getX() >= x && Gdx.input.getX() <= x+w) {
            if(Gdx.graphics.getHeight()-Gdx.input.getY() >= y && Gdx.graphics.getHeight()-Gdx.input.getY() <= y+h) {
                return true;
            }
        }
        return false;
    }
    public static boolean isCursorOverActor(Actor actor) {
        float x, y, w, h;
        x = actor.getX();
        y = actor.getY();
        w = actor.getWidth();
        h = actor.getHeight();
        if(Gdx.input.getX() >= x && Gdx.input.getX() <= x+w) {
            if(Gdx.graphics.getHeight()-Gdx.input.getY() >= y && Gdx.graphics.getHeight()-Gdx.input.getY() <= y+h) {
                return true;
            }
        }
        return false;
    }

}
