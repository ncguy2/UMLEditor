package net.ncguy.uml.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.sun.istack.internal.NotNull;
import net.ncguy.uml.event.EventHandler;
import net.ncguy.uml.event.IEvent;

/**
 * Created by Nick on 07/10/2015 at 23:30,
 * Project: UMLEditor.
 */
public class VisProgressBarEvent extends VisProgressBar {

    private boolean eventCooldown = false;
    private String eventId = "ProgressBar.";

    public VisProgressBarEvent(float min, float max, float stepSize, boolean vertical, String eventId) {
        super(min, max, stepSize, vertical);
        this.eventId = "ProgressBar."+eventId;
        setDuration((int)max, 60);
        setAnimateDuration(.2f);
    }

    public void addEvent(@NotNull IEvent event) {
        EventHandler.addEventToHandler(eventId, event);
    }

    public void setDurationH(int hours)           { setDurationM(hours , 60); }
    public void setDurationH(int hours, int fps)  { setDurationM(hours , fps); }
    public void setDurationM(int minutes)         { setDuration(minutes, 60); }
    public void setDurationM(int minutes, int fps){ setDuration(minutes, fps); }
    public void setDuration(int seconds)          { setDuration(seconds, 60); }
    public void setDuration(int seconds, int fps) { setRange(0, seconds * fps); }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setValue((getValue()+getStepSize())%getMaxValue());
        super.draw(batch, parentAlpha);
        if(eventCooldown) {
            if(getValue() < (getMaxValue() / 20.f)) {
                EventHandler.executeEventsByHandler(this.eventId);
                eventCooldown = false;
            }
        }else{
            if(getValue() > (getMaxValue() / 20.0f)) eventCooldown = true;
        }
    }
}
