package net.ncguy.uml.drawable;

/**
 * Created by Nick on 01/10/2015 at 16:36,
 * Project: UMLEditor.
 */
public enum Icons {
    NEW("new"),
    UNDO("undo"),
    REDO("redo"),
    SETTINGS("settings"),
    SETTINGS_VIEW("settings-view"),
    EXPORT("export"),
    IMPORT("import"),
    LOAD("load"),
    SAVE("save"),
    GLOBE("globe"),
    INFO("info"),
    EXIT("exit"),
    FOLDER_OPEN("folder-open"),
    SEARCH("search"),
    QUESTION("question-big"),
    MORE("more"),
    SOUND("sound-big"),
    MUSIC("music-big"),
    WARNING("warning"),
    LAYER_ADD("layer-add"),
    LAYER_REMOVE("layer-remove"),
    LAYER_UP("layer-up"),
    LAYER_DOWN("layer-down"),
    EYE("eye"),
    EYE_DISABLED("eye-disabled"),
    LOCKED("locked"),
    UNLOCKED("unlocked"),
    ALIGN_LEFT("align-left"),
    ALIGN_RIGHT("align-right"),
    ALIGN_BOTTOM("align-bottom"),
    ALIGN_TOP("align-top"),
    ALIGN_CENTER_X("align-center-x"),
    ALIGN_CENTER_Y("align-center-y"),
    CURSOR("cursor"),
    POLYGON2("polygon2"),
    PLUS("plus"),
    POINT("point-big"),
    // misc
    GRID20("alpha-grid-20x20"),
    GRID32("alpha-grid-32x32"),
    BALL("ball"),
    BREADCRUMB("breadcrumb-arrow"),
    INDETERMINATE("check-indeterminate"),
    OVERINDETERMINATE("check-over-indeterminate"),
    POLYGON("polygon"),
    POLYGONDOWN("polygon-down"),
    POLYGONOVER("polygon-over"),
    ;

    Icons(String iconName) {
        this.iconName = iconName;
    }

    String iconName;

    public String getIconName() {
        return iconName;
    }

}
