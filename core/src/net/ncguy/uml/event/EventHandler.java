package net.ncguy.uml.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ncguy on 21/09/2015 at 18:24,
 * Project: Moniter.
 */
public class EventHandler {

    private static HashMap<String, List<IEvent>> registeredHandlers;

    public static HashMap<String, List<IEvent>> getRegisteredHandlers() {
        if(registeredHandlers == null) registeredHandlers = new HashMap<>();
        return registeredHandlers;
    }

    public static void addEventToHandler(String handler, IEvent event) {
        List<IEvent> eventList;
        if(getRegisteredHandlers().get(handler) == null) {
            eventList = new ArrayList<>();
        }else{
            eventList = getRegisteredHandlers().get(handler);
        }
        eventList.add(event);
        getRegisteredHandlers().put(handler, eventList);
    }

    public static List<IEvent> getEventsByHandler(String handler) {
        if(getRegisteredHandlers().containsKey(handler)) {
            return getRegisteredHandlers().get(handler);
        }
        return null;
    }

    public static void executeEventsByHandler(String handler, Object... args) {
        if(getRegisteredHandlers().get(handler) == null) return;
        List<IEvent> eventList = getRegisteredHandlers().get(handler);
        for(IEvent event : eventList) {
            event.run(args);
        }
    }

}