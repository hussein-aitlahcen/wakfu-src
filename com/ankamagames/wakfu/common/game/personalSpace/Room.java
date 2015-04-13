package com.ankamagames.wakfu.common.game.personalSpace;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;

public abstract class Room
{
    protected static final Logger m_logger;
    protected byte m_layoutPosition;
    protected short m_originX;
    protected short m_originY;
    protected byte m_width;
    protected byte m_height;
    protected List<RoomContent> m_contents;
    protected PersonalSpaceLayout m_layout;
    
    protected Room() {
        super();
        this.m_contents = new ArrayList<RoomContent>();
        this.m_layoutPosition = 0;
        this.m_originX = 0;
        this.m_originY = 0;
        this.m_width = 1;
        this.m_height = 1;
    }
    
    public final PersonalSpaceLayout getLayout() {
        return this.m_layout;
    }
    
    public final void setLayout(final PersonalSpaceLayout layout) {
        this.m_layout = layout;
    }
    
    public final void setLayoutPosition(final byte layoutPosition) {
        this.m_layoutPosition = layoutPosition;
    }
    
    public final byte getLayoutPosition() {
        return this.m_layoutPosition;
    }
    
    public boolean unitWithinBounds(final int x, final int y) {
        return x >= this.m_originX && x < this.m_originX + this.m_width && y >= this.m_originY && y < this.m_originY + this.m_height;
    }
    
    public final short getOriginX() {
        return this.m_originX;
    }
    
    public final void setOriginX(final short originX) {
        this.m_originX = originX;
    }
    
    public final short getOriginY() {
        return this.m_originY;
    }
    
    public final void setOriginY(final short originY) {
        this.m_originY = originY;
    }
    
    public final byte getWidth() {
        return this.m_width;
    }
    
    public final void setWidth(final byte width) {
        this.m_width = width;
    }
    
    public final byte getHeight() {
        return this.m_height;
    }
    
    public final void setHeight(final byte height) {
        this.m_height = height;
    }
    
    public final boolean toRaw(final RawRoom raw, final boolean putContent) {
        raw.clear();
        raw.layoutPosition = this.m_layoutPosition;
        if (putContent) {
            for (final RoomContent element : this.m_contents) {
                try {
                    final RawRoom.ContainedInteractiveElement rawIe = new RawRoom.ContainedInteractiveElement();
                    element.toRawPersistantData(rawIe.persistantData);
                    raw.interactiveElements.add(rawIe);
                }
                catch (Exception e) {
                    Room.m_logger.error((Object)("Erreur lors de la serialisation d'un room content ie=" + element), (Throwable)e);
                }
            }
        }
        raw.roomSpecificData = this.getSpecificData();
        return true;
    }
    
    public final boolean fromRaw(final RawRoom rawRoom, final boolean extractRoomContents) {
        this.m_layoutPosition = rawRoom.layoutPosition;
        if (!extractRoomContents) {
            this.setSpecificData(rawRoom.roomSpecificData);
            return true;
        }
        final PersonalSpace space = this.m_layout.getPersonalSpace();
        for (final RawRoom.ContainedInteractiveElement interactiveElement : rawRoom.interactiveElements) {
            try {
                final RoomContent content = space.getRoomContentFactory().createContent(space, interactiveElement.persistantData);
                if (content == null) {
                    continue;
                }
                space.putRoomContent(content);
            }
            catch (Exception e) {
                Room.m_logger.error((Object)("Erreur durant la r\u00e9cup\u00e9ration du roomContent " + interactiveElement + " d'une pi\u00e8ce du sac " + rawRoom), (Throwable)e);
            }
        }
        return true;
    }
    
    public boolean canPutContentAt(final RoomContent content, final int x, final int y) {
        return x >= this.m_originX && x < this.m_originX + this.m_width && y >= this.m_originY && y < this.m_originY + this.m_height && this.getContentFromWorldPos(x, y) == null;
    }
    
    protected final boolean putContent(final RoomContent content) {
        if (this.canPutContentAt(content, content.getWorldCellX(), content.getWorldCellY())) {
            this.m_contents.add(content);
            return true;
        }
        return false;
    }
    
    protected final boolean removeContent(final RoomContent content) {
        return this.m_contents.remove(content);
    }
    
    public final RoomContent getContentFromUnit(final byte x, final byte y) {
        final int wx = this.m_originX + x;
        final int wy = this.m_originY + y;
        return this.getContentFromWorldPos(wx, wy);
    }
    
    public List<RoomContent> getContents() {
        return this.m_contents;
    }
    
    public RoomContent getContentFromWorldPos(final int wx, final int wy) {
        for (int count = this.m_contents.size(), i = 0; i < count; ++i) {
            final RoomContent content = this.m_contents.get(i);
            if (content.getWorldCellX() == wx && content.getWorldCellY() == wy) {
                return content;
            }
        }
        return null;
    }
    
    public final ArrayList<RoomContent> getContentsFromArea(final int x, final int y, final int w, final int h) {
        final ArrayList<RoomContent> contents = new ArrayList<RoomContent>();
        final int wx = this.m_originX + x;
        final int wy = this.m_originY + y;
        final int ww = wx + w;
        final int wh = wy + h;
        for (int count = this.m_contents.size(), i = 0; i < count; ++i) {
            final RoomContent content = this.m_contents.get(i);
            final int cx = content.getWorldCellX();
            final int cy = content.getWorldCellY();
            if (cx >= wx && cx < ww && cy >= wy && cy < wh) {
                contents.add(content);
            }
        }
        return contents;
    }
    
    public final ArrayList<RoomContent> getContentsOfType(final RoomContentType type) {
        final ArrayList<RoomContent> contents = new ArrayList<RoomContent>();
        for (int count = this.m_contents.size(), i = 0; i < count; ++i) {
            final RoomContent content = this.m_contents.get(i);
            if (content.getContentType() == type) {
                contents.add(content);
            }
        }
        return contents;
    }
    
    public final EnumSet<RoomContentType> getPresentContentTypes() {
        final EnumSet<RoomContentType> types = EnumSet.noneOf(RoomContentType.class);
        for (int count = this.m_contents.size(), i = 0; i < count; ++i) {
            types.add(this.m_contents.get(i).getContentType());
        }
        return types;
    }
    
    public final boolean isEmpty() {
        return this.m_contents.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Room : x=" + this.m_originX + ", y=" + this.m_originY + ", width=" + this.m_width + ", height=" + this.m_height;
    }
    
    public abstract boolean update();
    
    protected abstract RawSpecificRooms getSpecificData();
    
    protected abstract void setSpecificData(final RawSpecificRooms p0);
    
    static {
        m_logger = Logger.getLogger((Class)Room.class);
    }
}
