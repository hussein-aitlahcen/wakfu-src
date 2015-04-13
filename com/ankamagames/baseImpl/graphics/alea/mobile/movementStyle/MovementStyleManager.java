package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

import java.util.*;

public class MovementStyleManager
{
    public static String WALK_STYLE;
    public static String RUN_STYLE;
    public static String SLIDE_STYLE;
    public static String SWIMM_STYLE;
    public static String WALK_CARRY_STYLE;
    public static String THROW_STYLE;
    public static String CUSTOM_STYLE;
    private static final MovementStyleManager m_instance;
    private final HashMap<String, PathMovementFactory> m_styles;
    
    public static MovementStyleManager getInstance() {
        return MovementStyleManager.m_instance;
    }
    
    private MovementStyleManager() {
        super();
        this.m_styles = new HashMap<String, PathMovementFactory>();
        this.registerStyleFactory(MovementStyleManager.WALK_STYLE, new PathMovementFactory() {
            @Override
            public PathMovementStyle create() {
                return WalkMovementStyle.getInstance();
            }
        });
        this.registerStyleFactory(MovementStyleManager.RUN_STYLE, new PathMovementFactory() {
            @Override
            public PathMovementStyle create() {
                return RunMovementStyle.getInstance();
            }
        });
        this.registerStyleFactory(MovementStyleManager.SLIDE_STYLE, new PathMovementFactory() {
            @Override
            public PathMovementStyle create() {
                return SlideMovementStyle.getInstance();
            }
        });
        this.registerStyleFactory(MovementStyleManager.WALK_CARRY_STYLE, new PathMovementFactory() {
            @Override
            public PathMovementStyle create() {
                return WalkCarryMovementStyle.getInstance();
            }
        });
        this.registerStyleFactory(MovementStyleManager.THROW_STYLE, new PathMovementFactory() {
            @Override
            public PathMovementStyle create() {
                return new ThrowMovementStyle();
            }
        });
        this.registerStyleFactory(MovementStyleManager.CUSTOM_STYLE, new PathMovementFactory() {
            @Override
            public PathMovementStyle create() {
                return new CustomWalkMovementStyle();
            }
        });
    }
    
    public void registerStyleFactory(final String key, final PathMovementFactory style) {
        this.m_styles.put(key, style);
    }
    
    public PathMovementStyle getMovementStyle(final String key) {
        final PathMovementFactory factory = this.m_styles.get(key);
        if (factory == null) {
            return null;
        }
        return factory.create();
    }
    
    static {
        MovementStyleManager.WALK_STYLE = "WALK";
        MovementStyleManager.RUN_STYLE = "RUN";
        MovementStyleManager.SLIDE_STYLE = "SLIDE";
        MovementStyleManager.SWIMM_STYLE = "SWIM";
        MovementStyleManager.WALK_CARRY_STYLE = "WALK_CARRY";
        MovementStyleManager.THROW_STYLE = "THROW";
        MovementStyleManager.CUSTOM_STYLE = "CUSTOM_WALK";
        m_instance = new MovementStyleManager();
    }
}
