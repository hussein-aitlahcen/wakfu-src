package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;

public class UIChestLootFrame implements MessageFrame
{
    private static UIChestLootFrame m_instance;
    private ChestLootFieldProvider m_chestLootFieldProvider;
    private ArrayList<ReferenceItemFieldProvider> m_lootItemToAdd;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIChestLootFrame getInstance() {
        return UIChestLootFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("chestLootDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIChestLootFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_chestLootFieldProvider = new ChestLootFieldProvider();
            if (this.m_lootItemToAdd != null) {
                for (final ReferenceItemFieldProvider referenceItemFieldProvider : this.m_lootItemToAdd) {
                    this.m_chestLootFieldProvider.addLootItem(referenceItemFieldProvider);
                }
                this.m_lootItemToAdd.clear();
            }
            final ChestLootFieldProvider[] chestLootFieldProviders = { this.m_chestLootFieldProvider };
            PropertiesProvider.getInstance().setPropertyValue("chestLoot", chestLootFieldProviders);
            Xulor.getInstance().load("chestLootDialog", Dialogs.getDialogPath("chestLootDialog"), 257L, (short)10000);
            WakfuSoundManager.getInstance().playGUISound(600068L);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_chestLootFieldProvider = null;
            this.m_lootItemToAdd = null;
            Xulor.getInstance().unload("chestLootDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("chestLoot");
        }
    }
    
    public void addLootItem(final int refId, final short quantity) {
        if (this.m_chestLootFieldProvider == null) {
            if (this.m_lootItemToAdd == null) {
                this.m_lootItemToAdd = new ArrayList<ReferenceItemFieldProvider>();
            }
            this.m_lootItemToAdd.add(new ReferenceItemFieldProvider(refId, quantity));
            return;
        }
        this.m_chestLootFieldProvider.addLootItem(refId, quantity);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_chestLootFieldProvider, "loots");
    }
    
    static {
        UIChestLootFrame.m_instance = new UIChestLootFrame();
    }
    
    private static class ChestLootFieldProvider extends ImmutableFieldProvider
    {
        public static final String NAME_FIELD = "name";
        public static final String LEVEL_FIELD = "level";
        public static final String LOOTS_FIELD = "loots";
        public final String[] FIELDS;
        private final List<ReferenceItemFieldProvider> m_lootItems;
        
        private ChestLootFieldProvider() {
            super();
            this.FIELDS = new String[] { "name", "level", "loots" };
            this.m_lootItems = new ArrayList<ReferenceItemFieldProvider>();
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return WakfuGameEntity.getInstance().getLocalPlayer().getName();
            }
            if (fieldName.equals("level")) {
                return WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
            }
            if (fieldName.equals("loots")) {
                return this.m_lootItems.isEmpty() ? null : Collections.unmodifiableCollection((Collection<?>)this.m_lootItems);
            }
            return null;
        }
        
        public void addLootItem(final int refId, final short quantity) {
            this.m_lootItems.add(new ReferenceItemFieldProvider(refId, quantity));
        }
        
        public void addLootItem(final ReferenceItemFieldProvider referenceItemFieldProvider) {
            if (!this.m_lootItems.contains(referenceItemFieldProvider)) {
                this.m_lootItems.add(referenceItemFieldProvider);
            }
        }
    }
}
