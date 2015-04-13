package com.ankamagames.wakfu.client.core.game.actor;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.shortKey.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.wakfu.common.game.item.*;

public enum EquipmentType
{
    HEAD((short)EquipmentPosition.HEAD.m_id, new String[] { "Chapeau", "CheveuxHaut" }), 
    HAIR((short)EquipmentPosition.HAIR.m_id, new String[] { "Cheveux", "Natte" }), 
    FACE((short)EquipmentPosition.FACE.m_id, new String[] { "Barbe" }), 
    SHOULDERS((short)EquipmentPosition.SHOULDERS.m_id, new String[] { "Epaulette-G", "Epaulette-D" }), 
    CHEST((short)EquipmentPosition.CHEST.m_id, new String[] { "CorpsHabit", "Jupe", "CuisseHabit" }), 
    TROUSERS((short)EquipmentPosition.TROUSERS.m_id, new String[] { "BassinPeau" }), 
    LEGS((short)EquipmentPosition.LEGS.m_id, new String[] { "JambeHabit", "PiedHabit01", "PiedHabit02", "CuisseHabit" }), 
    BACK((short)EquipmentPosition.BACK.m_id, new String[] { "Cape", "CapeBas" }), 
    FIRST_WEAPON((short)EquipmentPosition.FIRST_WEAPON.m_id, new String[] { "Arme" }), 
    SECOND_WEAPON((short)EquipmentPosition.SECOND_WEAPON.m_id, new String[] { "Bouclier" }), 
    ACCESSORY((short)EquipmentPosition.ACCESSORY.m_id, new String[] { "Accessoire", "Accessoire-0", "Accessoire-1", "Accessoire-2" });
    
    private static final Logger m_logger;
    private static final ShortObjectLightWeightMap<EquipmentType> EQUIPMENT_SORTED_BY_POSITION;
    private static final IntLightWeightSet NO_SLOT_EQUIPMENT;
    private final short m_position;
    public final String[] m_linkageNames;
    public final int[] m_linkageCrc;
    
    private EquipmentType(final short position, final String[] linkageNames) {
        this.m_position = position;
        this.m_linkageNames = linkageNames;
        this.m_linkageCrc = Engine.getPartsNames(linkageNames);
    }
    
    public static EquipmentType getActorEquipmentTypeFromPosition(final short position) {
        final EquipmentType equipmentType = EquipmentType.EQUIPMENT_SORTED_BY_POSITION.get(position);
        if (equipmentType != null) {
            return equipmentType;
        }
        if (!EquipmentType.NO_SLOT_EQUIPMENT.contains(position)) {
            EquipmentType.m_logger.warn((Object)("la position " + position + "ne possede pas de slot et ce n'est pas un equipment connu pour cela"));
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EquipmentType.class);
        EQUIPMENT_SORTED_BY_POSITION = new ShortObjectLightWeightMap<EquipmentType>();
        for (final EquipmentType e : values()) {
            assert !EquipmentType.EQUIPMENT_SORTED_BY_POSITION.contains(e.m_position);
            EquipmentType.EQUIPMENT_SORTED_BY_POSITION.put(e.m_position, e);
        }
        (NO_SLOT_EQUIPMENT = new IntLightWeightSet()).add(EquipmentPosition.RIGHT_HAND.m_id);
        EquipmentType.NO_SLOT_EQUIPMENT.add(EquipmentPosition.LEFT_HAND.m_id);
        EquipmentType.NO_SLOT_EQUIPMENT.add(EquipmentPosition.COSTUME.m_id);
        EquipmentType.NO_SLOT_EQUIPMENT.add(EquipmentPosition.PET.m_id);
        EquipmentType.NO_SLOT_EQUIPMENT.add(EquipmentPosition.MOUNT.m_id);
        EquipmentType.NO_SLOT_EQUIPMENT.add(EquipmentPosition.BELT.m_id);
        EquipmentType.NO_SLOT_EQUIPMENT.add(EquipmentPosition.NECK.m_id);
    }
}
