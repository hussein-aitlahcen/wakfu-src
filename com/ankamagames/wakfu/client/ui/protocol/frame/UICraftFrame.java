package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.craft.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.craft.*;

public class UICraftFrame implements MessageFrame, CraftListener
{
    protected static final Logger m_logger;
    private static final UICraftFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UICraftFrame getInstance() {
        return UICraftFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (CraftDisplayer.INSTANCE.isEmpty()) {
                WakfuGameEntity.getInstance().removeFrame(this);
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("craftDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UICraftFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            localPlayer.getCraftHandler().addListener(this);
            Xulor.getInstance().load("craftDialog", Dialogs.getDialogPath("craftDialog"), 32769L, (short)10000);
            this.setCurrentCtaftFromPrefs();
            Xulor.getInstance().putActionClass("wakfu.craft", CraftDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600057L);
            if (!WakfuGameEntity.getInstance().hasFrame(UICraftTableFrame.getInstance())) {
                Xulor.getInstance().putActionClass("wakfu.crafts", CraftDialogsActions.class);
            }
        }
    }
    
    public void setCurrentCtaftFromPrefs() {
        final int craftId = WakfuClientInstance.getInstance().getGamePreferences().getIntValue(WakfuKeyPreferenceStoreEnum.LAST_CRAFT_SEEN);
        this.setCurrentCraft(craftId);
    }
    
    public void setCurrentCraft(final int craftId) {
        CraftView craft;
        if (craftId == -1 || !WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler().contains(craftId)) {
            craft = CraftDisplayer.INSTANCE.getFirstKnownCraftView();
        }
        else {
            craft = CraftDisplayer.INSTANCE.getKnownCraftView(craftId);
        }
        CraftType type;
        if (craft.hasHarvests()) {
            type = CraftType.HARVEST;
        }
        else {
            type = CraftType.CRAFT;
        }
        craft.setCurrentType(type);
        PropertiesProvider.getInstance().setPropertyValue("craftDisplayType", type.getId());
        PropertiesProvider.getInstance().setLocalPropertyValue("craft", craft, "craftDialog");
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            localPlayer.getCraftHandler().removeListener(this);
            Xulor.getInstance().unload("craftDialog");
            Xulor.getInstance().unload("craftLinkedRecipeDialog");
            PropertiesProvider.getInstance().removeProperty("linkedRecipes");
            PropertiesProvider.getInstance().removeProperty("linkedRecipesItemName");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.craft");
            WakfuSoundManager.getInstance().playGUISound(600058L);
            if (!WakfuGameEntity.getInstance().hasFrame(UICraftTableFrame.getInstance())) {
                Xulor.getInstance().removeActionClass("wakfu.crafts");
            }
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16833: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int craftId = msg.getIntValue();
                this.setCurrentCraft(craftId);
                return false;
            }
            case 16830: {
                final CraftView craftView = ((UISelectCraftMessage)message).getCraftView();
                CraftType type = CraftType.getFromId((byte)PropertiesProvider.getInstance().getIntProperty("craftDisplayType"));
                switch (type) {
                    case HARVEST: {
                        if (!craftView.hasHarvests()) {
                            type = CraftType.CRAFT;
                            break;
                        }
                        break;
                    }
                    case CRAFT: {
                        if (!craftView.hasRecipes()) {
                            type = CraftType.HARVEST;
                            break;
                        }
                        break;
                    }
                }
                PropertiesProvider.getInstance().setPropertyValue("craftDisplayType", type.getId());
                PropertiesProvider.getInstance().setLocalPropertyValue("craft", craftView, "craftDialog");
                craftView.setCurrentType(type);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.LAST_CRAFT_SEEN, craftView.getCraftReferenceId());
                return false;
            }
            case 16831: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int itemId = msg.getIntValue();
                this.createLinkedRecipes(itemId);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void createLinkedRecipes(final int ingredientId) {
        final Collection<LightCraftView> views = new ArrayList<LightCraftView>();
        CraftManager.INSTANCE.foreachCraft(new TObjectProcedure<ReferenceCraft>() {
            @Override
            public boolean execute(final ReferenceCraft craft) {
                final LightCraftView view = new LightCraftView(craft.getId(), ingredientId);
                if (view.hasRecipes()) {
                    view.createRecipeViews(ingredientId);
                }
                if (view.hasRecipesWithIngredient()) {
                    views.add(view);
                }
                return true;
            }
        });
        PropertiesProvider.getInstance().setPropertyValue("linkedRecipes", views);
        PropertiesProvider.getInstance().setPropertyValue("linkedRecipesItemName", WakfuTranslator.getInstance().getString(15, ingredientId, new Object[0]));
        if (!Xulor.getInstance().isLoaded("craftLinkedRecipeDialog")) {
            Xulor.getInstance().loadAsMultiple("craftLinkedRecipeDialog", Dialogs.getDialogPath("craftLinkedRecipeDialog"), "craftDialog", "craftDialog", "craftLinkedRecipeDialog", 0L, (short)10000);
        }
    }
    
    @Override
    public void onCraftLearned(final ReferenceCraft refCraft) {
        PropertiesProvider.getInstance().firePropertyValueChanged(CraftDisplayer.INSTANCE, "craftsByLevel");
    }
    
    @Override
    public void onCraftXpGained(final int craftId, final long xpAdded) {
        PropertiesProvider.getInstance().firePropertyValueChanged(CraftDisplayer.INSTANCE, "craftsByLevel");
        final AbstractCraftView craftView = (AbstractCraftView)PropertiesProvider.getInstance().getObjectProperty("craft", "craftDialog");
        if (craftId == craftView.getCraftReferenceId()) {
            PropertiesProvider.getInstance().firePropertyValueChanged(craftView, "currentXpPercentage", "level", "levelText");
        }
    }
    
    @Override
    public void onRecipeLearned(final int craftId, final int recipeId) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)UICraftFrame.class);
        m_instance = new UICraftFrame();
    }
}
