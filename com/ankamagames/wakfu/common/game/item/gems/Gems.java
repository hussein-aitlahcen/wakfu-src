package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

public class Gems implements RawConvertible<RawGems>, LoggableEntity
{
    private static final Logger m_logger;
    public static final Gems EMPTY;
    private final AbstractReferenceItem m_holderDefintion;
    private int[] m_gems;
    
    public Gems(final AbstractReferenceItem holderDefinition) {
        super();
        this.m_holderDefintion = holderDefinition;
        this.m_gems = new int[(this.m_holderDefintion == null) ? 0 : this.m_holderDefintion.getGemsNum()];
    }
    
    public Gems(final AbstractReferenceItem holderDefinition, final Gems gemsHandler) {
        super();
        this.m_holderDefintion = holderDefinition;
        final int gemsCount = gemsHandler.m_gems.length;
        this.m_gems = new int[gemsCount];
        for (int i = 0; i < gemsCount; ++i) {
            this.m_gems[i] = gemsHandler.m_gems[i];
        }
    }
    
    public boolean hasGems() {
        for (byte index = 0, size = (byte)this.m_gems.length; index < size; ++index) {
            if (this.hasGem(index)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasGem(final byte index) {
        return this.m_gems[index] > 0;
    }
    
    public boolean isEditable() {
        return true;
    }
    
    public boolean canGem(final AbstractReferenceItem item, final byte index) {
        return item.getGemElementType() == GemElementType.GEM && item.getLevel() >= this.m_holderDefintion.getLevel() && index >= 0 && index < this.m_gems.length;
    }
    
    public void equipGem(final AbstractReferenceItem item, final byte index) throws GemsException {
        if (index < 0 || index >= this.m_gems.length) {
            throw new GemsException("On essaye d'\u00e9quiper une gemme alors qu'il n'y a plus d'emplacements disponibles");
        }
        this.m_gems[index] = item.getId();
    }
    
    public int removeGem(final byte index) throws GemsException {
        if (!this.hasGem(index)) {
            throw new GemsException("tentative de retrait d'une gemme d'un slot vide");
        }
        final int gemReferenceId = this.m_gems[index];
        this.m_gems[index] = 0;
        return gemReferenceId;
    }
    
    public byte getSlotCount() {
        return (byte)((this.m_holderDefintion == null) ? 0 : this.m_holderDefintion.getGemsNum());
    }
    
    public int getGem(final int index) {
        return this.m_gems[index];
    }
    
    public GemType getGemType() {
        return this.m_holderDefintion.getGemType();
    }
    
    public int removeLastGem() {
        for (int i = this.m_gems.length - 1; i >= 0; --i) {
            final int gem = this.m_gems[i];
            if (gem != 0) {
                this.removeGem((byte)i);
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean toRaw(final RawGems rawGems) {
        for (int i = 0, size = this.m_gems.length; i < size; ++i) {
            final RawGems.Content content = new RawGems.Content();
            content.position = (byte)i;
            content.referenceId = this.m_gems[i];
            rawGems.gems.add(content);
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawGems raw) {
        this.m_gems = new int[this.getSlotCount()];
        for (final RawGems.Content rawContent : raw.gems) {
            final byte position = rawContent.position;
            final int referenceId = rawContent.referenceId;
            if (referenceId != 0) {
                final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(referenceId);
                if (refItem == null) {
                    Gems.m_logger.warn((Object)("Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation, on essaye de placer une gemme inconnue d'id : " + referenceId), (Throwable)new Exception());
                    continue;
                }
            }
            if (position < 0 || position >= this.m_gems.length) {
                Gems.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation, on essaye de placer une gemme \u00e0 un emplacement indisponible", (Throwable)new Exception());
            }
            else {
                this.m_gems[position] = referenceId;
            }
        }
        return true;
    }
    
    @Override
    public String getLogRepresentation() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("gems(");
        for (int i = 0; i < this.m_gems.length; ++i) {
            buffer.append(this.m_gems[i]);
            if (i < this.m_gems.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Gems.class);
        EMPTY = new ImmutableGems(null);
    }
}
