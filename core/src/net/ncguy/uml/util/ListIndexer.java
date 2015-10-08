package net.ncguy.uml.util;

import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.data.LineData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Nick on 07/10/2015 at 16:14.
 * Project: UMLEditor
 * Package: net.ncguy.uml.util
 */
public class ListIndexer {

    private Thread indexThread;
    private Object parentInstance;
    private Field fieldIn;
    private Field fieldOut;
    private Class parentClass;

    private String fieldInName, fieldOutName;

    private Runnable indexThreadRunnable;

    public ListIndexer(Object parentInstance, String fieldInName, String fieldOutName) {
        try {
            this.parentInstance = parentInstance;
            this.fieldInName = fieldInName;
            this.fieldOutName = fieldOutName;
            parentClass = Class.forName(parentInstance.getClass().getCanonicalName());
            System.out.println("Fields in " + parentClass.getCanonicalName());
            fieldIn = parentClass.getField(fieldInName);
            fieldOut = parentClass.getField(fieldOutName);
        }catch(Exception e) {
            e.printStackTrace();
        }
        indexThreadRunnable = () -> {
            System.out.println("PreIndex: "+System.currentTimeMillis());
            try {
                if(fieldIn.get(parentInstance) instanceof Collection) {
                    if(fieldOut.get(parentInstance) instanceof Collection) {
                        if(fieldIn.getType().equals(fieldOut.getType())) {
                            ArrayList<Object> itemsIn = new ArrayList<>();
                            ArrayList<Object> itemsOut = new ArrayList<>();
                            for(Object itemIn : (Collection)fieldIn.get(parentInstance)) {
                                itemsIn.add(itemIn);
                            }
                            for(Object obj : itemsIn) {
                                if(obj instanceof EditorElement) {
                                    EditorElement e = (EditorElement)obj;
                                    for(LineData line : e.linedata) {
                                        itemsOut.add(line);
                                    }
                                }
                            }
                            fieldOut.set(parentInstance, itemsOut);
                        }else{
                            System.out.println("Fields do not match type");
                            System.out.println("\t"+fieldInName+": "+fieldIn.getType());
                            System.out.println("\t"+fieldOutName+": "+fieldOut.getType());
                            return;
                        }
                    }else{
                        System.out.println(fieldOutName+" is not a collection");
                        return;
                    }
                }else{
                    System.out.println(fieldInName+" is not a collection");
                    return;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("PostIndex: "+System.currentTimeMillis());
        };
    }
    synchronized public void index() {
        indexThread = new Thread(indexThreadRunnable);
        System.out.println(indexThread.getState());
        if(indexThread.getState() == Thread.State.NEW)
            indexThread.start();
    }
}
