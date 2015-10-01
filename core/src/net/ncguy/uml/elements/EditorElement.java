package net.ncguy.uml.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Nick on 30/09/2015 at 20:58,
 * Project: UMLEditor.
 */
public class EditorElement extends Actor {

    Sprite sprite;
    Vector2 baseLocation;

    public EditorElement() {
        sprite = new Sprite(new Texture("assets/badlogic.jpg"));
        baseLocation = new Vector2();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        baseLocation.set(x, y);
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        baseLocation.set(x, y);
    }

    @Override
    public void setBounds(float x, float y, float w, float h) {
        super.setBounds(x, y, w, h);
        baseLocation.set(x, y);
    }

    @Override public void setX(float x) {
        super.setX(x);
        baseLocation.x = x;
    }

    @Override public void setY(float y) {
        super.setY(y);
        baseLocation.y = y;
    }


    @Override
    public void draw(Batch batch, float alpha) {
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch, alpha);
    }

    public void redraw(Vector2 offset) {
        setX(baseLocation.x + offset.x);
        setY(baseLocation.y + offset.y);
    }
}
