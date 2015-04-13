package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.restat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.breed.*;

public class UISpellsRestatFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final UISpellsRestatFrame INSTANCE;
    private LocalPlayerCharacter m_localPlayer;
    private SpellRestatManagerDisplayer m_spellRestatManager;
    
    public void init(final SpellRestatComputer spellRestatComputer) {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        (this.m_spellRestatManager = new SpellRestatManagerDisplayer(gameEntity.getLocalPlayer(), spellRestatComputer)).initialize();
        gameEntity.pushFrame(this);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19351: {
                final UIRestatSpellModificationMessage uiRestatSpellModificationMessage = (UIRestatSpellModificationMessage)message;
                final RestatSpellLevel restatSpellLevel = uiRestatSpellModificationMessage.getRestatSpellLevel();
                final Spell spell = restatSpellLevel.getSpell();
                final int spellId = spell.getId();
                final Elements element = Elements.getElementFromId(spell.getElementId());
                final SpellRestatComputer spellRestatComputer = this.m_spellRestatManager.getSpellRestatComputer();
                if (!spellRestatComputer.canIncrementSpellLevel(element, spellId)) {
                    ModificationRunnable.INSTANCE.cleanModificationRunnable();
                    return false;
                }
                spellRestatComputer.incrementSpellLevel(element, spellId);
                this.m_spellRestatManager.refresh();
                final SpellLevel realSpellLevel = restatSpellLevel.getRealSpellLevel();
                PropertiesProvider.getInstance().firePropertyValueChanged(realSpellLevel, realSpellLevel.getFields());
                this.checkButtonAppearance();
                return false;
            }
            case 19352: {
                final UIRestatSpellModificationMessage uiRestatSpellModificationMessage = (UIRestatSpellModificationMessage)message;
                final RestatSpellLevel restatSpellLevel = uiRestatSpellModificationMessage.getRestatSpellLevel();
                final Spell spell = restatSpellLevel.getSpell();
                final int spellId = spell.getId();
                final Elements element = Elements.getElementFromId(spell.getElementId());
                final SpellRestatComputer spellRestatComputer = this.m_spellRestatManager.getSpellRestatComputer();
                if (!spellRestatComputer.canDecrementSpellLevel(element, spellId)) {
                    ModificationRunnable.INSTANCE.cleanModificationRunnable();
                    return false;
                }
                spellRestatComputer.decrementSpellLevel(element, spellId);
                this.m_spellRestatManager.refresh();
                PropertiesProvider.getInstance().firePropertyValueChanged(restatSpellLevel, restatSpellLevel.getFields());
                this.checkButtonAppearance();
                return false;
            }
            case 19353: {
                this.m_spellRestatManager.getSpellRestatComputer().validateRestat();
                return false;
            }
            case 19354: {
                this.m_spellRestatManager.getSpellRestatComputer().reset();
                this.m_spellRestatManager.refresh();
                this.checkButtonAppearance();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void checkButtonAppearance() {
        final Widget b = (Widget)this.getElementMap().getElement("validButton");
        if (this.m_spellRestatManager.getSpellRestatComputer().canValidateRestat()) {
            HighlightUIHelpers.highlightWidgetUntilClick(b);
        }
        else {
            b.getAppearance().removeTweensOfType(ModulationColorTween.class);
        }
    }
    
    private ElementMap getElementMap() {
        return Xulor.getInstance().getEnvironment().getElementMap("spellsRestatDialog");
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
            this.m_localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final AvatarBreedInfo breed = this.m_localPlayer.getBreedInfo();
            PropertiesProvider.getInstance().setPropertyValue("breedInfo", breed);
            Xulor.getInstance().load("spellsRestatDialog", Dialogs.getDialogPath("spellsRestatDialog"), 8449L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("describedSpell", null, "spellsRestatDialog");
            Xulor.getInstance().putActionClass("wakfu.spellsRestat", SpellsRestatDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600114L);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_spellRestatManager = null;
            PropertiesProvider.getInstance().removeProperty("breedInfo");
            Xulor.getInstance().unload("spellsRestatDialog");
            Xulor.getInstance().removeActionClass("wakfu.spellsRestat");
            WakfuSoundManager.getInstance().playGUISound(600013L);
        }
    }
    
    public SpellRestatComputer getSpellRestatManagerComputer() {
        return this.m_spellRestatManager.getSpellRestatComputer();
    }
    
    public SpellRestatManagerDisplayer getSpellRestatManager() {
        return this.m_spellRestatManager;
    }
    
    @Override
    public String toString() {
        return "UISpellsRestatFrame{m_localPlayer=" + this.m_localPlayer + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)UISpellsRestatFrame.class);
        INSTANCE = new UISpellsRestatFrame();
    }
}
