package com.ankamagames.wakfu.client.ui.protocol.message.item;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UIItemMessage extends UIMessage
{
    private Item m_item;
    private short m_position;
    private byte m_sourcePosition;
    private byte m_destinationPosition;
    private long m_sourceUniqueId;
    private long m_destinationUniqueId;
    private short m_quantity;
    private short m_x;
    private short m_y;
    private CharacterView m_sourceCharacter;
    private CharacterView m_destinationCharacter;
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    public short getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final short position) {
        this.m_position = position;
    }
    
    public byte getSourcePosition() {
        return this.m_sourcePosition;
    }
    
    public void setSourcePosition(final byte sourcePosition) {
        this.m_sourcePosition = sourcePosition;
    }
    
    public byte getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    public void setDestinationPosition(final byte destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
    
    public long getSourceUniqueId() {
        return this.m_sourceUniqueId;
    }
    
    public void setSourceUniqueId(final long sourceUniqueId) {
        this.m_sourceUniqueId = sourceUniqueId;
    }
    
    public long getDestinationUniqueId() {
        return this.m_destinationUniqueId;
    }
    
    public void setDestinationUniqueId(final long destinationUniqueId) {
        this.m_destinationUniqueId = destinationUniqueId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    public short getX() {
        return this.m_x;
    }
    
    public void setX(final short x) {
        this.m_x = x;
    }
    
    public short getY() {
        return this.m_y;
    }
    
    public void setY(final short y) {
        this.m_y = y;
    }
    
    public CharacterView getSourceCharacter() {
        return this.m_sourceCharacter;
    }
    
    public void setSourceCharacter(final CharacterView sourceCharacter) {
        this.m_sourceCharacter = sourceCharacter;
    }
    
    public CharacterView getDestinationCharacter() {
        return this.m_destinationCharacter;
    }
    
    public void setDestinationCharacter(final CharacterView destinationCharacter) {
        this.m_destinationCharacter = destinationCharacter;
    }
}
