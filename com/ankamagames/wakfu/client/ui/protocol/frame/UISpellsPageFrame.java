package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.restat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.game.breed.*;

public class UISpellsPageFrame extends UICompanionsEmbeddedFrame implements BigDialogLoadListener, ShortcutBarManager.ShortcutBarTypeListener
{
    private static final Logger m_logger;
    private static final UISpellsPageFrame m_instance;
    private ShortCutBarType m_shortCutBarType;
    private LocalPlayerCharacter m_localPlayer;
    private boolean m_frameAdded;
    
    public UISpellsPageFrame() {
        super();
        this.m_frameAdded = false;
    }
    
    public static UISpellsPageFrame getInstance() {
        return UISpellsPageFrame.m_instance;
    }
    
    @Override
    public final void dialogLoaded(final String id) {
        if (id != null && !id.equals("spellsDialog")) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    public final void loadUnloadSpellsPage() {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        if (gameEntity.hasFrame(this)) {
            gameEntity.removeFrame(this);
        }
        else {
            gameEntity.pushFrame(this);
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 4214: {
                this.updateAll();
                break;
            }
            case 16435: {
                final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
                final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
                final SpellLevel spellLevel = localPlayer.getSpellInventory().getFirstWithReferenceId(localPlayer.getLockedSpellId());
                final SpellXpLockRequestMessage spellXpLockRequestMessage = new SpellXpLockRequestMessage();
                spellXpLockRequestMessage.setNoSpell();
                gameEntity.getNetworkEntity().sendMessage(spellXpLockRequestMessage);
                localPlayer.unregisterLockedSpell();
                PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, "lockedSpell");
                PropertiesProvider.getInstance().firePropertyValueChanged(spellLevel, SpellLevel.FIELDS);
                localPlayer.getShortcutBarManager().updateShorctutBarUsability();
                return false;
            }
            case 19270: {
                final UIMessage uiMessage = (UIMessage)message;
                final String mapId = uiMessage.getStringValue();
                if (!mapId.contains(this.getBaseDialogId())) {
                    return true;
                }
                final CharacterView characterView = UISpellsPageFrame.m_companionViews.get(uiMessage.getIntValue());
                if (characterView.isCompanion()) {
                    final SpellLevel spellLevel2 = characterView.getCharacterInfo().getSpellInventoryManager().getFirstSpellLevel();
                    this.selectSpellLevel(spellLevel2, mapId);
                }
                else {
                    this.selectLocalPlayerFirstSpell(mapId);
                }
                return false;
            }
            case 16436: {
                final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
                final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
                final SpellLevel spellLevel = localPlayer.getSpellInventory().getFirstWithReferenceId(localPlayer.getLockedSpellId());
                final UISpellLevelMessage uiSpellLevelMessage = (UISpellLevelMessage)message;
                final SpellLevel spell = uiSpellLevelMessage.getSpell();
                final SpellXpLockRequestMessage spellXpLockRequestMessage2 = new SpellXpLockRequestMessage();
                spellXpLockRequestMessage2.setSpellId(spell.getSpell().getId());
                gameEntity.getNetworkEntity().sendMessage(spellXpLockRequestMessage2);
                gameEntity.getLocalPlayer().registerLockedSpellId(spell.getSpell().getId());
                PropertiesProvider.getInstance().firePropertyValueChanged(gameEntity.getLocalPlayer(), "lockedSpell");
                PropertiesProvider.getInstance().firePropertyValueChanged(spell, SpellLevel.FIELDS);
                PropertiesProvider.getInstance().firePropertyValueChanged(spellLevel, SpellLevel.FIELDS);
                localPlayer.getShortcutBarManager().updateShorctutBarUsability();
                return false;
            }
        }
        return super.onMessage(message);
    }
    
