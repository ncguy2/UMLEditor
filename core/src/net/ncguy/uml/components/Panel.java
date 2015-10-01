package net.ncguy.uml.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.ncguy.uml.global.Sprites;

/**
 * Created by Nick on 30/09/2015 at 21:46,
 * Project: UMLEditor.
 */
public class Panel extends Actor {

    Sprite sprite;
    Color colour;

    public Panel(Color colour) {
        super();
        this.sprite = new Sprite(Sprites.pixel);
        this.colour = colour;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.setColor(colour);
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch, alpha);
    }

}
