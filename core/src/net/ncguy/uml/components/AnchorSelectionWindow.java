package net.ncguy.uml.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;
import net.ncguy.uml.event.EventHandler;
import net.ncguy.uml.global.Sprites;

/**
 * Created by Nick on 11/10/2015 at 01:48,
 * Project: UMLEditor.
 */
public class AnchorSelectionWindow extends VisWindow {

    VisSlider xSlider, ySlider;
    Sprite sprite;

    private Sprite bgSprite;

    ChangeListener changeListener;
    final String eventId;

    public AnchorSelectionWindow(String title) {
        super(title);
        this.eventId = "anchorSelection."+title+".change";
        addCloseButton();
        sprite = Sprites.newPixel();
        xSlider = new VisSlider(0, 100, 1, false);
        ySlider = new VisSlider(0, 100, 1, true);

        addActor(xSlider);
        addActor(ySlider);

        setSize(240, 240 + getPadTop());
        setX((Gdx.graphics.getWidth() / 2) - (getWidth() / 2));
        setY((Gdx.graphics.getHeight() / 2) - (getHeight() / 2));

        changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EventHandler.executeEventsByHandler(eventId, getOffset());
            }
        };
        xSlider.addListener(changeListener);
        ySlider.addListener(changeListener);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(x < 5 || y < 5) return;
                if(x > 205 || y > 205) return;
                xSlider.setValue((x / 2)-6);
                ySlider.setValue((y / 2)-6);
            }
        });
        addListener(new DragListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                if(x < 5 || y < 5) return;
                if(x > 205 || y > 205) return;
                xSlider.setValue((x / 2)-5);
                ySlider.setValue((y / 2)-5);
            }
        });
    }

    private void drawGrid(Batch batch, float alpha) {
        drawBG(batch, alpha);
        sprite.setColor(Color.WHITE);
        sprite.setAlpha(.2f);
        float x = getX();
        float y = getY();
        for(int i = 0; i <= 10; i++) {
            sprite.setBounds(x + 5 + (20 * i), y + 5, 1, 200);
            sprite.draw(batch, alpha);
            sprite.setBounds(x + 5, y + 5 + (20 * i), 200, 1);
            sprite.draw(batch, alpha);
        }
        sprite.setAlpha(1);

        sprite.setBounds(x + (xSlider.getValue() * 2) + 5, y + 5, 1, 200);
        sprite.draw(batch, alpha);
        sprite.setBounds(x + 5, y + (ySlider.getValue() * 2) + 5, 200, 1);
        sprite.draw(batch, alpha);

        sprite.setColor(Color.CYAN);
        sprite.setAlpha(.5f);
        sprite.setBounds(x + (xSlider.getValue() * 2) + 3, y + (ySlider.getValue() * 2) + 3, 5, 5);
        sprite.draw(batch, alpha);
        sprite.setAlpha(1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        xSlider.setBounds(5, 210, 200, 25);
        ySlider.setBounds(210, 5, 25, 200);
        super.draw(batch, parentAlpha);
        drawGrid(batch, parentAlpha);
    }

    public Vector2 getOffset() {
        Vector2 off = new Vector2();
        off.x = Float.parseFloat(String.format("%.2f", xSlider.getValue()/100f));
        off.y = Float.parseFloat(String.format("%.2f", ySlider.getValue()/100f));
        return off;
    }

    public void setBG(Sprite sprite) {
        if(sprite == null) bgSprite = null;
        bgSprite = new Sprite(sprite);
    }

    public void drawBG(Batch batch, float alpha) {
        if(bgSprite == null) return;
        try {
            bgSprite.setAlpha(.4f);
            bgSprite.setBounds(getX() + 5, getY() + 5, 200, 200);
            bgSprite.draw(batch, alpha);
        }catch(Exception e) {
            e.printStackTrace();

        }
    }
}
