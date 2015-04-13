package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import org.jetbrains.annotations.*;

public class SpellInventory<S extends AbstractSpellLevel> extends StackInventory<S, RawSpellLevel> implements RawConvertible<RawSpellLevelInventory>
{
    private static final Logger m_logger;
    private short m_version;
    
    public SpellInventory(final short maximumSize, final InventoryContentProvider<S, RawSpellLevel> contentProvider, final InventoryContentChecker<S> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(maximumSize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public boolean toRaw(final RawSpellLevelInventory raw) {
        if (this.m_serializeQuantity) {
            SpellInventory.m_logger.warn((Object)"Impossible d'ajouter l'information de quantit\u00e9 \u00e0 un RawSpellLevelInventory qui n'est pas pr\u00e9vu pour");
        }
        raw.clear();
        for (final S spellLevel : this) {
            if (spellLevel.shouldBeSerialized()) {
                final RawSpellLevelInventory.Content content = new RawSpellLevelInventory.Content();
                if (!spellLevel.toRaw(content.spellLevel)) {
                    return false;
                }
                raw.contents.add(content);
            }
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawSpellLevelInventory raw) {
        this.destroyAll();
        if (this.m_serializeQuantity) {
            SpellInventory.m_logger.warn((Object)"Impossible d'ajouter les quantit\u00e9s depuis un RawStackInventory qui ne connait pas cette information");
        }
        boolean bOk = true;
        S spell = null;
        for (final RawSpellLevelInventory.Content content : raw.contents) {
            try {
                spell = (S)this.m_contentProvider.unSerializeContent((R)content.spellLevel);
                if (spell != null) {
                    if (this.add(spell)) {
                        continue;
                    }
                    bOk = false;
                    SpellInventory.m_logger.error((Object)("Impossible d'ajouter un sort (" + spell.getReferenceId() + ") au SpellInventory"));
                    spell.release();
                }
                else {
                    bOk = false;
                }
            }
            catch (InventoryCapacityReachedException e) {
                SpellInventory.m_logger.error((Object)ExceptionFormatter.toString(e));
                bOk = false;
                spell.release();
            }
            catch (ContentAlreadyPresentException e2) {
                SpellInventory.m_logger.error((Object)ExceptionFormatter.toString(e2));
                bOk = false;
                spell.release();
            }
        }
        return bOk;
    }
    
    public short getVersion() {
        return this.m_version;
    }
    
    public void setVersion(final short version) {
        this.m_version = version;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId) {
        return 0;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId, final int count) {
        return 0;
    }
    
    @Nullable
    public S getFirstSpell() {
        final Iterator i$ = this.m_contents.values().iterator();
        if (i$.hasNext()) {
            final S spell = i$.next();
            return spell;
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellInventory.class);
    }
}
