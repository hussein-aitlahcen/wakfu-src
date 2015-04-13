package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public abstract class CompanionsEmbeddedActions
{
    public static final String PACKAGE = "wakfu.companionsEmbedded";
    
    public static void selectCompanion(final ItemEvent e, final Window w) {
        final String elementMapId = w.getElementMap().getId();
        final ShortCharacterView shortCharacterView = (ShortCharacterView)e.getItemValue();
        selectCompanion(elementMapId, shortCharacterView.getBreedId());
    }
    
    public static void selectCompanion(final Event ev, final ShortCharacterView view, final Window w) {
        final String elementMapId = w.getElementMap().getId();
        selectCompanion(elementMapId, view.getBreedId());
    }
    
    public static void selectHero(final Event e, final PlayerCharacter character, final Window w) {
        final PlayerCompanionViewShort shortView = new PlayerCompanionViewShort(character);
        final CharacterView characterView = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(shortView);
        PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", characterView, w.getElementMap().getId());
        final UIMessage msg = new UIMessage((short)19460);
        msg.setLongValue(shortView.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void selectCompanion(final String elementMapId, final int breedId) {
        final CharacterView characterView = UICompanionsEmbeddedFrame.getCharacterSheetView(breedId);
        PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", characterView, elementMapId);
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(19270);
        uiMessage.setStringValue(elementMapId);
        uiMessage.setIntValue(breedId);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void openSpellDescription(final int button, final SpellLevel spellLevel, final String dialogId) {
        final UISpellLevelSelectionMessage msg = new UISpellLevelSelectionMessage();
        msg.setSpell(spellLevel.getCopy(false, true));
        msg.setButton(button);
        msg.setStringValue(dialogId);
        msg.setId(16409);
        Worker.getInstance().pushMessage(msg);
        if (button == 1) {
            UISpellsPageFrame.getInstance().selectSpellLevel(spellLevel, dialogId);
        }
    }
    
    public static void openSpellDescription(final ItemEvent e) {
        openSpellDescription(e, null);
    }
    
    public static void openSpellDescription(final ItemEvent e, final CharacterView characterView) {
        SpellLevel spellLevel;
        if (e.getItemValue() instanceof SpellLevel) {
            spellLevel = (SpellLevel)e.getItemValue();
        }
        else {
            spellLevel = new SpellLevel((Spell)e.getItemValue(), (short)0, -1L);
        }
        final ElementMap map = e.getCurrentTarget().getElementMap();
        openSpellDescription(e.getButton(), spellLevel, map.getId());
        final TabbedContainer tabbedContainer = (TabbedContainer)map.getElement("tabbedContainer");
        if (tabbedContainer != null) {
            tabbedContainer.setSelectedTabIndex(0);
        }
        if (characterView != null && characterView.isCompanion()) {
            final CharacteristicCompanionView companionView = (CharacteristicCompanionView)characterView;
            playSpellAnimation(spellLevel, map, companionView);
        }
    }
    
    private static void playSpellAnimation(final SpellLevel spellLevel, final ElementMap map, final CharacteristicCompanionView companionView) {
        final CompanionSpellAnimation companionSpellAnimations = companionView.getSpellAnimations(spellLevel.getSpellId());
        if (companionSpellAnimations == null) {
            return;
        }
        final ArrayList<String> spellAnimations = companionSpellAnimations.getAnimations();
        final AnimatedElementViewer animatedElementViewer = (AnimatedElementViewer)map.getElement("animatedElementViewer");
        if (spellAnimations != null && spellAnimations.size() > 0) {
            animatedElementViewer.setAnimName(spellAnimations.get(0));
            if (spellAnimations.size() > 1) {
                final AnimatedElement animatedElement = animatedElementViewer.getAnimatedElement();
                animatedElement.addAnimationEndedListener(new AnimationEndedListener() {
                    @Override
                    public void animationEnded(final AnimatedElement element) {
                        animatedElementViewer.setAnimName(spellAnimations.get(1));
                        animatedElement.removeAnimationEndedListener(this);
                    }
                });
            }
        }
        final int apsCaster = companionSpellAnimations.getApsCaster();
        if (apsCaster != -1) {
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsCaster);
                    system.setTarget(animatedElementViewer.getAnimatedElement());
                    IsoParticleSystemManager.getInstance().addParticleSystem(system);
                }
            }, companionSpellAnimations.getApsDelayCaster(), 1);
        }
    }
    
    private static void setSelectedModulationColor(final Widget w) {
        w.getAppearance().setModulationColor(new Color(0.4f, 0.5f, 0.62f, 1.0f));
    }
    
    public static void onMouseOverSpell(final Event event, final Container container) {
        setSelectedModulationColor(container);
    }
    
    public static void onMouseOutSpell(final Event event, final Container container, final Object spell) {
        final String mapId = container.getRenderableParent().getElementMap().getId();
        final SpellLevel selectedSpellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("describedSpell", mapId);
        if (spell instanceof Spell && selectedSpellLevel != null && ((Spell)spell).getId() == selectedSpellLevel.getReferenceId()) {
            return;
        }
        if (spell instanceof SpellLevel && selectedSpellLevel != null && ((SpellLevel)spell).getReferenceId() == selectedSpellLevel.getReferenceId()) {
            return;
        }
        container.getAppearance().setModulationColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
    }
}
