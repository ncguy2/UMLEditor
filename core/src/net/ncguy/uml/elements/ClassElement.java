package net.ncguy.uml.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisLabel;
import net.ncguy.uml.components.TwinTextArea;
import net.ncguy.uml.elements.data.LineData;
import net.ncguy.uml.global.Sprites;

import java.util.ArrayList;

/**
 * Created by Nick on 08/10/2015 at 22:07,
 * Project: UMLEditor.
 */
public class ClassElement extends EditorElement {

    public ArrayList<VisLabel> attributeLabel, methodLabel;
    public VisLabel nameLabel;

    public ClassElement() {
        super();
        sprite = Sprites.newPixel();
        ClassData d = new ClassData();
        d.attributes = new ArrayList<>();
        d.methods = new ArrayList<>();
        data.contents = d;
        setBasePosition((Gdx.graphics.getWidth()/2)-(getWidth()/2), (Gdx.graphics.getHeight()/2)-(getHeight()/2));
        attributeLabel = new ArrayList<>();
        methodLabel = new ArrayList<>();
        nameLabel = new VisLabel(data.name);
        update();
        addActor(nameLabel);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(data.lineData == null) {
            data.lineData = new ArrayList<>();
        }
        for(LineData line : data.lineData) {
            line.draw(batch, alpha);
        }

        if(data.contents instanceof ClassData) {
            float off1 = 20*methodLabel.size();
            float off2 = 20*attributeLabel.size();
            off2 += off1;
            float off3 = off2+30;
            drawBG(batch, alpha, new Color(.1f, .1f, .1f, 1));
            draw(batch, alpha, getX(), getY(), baseSize.x, 3);
            draw(batch, alpha, getX(), getY() + off1, baseSize.x, 3);
            draw(batch, alpha, getX(), getY() + off2, baseSize.x, 3);
            draw(batch, alpha, getX(), getY() + off3, baseSize.x, 3);
            draw(batch, alpha, getX(), getY(), 3, off3);
            draw(batch, alpha, getX() + baseSize.x, getY(), 3, off3+3);
            setBaseH(off3);

            int index = 1;
            for(VisLabel lbl : attributeLabel) {
                lbl.setPosition(getX()+5, getY()+(off2-(20*index++)));
                lbl.draw(batch, 1);
            }
            index = 1;
            for(VisLabel lbl : methodLabel) {
                lbl.setPosition(getX()+5, getY()+(off1-(20*index++)));
                lbl.draw(batch, 1);
            }

            nameLabel.setPosition(getX()+5, (getY()+getHeight())-25);
            nameLabel.setText(data.name);
            nameLabel.draw(batch, alpha);
        }

//        super.draw(batch, alpha);
    }

    @Override
    public void contentHandle(Actor contentActor) {
        if(contentActor instanceof TwinTextArea) {
            TwinTextArea content = (TwinTextArea)contentActor;
            if(data.contents instanceof ClassData)
                content.setColLines((ClassData)data.contents);
        }
        // TODO configure class elements dataDialog content handling
    }

    public void updateFromData() {
        System.out.println("Updating from data");
        if(data.contents instanceof ClassData) {
            attributeLabel.clear();
            for(String s : ((ClassData) data.contents).attributes) {
                attributeLabel.add(new VisLabel(s));
            }
            methodLabel.clear();
            for(String s : ((ClassData) data.contents).methods) {
                methodLabel.add(new VisLabel(s));
            }
        }
        update();
    }

    public void update() {
        for(VisLabel lbl : attributeLabel) {
            lbl.remove();
            addActor(lbl);
        }
        for(VisLabel lbl : methodLabel) {
            lbl.remove();
            addActor(lbl);
        }
    }

    private void draw(Batch batch, float alpha, float x, float y, float w, float h) {
        sprite.setBounds(x, y, w, h);
        sprite.draw(batch, alpha);
    }
    private void drawBG(Batch batch, float alpha, Color colour) {
        Color origCol = new Color(sprite.getColor());
        sprite.setColor(colour);
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch, alpha);
        sprite.setColor(origCol);
    }

    public static class ClassData {
        public ArrayList<String> attributes;
        public ArrayList<String> methods;
    }

}
