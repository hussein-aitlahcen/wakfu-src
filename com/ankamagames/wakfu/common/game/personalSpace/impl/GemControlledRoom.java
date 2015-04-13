package com.ankamagames.wakfu.common.game.personalSpace.impl;

import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class GemControlledRoom extends Room
{
    public static final byte DEFAULT_WIDTH = 4;
    public static final byte DEFAULT_HEIGHT = 4;
    public static final byte EXTENDED_WIDTH = 5;
    public static final byte EXTENDED_HEIGHT = 5;
    public static final byte RIGHT_MERGED_WIDTH = 6;
    public static final byte BOTTOM_MERGED_HEIGHT = 6;
    public static final byte FULL_MERGE_EXTRA_CELL_X = 6;
    public static final byte FULL_MERGE_EXTRA_CELL_Y = 6;
    public static final byte ROOM_STEP_X = 6;
    public static final byte ROOM_STEP_Y = 6;
    public static final int ECOSYSTEM_ROOM_GROUND_TYPE = 28;
    protected boolean m_primaryGemLocked;
    protected Item m_primaryGem;
    protected Item m_secondaryGem;
    protected boolean m_extraCellUnlocked;
    
    public GemControlledRoom() {
        super();
        this.setWidth((byte)4);
        this.setHeight((byte)4);
        this.m_extraCellUnlocked = false;
        this.m_primaryGemLocked = false;
    }
    
    public void lockPrimaryGem() {
        if (this.m_primaryGem != null) {
            this.m_primaryGemLocked = true;
        }
    }
    
    public boolean isPrimaryGemLocked() {
        return this.m_primaryGemLocked;
    }
    
    public Item getGem(final boolean primary) {
        if (primary) {
            return this.m_primaryGem;
        }
        return this.m_secondaryGem;
    }
    
    private boolean isGem(final Item item) {
        return item != null && item.getReferenceItem() != null && GemType.getFromItemReferenceId(item.getReferenceId()) != null;
    }
    
    public ModResult putGem(final Item gem, final boolean primary, final boolean commit) {
        if (primary) {
            if (gem == null) {
                return ModResult.INVALID_ARGUMENT;
            }
            if (this.m_primaryGem != null) {
                return ModResult.GEM_SLOT_BUSY;
            }
            if (!this.isGem(gem)) {
                return ModResult.WRONG_ITEM_TYPE;
            }
            if (commit) {
                this.m_primaryGem = gem;
                this.update();
            }
            return ModResult.OK;
        }
        else {
            if (gem == null) {
                return ModResult.INVALID_ARGUMENT;
            }
            if (this.m_secondaryGem != null) {
                return ModResult.GEM_SLOT_BUSY;
            }
            if (!this.isGem(gem)) {
                return ModResult.WRONG_ITEM_TYPE;
            }
            if (this.m_primaryGem == null) {
                return ModResult.PRIMARY_DOESNT_EXIST;
            }
            if (GemType.getFromItemReferenceId(this.m_primaryGem.getReferenceId()) != GemType.getFromItemReferenceId(gem.getReferenceId())) {
                return ModResult.INCOMPATIBLE_GEM;
            }
            if (commit) {
                this.m_secondaryGem = gem;
                this.update();
            }
            return ModResult.OK;
        }
    }
    
    public ModResult removeGem(final boolean primary, final boolean commit) {
        if (primary) {
            if (this.m_primaryGemLocked) {
                return ModResult.PRIMARY_GEM_LOCKED;
            }
            if (this.m_secondaryGem != null) {
                return ModResult.SECONDARY_GEM_NOT_REMOVED;
            }
            if (this.m_primaryGem == null) {
                return ModResult.NO_GEM;
            }
            if (!this.isEmpty()) {
                return ModResult.ROOM_IS_NOT_EMPTY;
            }
            if (commit) {
                this.m_primaryGem = null;
                this.update();
            }
            return ModResult.OK;
        }
        else {
            if (this.m_secondaryGem == null) {
                return ModResult.NO_GEM;
            }
            final EnumSet<RoomContentType> types = this.getPresentContentTypes();
            for (final RoomContentType type : types) {
                if (this.getContentsOfType(type).size() > type.maxContentPerGem) {
                    return ModResult.TOO_MANY_CONTENT_IN_ROOM;
                }
            }
            ArrayList<RoomContent> contents = this.getContentsFromArea(4, 0, 2, 4);
            if (!contents.isEmpty()) {
                return ModResult.CONTENTS_IN_REMOVED_SPACE;
            }
            contents = this.getContentsFromArea(0, 4, 4, 2);
            if (!contents.isEmpty()) {
                return ModResult.CONTENTS_IN_REMOVED_SPACE;
            }
            contents = this.getContentsFromArea(4, 4, 2, 2);
            if (!contents.isEmpty()) {
                return ModResult.CONTENTS_IN_REMOVED_SPACE;
            }
            final PersonalSpaceLayout layout = this.getLayout();
            final Room topRoom = this.m_layout.getRoomFromUnit(this.m_originX, this.m_originY - 6);
            if (topRoom != null && topRoom instanceof GemControlledRoom) {
                final GemControlledRoom room = (GemControlledRoom)topRoom;
                if (room.m_height == 6 && !room.getContentsFromArea(0, 5, 6, 1).isEmpty()) {
                    return ModResult.CONTENTS_IN_REMOVED_SPACE;
                }
                if (room.m_extraCellUnlocked && room.getContentFromUnit((byte)6, (byte)6) != null) {
                    return ModResult.CONTENTS_IN_REMOVED_SPACE;
                }
            }
            final Room leftRoom = this.m_layout.getRoomFromUnit(this.m_originX - 6, this.m_originY);
            if (leftRoom != null && leftRoom instanceof GemControlledRoom) {
                final GemControlledRoom room2 = (GemControlledRoom)leftRoom;
                if (room2.m_width == 6 && !room2.getContentsFromArea(5, 0, 1, 6).isEmpty()) {
                    return ModResult.CONTENTS_IN_REMOVED_SPACE;
                }
                if (room2.m_extraCellUnlocked && room2.getContentFromUnit((byte)6, (byte)6) != null) {
                    return ModResult.CONTENTS_IN_REMOVED_SPACE;
                }
            }
            final Room topLeftRoom = this.m_layout.getRoomFromUnit(this.m_originX - 6, this.m_originY - 6);
            if (topLeftRoom instanceof GemControlledRoom) {
                final GemControlledRoom room3 = (GemControlledRoom)topLeftRoom;
                if (room3.m_height == 6 && room3.m_width == 6 && room3.getContentFromUnit((byte)5, (byte)5) != null) {
                    return ModResult.CONTENTS_IN_REMOVED_SPACE;
                }
                if (room3.m_extraCellUnlocked && room3.getContentFromUnit((byte)6, (byte)6) != null) {
                    return ModResult.CONTENTS_IN_REMOVED_SPACE;
                }
            }
            if (commit) {
                this.m_secondaryGem = null;
                this.update();
            }
            return ModResult.OK;
        }
    }
    
    public ModResult replaceGem(final boolean primary, final Item gem, final boolean commit) {
        if (primary) {
            if (this.m_primaryGemLocked) {
                return ModResult.PRIMARY_GEM_LOCKED;
            }
            if (commit) {
                this.m_primaryGem = gem;
                this.update();
            }
        }
        else {
            if (GemType.getFromItemReferenceId(this.m_primaryGem.getReferenceId()) != GemType.getFromItemReferenceId(gem.getReferenceId())) {
                return ModResult.INCOMPATIBLE_GEM;
            }
            if (commit) {
                this.m_secondaryGem = gem;
                this.update();
            }
        }
        return ModResult.OK;
    }
    
    @Override
    public boolean canPutContentAt(final RoomContent content, final int x, final int y) {
        if (this.m_primaryGem == null) {
            return false;
        }
        final GemType primaryGemType = GemType.getFromItemReferenceId(this.m_primaryGem.getReferenceId());
        boolean ok = false;
        for (final GemType allowedRoom : content.getAllowedInRooms()) {
            if (allowedRoom == primaryGemType) {
                ok = true;
                break;
            }
        }
        return (this.getContents().contains(content) || !this.maxContentReached(content)) && (!ok || content.canBeAddedIn(this)) && ok && super.canPutContentAt(content, x, y);
    }
    
    public boolean maxContentReached(final RoomContent content) {
        final RoomContentType contentType = content.getContentType();
        final ArrayList<RoomContent> contents = this.getContentsOfType(contentType);
        final int maxContent = contentType.maxContentPerGem * this.getNumGem();
        return contents.size() >= maxContent;
    }
    
    private int getNumGem() {
        int num = 0;
        if (this.m_primaryGem != null) {
            ++num;
        }
        if (this.m_secondaryGem != null) {
            ++num;
        }
        return num;
    }
    
    public boolean isExtraCellUnlocked() {
        return this.m_extraCellUnlocked;
    }
    
    private boolean canMergeWith(final Room room) {
        if (room == null) {
            return false;
        }
        if (!(room instanceof GemControlledRoom)) {
            return false;
        }
        final GemControlledRoom altRoom = (GemControlledRoom)room;
        return altRoom.m_primaryGem != null && altRoom.m_secondaryGem != null && GemType.getFromItemReferenceId(this.m_primaryGem.getReferenceId()) == GemType.getFromItemReferenceId(altRoom.m_primaryGem.getReferenceId()) && GemType.getFromItemReferenceId(this.m_secondaryGem.getReferenceId()) == GemType.getFromItemReferenceId(altRoom.m_secondaryGem.getReferenceId());
    }
    
    private void recomputeMerge() {
        if (this.m_primaryGem != null && this.m_secondaryGem != null && GemType.getFromItemReferenceId(this.m_primaryGem.getReferenceId()) == GemType.getFromItemReferenceId(this.m_secondaryGem.getReferenceId())) {
            final Room bottom = this.m_layout.getRoomFromUnit(this.m_originX, (short)(this.m_originY + 6));
            this.m_height = (byte)(this.canMergeWith(bottom) ? 6 : 5);
            final Room right = this.m_layout.getRoomFromUnit((short)(this.m_originX + 6), this.m_originY);
            this.m_width = (byte)(this.canMergeWith(right) ? 6 : 5);
            final Room diag = this.m_layout.getRoomFromUnit((short)(this.m_originX + 6), (short)(this.m_originY + 6));
            this.m_extraCellUnlocked = (this.m_width == 6 && this.m_height == 6 && this.canMergeWith(diag));
        }
        else {
            this.m_width = 4;
            this.m_height = 4;
        }
    }
    
    @Override
    public boolean update() {
        try {
            this.recomputeMerge();
            final Room top = this.m_layout.getRoomFromUnit(this.m_originX, (short)(this.m_originY - 6));
            if (top != null && top instanceof GemControlledRoom) {
                ((GemControlledRoom)top).recomputeMerge();
            }
            final Room left = this.m_layout.getRoomFromUnit((short)(this.m_originX - 6), this.m_originY);
            if (left != null && left instanceof GemControlledRoom) {
                ((GemControlledRoom)left).recomputeMerge();
            }
            final Room topLeft = this.m_layout.getRoomFromUnit((short)(this.m_originX - 6), (short)(this.m_originY - 6));
            if (topLeft != null && topLeft instanceof GemControlledRoom) {
                ((GemControlledRoom)topLeft).recomputeMerge();
            }
            return true;
        }
        catch (Exception e) {
            GemControlledRoom.m_logger.error((Object)"Erreur lors de l'Update des room d'un Havre-sac", (Throwable)e);
            return false;
        }
    }
    
    @Override
    protected RawSpecificRooms getSpecificData() {
        final RawGemControlledRoom data = new RawGemControlledRoom();
        data.primaryGemLocked = this.m_primaryGemLocked;
        data.primaryGemitemRefId = ((this.m_primaryGem != null) ? this.m_primaryGem.getReferenceId() : -1);
        data.primaryGemUniqueId = ((this.m_primaryGem != null) ? this.m_primaryGem.getUniqueId() : -1L);
        data.secondaryGemitemRefId = ((this.m_secondaryGem != null) ? this.m_secondaryGem.getReferenceId() : -1);
        data.secondaryGemUniqueId = ((this.m_secondaryGem != null) ? this.m_secondaryGem.getUniqueId() : -1L);
        return data;
    }
    
    private Item createNewGem(final int refId) {
        final AbstractReferenceItem primaryRefItem = ReferenceItemManager.getInstance().getReferenceItem(refId);
        if (primaryRefItem != null) {
            final Item gem = Item.newInstance(primaryRefItem);
            gem.setQuantity((short)1);
            return gem;
        }
        return null;
    }
    
    @Override
    protected void setSpecificData(final RawSpecificRooms data) {
        final RawGemControlledRoom rawRoomSpecData = (RawGemControlledRoom)data;
        this.m_primaryGemLocked = rawRoomSpecData.primaryGemLocked;
        this.release();
        if (rawRoomSpecData.primaryGemitemRefId != -1) {
            final AbstractReferenceItem primaryRefItem = ReferenceItemManager.getInstance().getReferenceItem(rawRoomSpecData.primaryGemitemRefId);
            final Item item = Item.newInstance(rawRoomSpecData.primaryGemUniqueId, primaryRefItem);
            if (!this.isGem(item)) {
                GemControlledRoom.m_logger.error((Object)("La gemme primaire n'est pas un item de la bonne cat\u00e9gorie (refItemId=" + rawRoomSpecData.primaryGemitemRefId + "), on la remplace par la gemme par d\u00e9faut"));
                item.release();
                this.m_primaryGem = this.createNewGem(GemType.getDefaultGemType().getItemReferenceId());
            }
            else {
                (this.m_primaryGem = item).setQuantity((short)1);
            }
        }
        else {
            this.m_primaryGem = null;
        }
        if (rawRoomSpecData.secondaryGemitemRefId != -1) {
            if (rawRoomSpecData.secondaryGemitemRefId != rawRoomSpecData.primaryGemitemRefId) {
                GemControlledRoom.m_logger.error((Object)"Le type de gemme secondaire n'est pas le meme que le type primaire, on r\u00e9initialise cette gemme");
                rawRoomSpecData.secondaryGemitemRefId = rawRoomSpecData.primaryGemitemRefId;
            }
            final AbstractReferenceItem secondaryRefItem = ReferenceItemManager.getInstance().getReferenceItem(rawRoomSpecData.secondaryGemitemRefId);
            final Item item = Item.newInstance(rawRoomSpecData.secondaryGemUniqueId, secondaryRefItem);
            if (!this.isGem(item)) {
                GemControlledRoom.m_logger.error((Object)("La gemme secondaire n'est pas un item de la bonne cat\u00e9gorie (refItemId=" + rawRoomSpecData.secondaryGemitemRefId + "), on la remplace par la gemme par d\u00e9faut"));
                item.release();
                this.m_secondaryGem = this.createNewGem(this.m_primaryGem.getReferenceId());
            }
            else {
                (this.m_secondaryGem = item).setQuantity((short)1);
            }
        }
        else {
            this.m_secondaryGem = null;
        }
    }
    
    @Override
    public boolean unitWithinBounds(final int x, final int y) {
        return x >= this.m_originX && x < this.m_originX + this.m_width && y >= this.m_originY && y < this.m_originY + this.m_height && (x != this.m_originX + this.m_width - 1 || y != this.m_originY + this.m_height - 1 || this.m_width != 6 || this.m_height != 6 || this.m_extraCellUnlocked);
    }
    
    public GemType getPrimaryGemType() {
        if (this.m_primaryGem == null) {
            return null;
        }
        return GemType.getFromItemReferenceId(this.m_primaryGem.getReferenceId());
    }
    
    public GemType getSecondaryGemType() {
        if (this.m_secondaryGem == null) {
            return null;
        }
        return GemType.getFromItemReferenceId(this.m_secondaryGem.getReferenceId());
    }
    
    public void release() {
        if (this.m_primaryGem != null) {
            this.m_primaryGem.release();
            this.m_primaryGem = null;
        }
        if (this.m_secondaryGem != null) {
            this.m_secondaryGem.release();
            this.m_secondaryGem = null;
        }
    }
    
    public enum ModResult
    {
        OK, 
        GEM_SLOT_BUSY, 
        WRONG_ITEM_TYPE, 
        NO_GEM, 
        SECONDARY_GEM_NOT_REMOVED, 
        PRIMARY_DOESNT_EXIST, 
        ROOM_IS_NOT_EMPTY, 
        PRIMARY_GEM_LOCKED, 
        CONTENTS_IN_REMOVED_SPACE, 
        INVALID_ARGUMENT, 
        INCOMPATIBLE_GEM, 
        TOO_MANY_CONTENT_IN_ROOM;
    }
}
