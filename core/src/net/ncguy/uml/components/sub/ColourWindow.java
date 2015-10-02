package net.ncguy.uml.components.sub;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.event.EventHandler;

import java.lang.reflect.Field;

/**
 * Created by Nick on 02/10/2015 at 20:46,
 * Project: UMLEditor.
 */
public class ColourWindow implements InputProcessor {

    public ColorPicker colWin;
    private static Field colF, oldColF;
    private String eventId;

    public ColourWindow(String title) {
        colWin = new ColorPicker(title);
        colWin.setModal(false);
        colWin.setColor(Color.WHITE);
        colWin.setResizable(false);
        if(colF == null)
            try{
                colF = colWin.getClass().getDeclaredField("color");
                colF.setAccessible(true);
            }catch(NoSuchFieldException nsfe) { nsfe.printStackTrace(); }
        if(oldColF == null)
            try{
                oldColF = colWin.getClass().getDeclaredField("oldColor");
                oldColF.setAccessible(true);
            }catch(NoSuchFieldException nsfe) { nsfe.printStackTrace(); }

        colWin.setListener(new ColorPickerAdapter(){
            @Override
            public void canceled() {
                Color col = new Color(colour);
                try {
                    col = (Color)oldColF.get(colWin);
                }catch(Exception nsfe) {nsfe.printStackTrace();}
                colour = col;
                bail();
            }
            @Override
            public void finished(Color newColour) {
                colour = newColour;
                bail();
            }
        });
    }
    public Color getDynamicColour() {
        Color col = new Color(colour);
        if(colF != null) {
            try {
                col = (Color)colF.get(colWin);
            }catch(IllegalAccessException iae) { iae.printStackTrace(); }
        }
        return col;
    }
    public Color getColour() {
        return colour;
    }
    public Color colour = new Color(1,1,1,1);

    public VisWindow fadeIn(String eventId) {
        this.eventId = eventId;
        if(!UMLLauncher.instance.display.multiplexer.getProcessors().contains(this, true))
            UMLLauncher.instance.display.multiplexer.addProcessor(0, this);
        colWin.setVisible(true);
        Actor a = UMLLauncher.instance.display.currentElement;
        Color defCol = new Color();
        if(a instanceof EditorElement) {
            EditorElement e = (EditorElement)a;
            defCol.set(e.data.colour);
        }else{
            defCol.set(1, 1, 1, 1);
        }
        colWin.setColor(defCol);
        return colWin.fadeIn();
    }
    public void bail() {
        if(UMLLauncher.instance.display.multiplexer.getProcessors().contains(this, true))
            UMLLauncher.instance.display.multiplexer.removeProcessor(this);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        colour = getDynamicColour();
        EventHandler.executeEventsByHandler(eventId, colour);
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int btn) {
        colour = getDynamicColour();
        EventHandler.executeEventsByHandler(eventId, colour);
        return false;
    }

    // Irrelevant
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}