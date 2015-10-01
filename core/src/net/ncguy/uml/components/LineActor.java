package net.ncguy.uml.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import net.ncguy.uml.elements.ElementController;
import net.ncguy.uml.global.Helpers;
import net.ncguy.uml.global.Sprites;

/**
 * Created by Nick on 30/09/2015 at 23:20,
 * Project: UMLEditor.
 */
public class LineActor extends Actor {

    public Sprite sprite;
    public Color colour;
    public Actor a1, a2;
    public ElementController ctrl;
    public ElementController.PointIndex key;
    public ResizeSingleAxisDrag resizeListener;

    public LineActor(Actor a1, Actor a2, ElementController ctrl) {
        super();
        this.a1 = a1;
        this.a2 = a2;
        this.ctrl = ctrl;
        sprite = new Sprite(Sprites.pixel);
        colour = Color.CYAN;
    }

    public void setBounds(Rectangle rect) {
//        setBounds(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        setX(rect.getX()-2);
        setY(rect.getY() - 2);
        setWidth(rect.getWidth() + 4);
        setHeight(rect.getHeight() + 4);
        setOriginX((getWidth() / 2));
        setOriginY((getHeight()/2));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        Helpers.lineDraw(sprite, batch, colour,
                a1.getX() + ctrl.halfCornerSize, a1.getY() + ctrl.halfCornerSize,
                a2.getX() + ctrl.halfCornerSize, a2.getY() + ctrl.halfCornerSize,
                1);
        setBounds(sprite.getBoundingRectangle());

//        sprite.setPosition(a1.getX(), a1.getY());
//        sprite.setSize(a2.getX()-a1.getX(), a2.getY()-a1.getY());
//        sprite.draw(batch, alpha);
    }


    public void initResizeListener() {
        resizeListener = new ResizeSingleAxisDrag(this, ctrl.controlledElement);
        addListener(resizeListener);
    }

    public static class ResizeSingleAxisDrag extends DragListener {
        LineActor self;
        Actor ctrl;
        public ResizeSingleAxisDrag(LineActor self, Actor controlled) {
            super();
            this.self = self;
            this.ctrl = controlled;
        }
        public void touchDragged(InputEvent event, float x1, float y1, int pointer){
            ElementController.PointIndex key = self.key;
            LineActor opposite = self.ctrl.lines[key.opposite()];
            if(self.getWidth() > self.getHeight()) {
                float y = Gdx.graphics.getHeight() - Gdx.input.getY();
                float modY = (y - self.getOriginY());
                if(key.name().toLowerCase().contains("top")) {
                if(modY < opposite.getY()+self.ctrl.halfCornerSize)
                    modY = opposite.getY()+self.ctrl.halfCornerSize;
                }else{
                if(modY > opposite.getY()-self.ctrl.halfCornerSize)
                    modY = opposite.getY()-self.ctrl.halfCornerSize;
                }
                self.a1.setY(modY);
            }else{
                float x = Gdx.input.getX();
                float modX = (x - self.getOriginX());
                if(key.name().toLowerCase().contains("left")) {
                    if(modX > opposite.getX()-self.ctrl.halfCornerSize)
                        modX = opposite.getX()-self.ctrl.halfCornerSize;
                }else{
                    if(modX < opposite.getX()+self.ctrl.halfCornerSize)
                        modX = opposite.getX()+self.ctrl.halfCornerSize;
                }
                self.a1.setX(modX);
            }
            if(self.a1 instanceof CornerActor)
                self.ctrl.assertPoints((CornerActor)self.a1);
        }
    }

}

