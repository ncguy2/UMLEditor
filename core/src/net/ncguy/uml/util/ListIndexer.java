package net.ncguy.uml.util;

import net.ncguy.uml.UMLLauncher;
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

    public static Thread indexThread;

    public static void start(Object parentInstance, String fieldInName, String fieldOutName) {
        indexThread = new Thread(() -> {
            try {
                Class c = Class.forName(parentInstance.getClass().getCanonicalName());
                System.out.println("Fields in " + c.getCanonicalName());
                Field fieldIn = c.getField(fieldInName);
                Field fieldOut = c.getField(fieldOutName);
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
        });
        indexThread.start();
    }

}
