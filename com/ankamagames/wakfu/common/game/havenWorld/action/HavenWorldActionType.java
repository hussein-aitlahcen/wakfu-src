package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.exception.*;

public enum HavenWorldActionType implements SimpleObjectFactory<HavenWorldAction>
{
    TOPOLOGY_CREATE(1) {
        @Override
        public HavenWorldAction createNew() {
            return new TopologyCreate();
        }
    }, 
    TOPOLOGY_UPDATE(2) {
        @Override
        public HavenWorldAction createNew() {
            return new TopologyUpdate();
        }
    }, 
    BUILDING_CREATE(3) {
        @Override
        public HavenWorldAction createNew() {
            return new BuildingCreate();
        }
    }, 
    BUILDING_DELETE(4) {
        @Override
        public HavenWorldAction createNew() {
            return new BuildingDelete();
        }
    }, 
    BUILDING_EVOLUTION(5) {
        @Override
        public HavenWorldAction createNew() {
            return new BuildingEvolve();
        }
    }, 
    BUILDING_EQUIP(6) {
        @Override
        public HavenWorldAction createNew() {
            return new BuildingEquip();
        }
    }, 
    INTERACTIVE_ELEMENT_UPDATE(7) {
        @Override
        public HavenWorldAction createNew() {
            return new InteractiveElementUpdate();
        }
    }, 
    INTERACTIVE_ELEMENT_CREATE(8) {
        @Override
        public HavenWorldAction createNew() {
            return new InteractiveElementCreate();
        }
    }, 
    INTERACTIVE_ELEMENT_DELETE(9) {
        @Override
        public HavenWorldAction createNew() {
            return new InteractiveElementDelete();
        }
    }, 
    BUILDING_MOVE(10) {
        @Override
        public HavenWorldAction createNew() {
            return new BuildingMove();
        }
    };
    
    public final byte id;
    
    private HavenWorldActionType(final int idx) {
        this.id = MathHelper.ensureByte(idx);
    }
    
    public static HavenWorldActionType valueOf(final byte idx) {
        final HavenWorldActionType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final HavenWorldActionType value = values[i];
            if (value.id == idx) {
                return value;
            }
        }
        throw new HavenWorldRuntimeException("[HAVEN_WORLD] Action " + idx + " inconnue");
    }
}
