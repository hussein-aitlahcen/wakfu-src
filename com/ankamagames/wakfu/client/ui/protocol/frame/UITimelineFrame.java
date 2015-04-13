package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.character.*;
import com.ankamagames.wakfu.client.ui.protocol.message.effectArea.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.*;

public class UITimelineFrame implements MessageFrame, DialogUnloadListener
{
    private static UITimelineFrame m_instance;
    private static final String WATCHER_CONTAINER_ID = "watcherContainer";
    private ArrayList<String> m_timelineCells;
    private ArrayList<String> m_openedFighterDescriptions;
    private Timeline m_timeline;
    
    public UITimelineFrame() {
        super();
        this.m_timelineCells = new ArrayList<String>();
        this.m_openedFighterDescriptions = new ArrayList<String>();
    }
    
    public static UITimelineFrame getInstance() {
        return UITimelineFrame.m_instance;
    }
    
    public void clearDialogs() {
    }
    
    public void openCloseFighterDescription(final CharacterInfo info) {
        if (HoodedMonsterFightEventListener.isVisuallyHooded(info)) {
            return;
        }
        final String dialogId = "fighterDescriptionDialog" + info.getId();
        if (this.m_openedFighterDescriptions.contains(dialogId)) {
            this.closeFighterDescription(dialogId);
        }
        else {
            this.openFighterDescription(info, dialogId);
        }
    }
    
    public void openFighterDescription(final CharacterInfo info) {
        final String dialogId = "fighterDescriptionDialog" + info.getId();
        this.openFighterDescription(info, dialogId);
    }
    
    public void closeFighterDescription(final CharacterInfo info) {
        final String dialogId = "fighterDescriptionDialog" + info.getId();
        this.closeFighterDescription(dialogId);
    }
    
    private void openFighterDescription(final CharacterInfo info, final String dialogId) {
        if (info.hasProperty(FightPropertyType.DISPLAYED_LIKE_A_DECORATION)) {
            return;
        }
        if (!this.m_openedFighterDescriptions.contains(dialogId)) {
            this.m_openedFighterDescriptions.add(dialogId);
            final Widget descriptionDialog = (Widget)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("fighterDescriptionDialog"), 1L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("fighter", info, dialogId);
            descriptionDialog.setSizeToPrefSize();
        }
    }
    
    private void closeFighterDescription(final String dialogId) {
        if (this.m_openedFighterDescriptions.remove(dialogId)) {
            Xulor.getInstance().unload(dialogId);
        }
    }
    
    @Override
    public void dialogUnloaded(final String id) {
        this.m_openedFighterDescriptions.remove(id);
    }
    
