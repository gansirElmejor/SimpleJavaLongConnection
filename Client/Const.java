import java.awt.Color;

public final class Const{
    static final int WINDOW_SIZE = 800;
    static final int TOP = 0;
    static final int BOTTOM = WINDOW_SIZE;
    static final int LEFT = 0;
    static final int RIGHT = WINDOW_SIZE;
    static final int MIN_RADIUS = 15;
    static final int MAX_RADIUS = 67;
    static final int FRAME_DURATION = 15;    
    static final int STEP = 10;
    static final int SLEEPMICRO = 50;
    static final Color[] COLORS = {new Color(255,  0,  0), new Color(  0,255,  0), new Color(  0,  0,255),
                                   new Color(128,  0,  0), new Color(  0,128,  0), new Color(  0,  0,128),
                                   new Color(  0,255,255), new Color(255,  0,255), new Color(255,255,  0),
                                   new Color(  0,128,128), new Color(128,  0,128), new Color(128,128,  0),
                                   new Color(255,128,  0), new Color(  0,255,128), new Color(255,  0,128),
                                   new Color(128,255,  0), new Color(  0,128,255), new Color(128,  0,255)};
    
    private Const(){}
}
