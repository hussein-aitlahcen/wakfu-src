package com.ankamagames.wakfu.common.game.shortcut;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import gnu.trove.*;

public class ShortcutInventory<S extends AbstractShortCutItem> extends ArrayInventory<S, RawShortcut> implements RawConvertible<RawShortcutInventory>
{
    private static final Logger m_logger;
    private ShortCutBarType m_type;
    
    public ShortcutInventory(final ShortCutBarType type, final InventoryContentProvider<S, RawShortcut> contentProvider, final InventoryContentChecker<S> contentChecker, final short maximumSize, final boolean stackable) {
        super(contentProvider, contentChecker, maximumSize, stackable);
        this.m_type = ShortCutBarType.WORLD;
        this.m_type = type;
    }
    
    @Override
    public boolean toRaw(final RawShortcutInventory raw) {
        raw.clear();
        raw.type = (byte)this.m_type.ordinal();
        final TLongShortIterator it = this.m_idxByUniqueId.iterator();
        while (it.hasNext()) {
            it.advance();
            final short pos = it.value();
            final S item = this.getFromPosition(pos);
            if (item == null) {
                ShortcutInventory.m_logger.error((Object)("Incoh\u00e9rence d'Inventory, l'item $" + it.key() + " est r\u00e9f\u00e9renc\u00e9 mais n'est pas pr\u00e9sent dans le tableau"), (Throwable)new Exception());
            }
            else {
                if (!item.shouldBeSerialized()) {
                    continue;
                }
                final RawShortcutInventory.Content content = new RawShortcutInventory.Content();
                content.position = pos;
                final boolean ok = item.toRaw(content.shortcut);
                if (!ok) {
                    ShortcutInventory.m_logger.error((Object)("Impossible de convertir le raccourci \u00e0 la position " + pos + " sous forme d\u00e9-s\u00e9rialis\u00e9e brute"));
                    return false;
                }
                raw.contents.add(content);
            }
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawShortcutInventory raw) {
        this.destroyAll();
        boolean ok = true;
        final ShortCutBarType[] types = ShortCutBarType.values();
        if (raw.type >= 0 && raw.type < types.length) {
            this.m_type = types[raw.type];
        }
        else {
            ok = false;
        }
        try {
            for (final RawShortcutInventory.Content content : raw.contents) {
                final S item = (S)this.m_contentProvider.unSerializeContent((R)content.shortcut);
                if (item != null) {
                    if (this.addAt(item, content.position)) {
                        continue;
                    }
                    ok = false;
                }
                else {
                    ok = false;
                    ShortcutInventory.m_logger.error((Object)"Erreur lors de la d\u00e9-serialisation d'un ArrayInventory : item null");
                }
            }
        }
        catch (InventoryCapacityReachedException e) {
            ShortcutInventory.m_logger.error((Object)e);
            ok = false;
        }
        catch (ContentAlreadyPresentException e2) {
            ShortcutInventory.m_logger.error((Object)e2);
            ok = false;
        }
        catch (PositionAlreadyUsedException e3) {
            ShortcutInventory.m_logger.error((Object)e3);
            ok = false;
        }
        return ok;
    }
    
    public ShortCutBarType getType() {
        return this.m_type;
    }
    
    public boolean containsUniqueIdFromType(final long itemUid, final ShortCutType type) {
        final S shortcut = this.getWithUniqueId(itemUid);
        return shortcut != null && shortcut.getType() == type;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShortcutInventory.class);
    }
}