    @Override
    public long getId() {
        return 50L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String getBaseDialogId() {
        return "spellsDialog";
    }
    
    private String getElementTranslatorString(final int id) {
        switch (id) {
            case 3: {
                return "desc.spellEarth";
            }
            case 2: {
                return "desc.spellWater";
            }
            case 4: {
                return "desc.spellWind";
            }
            case 1: {
                return "desc.spellFire";
            }
            case 5: {
                return "desc.spellStasis";
            }
            case 0: {
                return "desc.spellPhysical";
            }
            case 9: {
                return "desc.spellSupport";
            }
            default: {
                return null;
            }
        }
    }
    
    public final void updateAll() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_localPlayer, this.m_localPlayer.getFields());
    }
    
    private void checkSpellInventoryButton() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldAndFightBarDialog");
        if (map == null) {
            return;
        }
        final Widget button = (Widget)map.getElement("spellButton");
        if (button == null) {
            return;
        }
        button.getAppearance().removeTweensOfType(ModulationColorTween.class);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!this.m_frameAdded) {
            super.onFrameAdd(frameHandler, this.m_frameAdded);
            this.checkSpellInventoryButton();
            this.m_localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final SpellInventoryManager sim = this.m_localPlayer.getSpellInventoryManager();
            final AvatarBreedInfo breed = this.m_localPlayer.getBreedInfo();
            PropertiesProvider.getInstance().setPropertyValue("breedInfo", breed);
            final DialogLoadListener dialogLoadListener = new DialogLoadListener() {
                @Override
                public void dialogLoaded(final String id) {
                    if (id.equals("spellsDialog")) {
                        Xulor.getInstance().removeDialogLoadListener(this);
                    }
                }
            };
            Xulor.getInstance().addDialogLoadListener(dialogLoadListener);
            this.selectLocalPlayerFirstSpell(this.getBaseDialogId());
            Xulor.getInstance().putActionClass("wakfu.characterBook.spellsPage", SpellsPageDialogActions.class);
            if (!WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
                Xulor.getInstance().putActionClass("wakfu.companionsEmbedded", CompanionsEmbeddedActions.class);
            }
            WakfuSoundManager.getInstance().playGUISound(600114L);
            EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.SPELLS_OPEN, "spellsDialog");
            final ShortcutBarManager manager1 = this.m_localPlayer.getShortcutBarManager();
            this.m_shortCutBarType = manager1.getCurrentBarType();
            manager1.setCurrentBarType(ShortCutBarType.FIGHT, true);
            manager1.addShortcutBarTypeChangedListener(this);
            this.m_frameAdded = true;
        }
    }
    
    private void selectLocalPlayerFirstSpell(final String mapId) {
        try {
            final SpellLevel spellLevel = this.m_localPlayer.getSpellInventoryManager().getSpellsByElementId((byte)0).get(0);
            this.selectSpellLevel(spellLevel, mapId);
        }
        catch (Exception e) {
            UISpellsPageFrame.m_logger.error((Object)"Impossible de s\u00e9lectionner le premier sort \u00e9l\u00e9mentaire du joueur ! ");
            e.printStackTrace();
        }
    }
    
    public void selectSpellLevel(final SpellLevel spellLevel, final String mapId) {
        if (spellLevel == null) {
            return;
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("describedSpell", spellLevel, mapId);
        PropertiesProvider.getInstance().setLocalPropertyValue("editableDescribedSpell", spellLevel.getCopy(false, true), mapId);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (this.m_frameAdded) {
            PropertiesProvider.getInstance().removeProperty("breedInfo");
            PropertiesProvider.getInstance().removeProperty("describedSpell");
            PropertiesProvider.getInstance().removeProperty("editableDescribedSpell");
            Xulor.getInstance().removeActionClass("wakfu.characterBook.spellsPage");
            WakfuSoundManager.getInstance().playGUISound(600013L);
            final ShortcutBarManager manager1 = this.m_localPlayer.getShortcutBarManager();
            if (this.m_localPlayer.getCurrentFight() == null) {
                manager1.setCurrentBarType(this.m_shortCutBarType, true);
            }
            manager1.removeShortcutBarTypeChangedListener(this);
            super.onFrameRemove(frameHandler, !this.m_frameAdded);
            this.m_frameAdded = false;
        }
    }
    
    @Override
    public void onShortcutBarTypeChangeRequested(final ShortCutBarType shortCutBarType) {
        this.m_shortCutBarType = shortCutBarType;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UISpellsPageFrame.class);
        m_instance = new UISpellsPageFrame();
    }
}
