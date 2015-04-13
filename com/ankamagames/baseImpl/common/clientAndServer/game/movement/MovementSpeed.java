package com.ankamagames.baseImpl.common.clientAndServer.game.movement;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;

public enum MovementSpeed implements ExportableEnum
{
    NORMAL_RUN_SPEED(1, 300, "vitesse de course normale", "300ms/cell, 7.5 frames \u00e0 25fps"), 
    NORMAL_WALK_SPEED(2, 600, "vitesse de marche normale", "600ms/cell, 15 frames \u00e0 25fps"), 
    SLOW_WALK_SPEED(3, 1000, "vitesse de marche lente", "1000ms/cell, 25 frames \u00e0 25fps"), 
    VERY_SLOW_WALK_SPEED(4, 1200, "vitesse de marche tr\u00e8s lente", "1200ms/cell, 30 frames \u00e0 25fps"), 
    MOUNT_SPEED1(5, 240, "Monture +25%", "240ms/cell"), 
    MOUNT_SPEED2(6, 200, "Monture +50%", "200ms/cell"), 
    MOUNT_SPEED3(7, 172, "Monture +75%", "172ms/cell");
    
    private static final IntObjectLightWeightMap<MovementSpeed> m_speedsById;
    private final int m_id;
    private final String m_label;
    private final String m_comment;
    private final int m_timeBetweenCells;
    
    private MovementSpeed(final int id, final int timeBetweenCells, final String label, final String comment) {
        this.m_id = id;
        this.m_timeBetweenCells = timeBetweenCells;
        this.m_label = label;
        this.m_comment = comment;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return this.m_comment;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getTimeBetweenCells() {
        return this.m_timeBetweenCells;
    }
    
    public static MovementSpeed getFromId(final int id) {
        return MovementSpeed.m_speedsById.get(id);
    }
    
    static {
        m_speedsById = new IntObjectLightWeightMap<MovementSpeed>();
        for (final MovementSpeed speed : values()) {
            MovementSpeed.m_speedsById.put(speed.getId(), speed);
        }
    }
}
