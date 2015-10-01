package net.ncguy.uml.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.ncguy.uml.elements.ElementController;

/**
 * Created by Nick on 30/09/2015 at 22:28,
 * Project: UMLEditor.
 */
public class CornerActor extends Actor {

    public ElementController controller;
    public int size;

    public CornerActor(ElementController controller, int size) {
        super();
        sprite = new Sprite(new Texture("assets/corner.png"));
        this.controller = controller;
        this.size = size;
    }

    Sprite sprite;

    public void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.setColor(Color.CYAN);
        sprite.setBounds(getX(), getY(), size, size);
        sprite.draw(batch, alpha);
    }

}