    private void displayHideTimelineCell(final AbstractEffectArea area, final Carrier carrier, final boolean display) {
        final FighterCharacteristic characteristic = (FighterCharacteristic)area.getDisplayedCharacteristic();
        if (characteristic == null) {
            return;
        }
        final String dialogId = "timelineCellDialog2" + area.getId();
        if (display) {
            final GraphicalAreaProvider provider = (GraphicalAreaProvider)area;
            final GraphicalArea graphicalArea = provider.getGraphicalArea();
            if (graphicalArea == null) {
                return;
            }
            ScreenTarget target;
            if (carrier != null) {
                final CharacterInfo characterInfo = (CharacterInfo)carrier;
                target = characterInfo.getActor();
            }
            else {
                target = graphicalArea.getAnimatedElement();
            }
            WatcherContainer wc;
            if (Xulor.getInstance().isLoaded(dialogId)) {
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
                wc = (WatcherContainer)map.getElement("watcherContainer");
            }
            else {
                wc = (WatcherContainer)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("timelineCellDialog2"), 8192L, (short)10000);
            }
            PropertiesProvider.getInstance().setLocalPropertyValue("timeline.fighter", new TimelineCellView(characteristic), dialogId);
            wc.setTarget(target);
            if (!this.m_timelineCells.contains(dialogId)) {
                this.m_timelineCells.add(dialogId);
            }
        }
        else if (this.m_timelineCells.remove(dialogId)) {
            Xulor.getInstance().unload(dialogId);
        }
    }
    
    private void displayHideTimelineCell(final CharacterInfo info, final boolean display) {
        final String dialogId = "timelineCellDialog" + info.getId();
        if (display) {
            if (!this.m_timelineCells.contains(dialogId)) {
                final WatcherContainer wc = (WatcherContainer)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("timelineCellDialog"), 8192L, (short)10000);
                PropertiesProvider.getInstance().setLocalPropertyValue("timeline.fighter", info, wc.getElementMap());
                wc.setTarget(info.getActor());
                this.m_timelineCells.add(dialogId);
            }
        }
        else if (this.m_timelineCells.remove(dialogId)) {
            Xulor.getInstance().unload(dialogId);
        }
    }
    
    private void setTimelineCellAvailability(final String dialogId, final boolean available) {
    }
    
    public void highlightTimeScoreBar(final int index) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("timePointBarDialog");
        if (map == null) {
            return;
        }
        if (index == 3) {
            this.highlightTimeScore((Widget)map.getElement("extraTurnScoreImage" + (index - 2)));
            this.highlightTimeScore((Widget)map.getElement("extraTurnScoreImage" + (index - 1)), 250);
            this.highlightTimeScore((Widget)map.getElement("extraTurnScoreImage" + index), 500);
        }
        else {
            this.highlightTimeScore((Widget)map.getElement("extraTurnScoreImage" + index));
        }
    }
    
    private void highlightTimeScore(final Widget widget) {
        this.highlightTimeScore(widget, 0);
    }
    
    private void highlightTimeScore(final Widget widget, final int delay) {
        if (widget == null) {
            return;
        }
        final DecoratorAppearance app = widget.getAppearance();
        final Color c1 = Color.WHITE;
        final Color c2 = Color.WHITE_ALPHA;
        app.removeTweensOfType(ModulationColorTween.class);
        app.addTween(new ModulationColorTween(c1, c2, app, delay, 250, 3, true, TweenFunction.PROGRESSIVE));
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18103: {
                final UICharacterInfoMessage msg = (UICharacterInfoMessage)message;
                this.displayHideTimelineCell(msg.getCharacterInfo(), msg.getBooleanValue());
                return false;
            }
            case 18106: {
                final UIEffectAreaMessage msg2 = (UIEffectAreaMessage)message;
                this.displayHideTimelineCell(msg2.getArea(), msg2.getTarget(), msg2.getBooleanValue());
                return false;
            }
            case 18100: {
                final boolean state = PropertiesProvider.getInstance().getBooleanProperty("displaySecondTimeline");
                PropertiesProvider.getInstance().setPropertyValue("displaySecondTimeline", !state);
                return false;
            }
            case 18101: {
                final boolean state = PropertiesProvider.getInstance().getBooleanProperty("timeline.displayStates");
                PropertiesProvider.getInstance().setPropertyValue("timeline.displayStates", !state);
                return false;
            }
            case 19990: {
                final UIWorldSceneKeyMessage msg3 = (UIWorldSceneKeyMessage)message;
                switch (msg3.getKeyEvent().getKeyCode()) {
                    case 17: {
                        DisplayHpBarCommand.invertDisplay(true);
                        break;
                    }
                }
                return false;
            }
            case 19991: {
                final UIWorldSceneKeyMessage msg3 = (UIWorldSceneKeyMessage)message;
                switch (msg3.getKeyEvent().getKeyCode()) {
                    case 17: {
                        DisplayHpBarCommand.invertDisplay(false);
                        break;
                    }
                }
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final WakfuWorldScene scene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
            scene.setDispatchKeyPressedMessage(true);
            scene.setDispatchKeyReleasedMessage(true);
            Xulor.getInstance().addDialogUnloadListener(this);
            PropertiesProvider.getInstance().setPropertyValue("displaySecondTimeline", false);
            PropertiesProvider.getInstance().setPropertyValue("timeline.displayStates", true);
            DisplayHpBarCommand.invertDisplay(false);
            this.m_timeline = (Timeline)PropertiesProvider.getInstance().getObjectProperty("fight.timeline");
            if (this.m_timeline != null) {
                for (final CharacterInfo c : this.m_timeline.getOrderedFightersThisTurn()) {
                    this.displayHideTimelineCell(c, true);
                }
                this.m_timeline.displayCurrentFighter();
            }
            FightInfo fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
            if (fight == null) {
                fight = WakfuGameEntity.getInstance().getLocalPlayer().getObservedFight();
            }
            Xulor.getInstance().load("timePointBarDialog", Dialogs.getDialogPath("timePointBarDialog"), 270336L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.timeline", TimelineDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final WakfuWorldScene scene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
            scene.setDispatchKeyPressedMessage(false);
            scene.setDispatchKeyReleasedMessage(false);
            Xulor.getInstance().removeDialogUnloadListener(this);
            Xulor.getInstance().unload("timePointBarDialog");
            Xulor.getInstance().unload("timePointBonusSelectionDialog");
            for (int i = this.m_timelineCells.size() - 1; i >= 0; --i) {
                Xulor.getInstance().unload(this.m_timelineCells.get(i));
            }
            this.m_timelineCells.clear();
            for (int i = this.m_openedFighterDescriptions.size() - 1; i >= 0; --i) {
                Xulor.getInstance().unload(this.m_openedFighterDescriptions.get(i));
            }
            this.m_openedFighterDescriptions.clear();
            Xulor.getInstance().removeActionClass("wakfu.timeline");
        }
    }
    
    public void refreshTimeLineBonusParticle(final int level) {
        final ElementMap map = null;
        if (map == null) {
            return;
        }
        this.setInnerParticleLevel(level, (Widget)map.getElement("numBonusLabel"));
    }
    
    private void setInnerParticleLevel(final int level, final Widget widget) {
        final ArrayList<EventDispatcher> children = widget.getAppearance().getChildren();
        for (int i = 0, size = children.size(); i < size; ++i) {
            final EventDispatcher decorator = children.get(i);
            if (decorator instanceof ParticleDecorator) {
                ((ParticleDecorator)decorator).setLevel(level);
            }
        }
    }
    
    public Timeline getTimeline() {
        return this.m_timeline;
    }
    
    static {
        UITimelineFrame.m_instance = new UITimelineFrame();
    }
}
