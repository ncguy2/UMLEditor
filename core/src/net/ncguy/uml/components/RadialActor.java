package net.ncguy.uml.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import net.ncguy.uml.event.EventHandler;
import net.ncguy.uml.sprite.RadialSprite;

/**
 * Created by Nick on 07/10/2015 at 19:51,
 * Project: UMLEditor.
 */
public class RadialActor extends VisProgressBar {

    public RadialSprite sprite;
    public Sprite sprit;
    private float maxVal;
    private boolean eventCooldown = false;
    private String eventId = "radialActor.";

    public RadialActor(float min, float max, String eventId) {
        super(min, max, 1, false);
        this.eventId = "radialActor."+eventId;
        this.maxVal = max-min;
//        sprite = new RadialSprite(new TextureRegion(Sprites.newPixel().getTexture()));
        sprite = new RadialSprite(new TextureRegion(new Texture("assets/blueCircle.png")));
        setDurationM(1);
        setAnimateDuration(.2f);
    }

    public String getEventId() {
        return this.eventId;
    }

    @Override
    public void setRange(float min, float max) {
        super.setRange(min, max);
        this.maxVal = max-min;
    }

    public void increment() {
        float val = getValue();
        float max = getMaxValue();
        setValue((val+1f)%max);
    }

    public void setDurationH(int hours)           { setDurationM(hours*60); }
    public void setDurationM(int minutes)         { setDuration(minutes*60); }
    public void setDuration(int seconds)          { setDuration(seconds, 60); }
    public void setDuration(int seconds, int fps) { setRange(0, seconds*fps); }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        increment();
        if(sprite == null) return;
        float visVal = getVisualValue();
        float angle = (((visVal/maxVal)*100.0f)/100.0f)*360.0f;
        sprite.currentAngle = angle;
        sprite.rotate(1
        );
        sprite.draw(batch, parentAlpha);
//        sprite.draw(batch, getX(), getY(), getWidth(), getHeight(), angle);
        if(eventCooldown) {
            if(angle < (maxVal / 20.f)) {
                EventHandler.executeEventsByHandler(this.eventId);
                eventCooldown = false;
            }
        }else{
            if(angle > (maxVal / 20.0f)) eventCooldown = true;
        }

//        super.draw(batch, parentAlpha);
    }
}
