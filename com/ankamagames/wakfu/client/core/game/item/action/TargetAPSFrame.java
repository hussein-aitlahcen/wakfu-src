package com.ankamagames.wakfu.client.core.game.item.action;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.util.*;

public class TargetAPSFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final byte DEFAULT_SELECTION = 0;
    public static final byte POSITION_SELECTION = 1;
    public static final byte CHARACTER_SELECTION = 2;
    private static final TargetAPSFrame m_instance;
    private static final String LAYER_NAME = "APSTarget";
    private static final float[] LAYER_COLOR;
    private DisplayedScreenElement m_oldElem;
    private TargetAPSSelectionListener m_listener;
    private CharacterActor m_oldCharacter;
    private byte m_mode;
    
    public void selectMode(final byte mode) {
        this.m_mode = mode;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        try {
            HighLightManager.getInstance().createLayer("APSTarget");
        }
        catch (Exception e) {
            TargetAPSFrame.m_logger.error((Object)"Erreur durant la cr\u00e9ation d'un layer sur le HighLightManager");
        }
        CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (this.m_listener != null) {
            final Point3 pt = (this.m_oldElem == null) ? null : new Point3(this.m_oldElem.getWorldCellX(), this.m_oldElem.getWorldCellY(), this.m_oldElem.getWorldCellAltitude());
            this.m_listener.onSelectionCanceled(pt);
            this.m_listener = null;
        }
        HighLightManager.getInstance().clearLayer("APSTarget");
        HighLightManager.getInstance().removeLayer("APSTarget");
        CursorFactory.getInstance().unlock();
        this.m_mode = 0;
        this.m_oldElem = null;
        if (this.m_oldCharacter != null) {
            this.m_oldCharacter.unHighlightCharacter();
            this.m_oldCharacter = null;
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        switch (message.getId()) {
            case 19994: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final AnimatedInteractiveElement elt = worldScene.getNearestElement();
                if (elt instanceof CharacterActor) {
                    final CharacterActor character = (CharacterActor)elt;
                    character.highlightCharacter();
                    this.m_oldCharacter = character;
                    final DisplayedScreenElement targetElt = DisplayedScreenWorld.getInstance().getNearesetWalkableElement(character.getWorldCellX(), character.getWorldCellY(), character.getWorldCellAltitude(), ElementFilter.VISIBLE_ONLY);
                    if (targetElt != this.m_oldElem) {
                        if (targetElt != null) {
                            HighLightManager.getInstance().clearLayer("APSTarget");
                            HighLightManager.getInstance().add(targetElt.getLayerReference(), "APSTarget");
                            HighLightManager.getInstance().getLayer("APSTarget").setColor(TargetAPSFrame.LAYER_COLOR);
                        }
                        this.m_oldElem = targetElt;
                    }
                }
                else if (this.m_oldCharacter != null) {
                    this.m_oldCharacter.unHighlightCharacter();
                    this.m_oldCharacter = null;
                }
                else {
                    final ArrayList<DisplayedScreenElement> hitElements = WakfuClientInstance.getInstance().getWorldScene().getDisplayedElementsUnderMousePoint(msg.getMouseX(), msg.getMouseY(), 0.0f, DisplayedScreenElementComparator.CELL_CENTER_DISTANCE);
                    DisplayedScreenElement targetElt = null;
                    if (hitElements != null) {
                        for (int i = 0; i < hitElements.size(); ++i) {
                            final DisplayedScreenElement element = hitElements.get(i);
                            if (TopologyMapManager.isWalkable(element.getWorldCellX(), element.getWorldCellY(), element.getWorldCellAltitude())) {
                                targetElt = element;
                                break;
                            }
                        }
                    }
                    if (targetElt != this.m_oldElem) {
                        if (targetElt != null) {
                            HighLightManager.getInstance().clearLayer("APSTarget");
                            HighLightManager.getInstance().add(targetElt.getLayerReference(), "APSTarget");
                            HighLightManager.getInstance().getLayer("APSTarget").setColor(TargetAPSFrame.LAYER_COLOR);
                        }
                        this.m_oldElem = targetElt;
                    }
                }
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (msg.isButtonLeft()) {
                    final AnimatedInteractiveElement elt = worldScene.getNearestElement();
                    if ((this.m_mode == 2 || this.m_mode == 0) && elt instanceof CharacterActor && this.m_listener != null) {
                        final CharacterActor character = (CharacterActor)elt;
                        this.m_listener.onCharacterSelected(character);
                        this.m_listener = null;
                    }
                    if ((this.m_mode == 1 || this.m_mode == 0) && this.m_listener != null) {
                        final Point3 point = WorldSceneInteractionUtils.getNearestPoint3(WakfuClientInstance.getInstance().getWorldScene(), msg.getMouseX(), msg.getMouseY(), true);
                        this.m_listener.onPositionSelected(point);
                        this.m_listener = null;
                    }
                }
                WakfuGameEntity.getInstance().removeFrame(this);
                break;
            }
        }
        return false;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public static TargetAPSFrame getInstance() {
        return TargetAPSFrame.m_instance;
    }
    
    public void setSelectionListener(final TargetAPSSelectionListener listener) {
        this.m_listener = listener;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TargetAPSFrame.class);
        m_instance = new TargetAPSFrame();
        LAYER_COLOR = new float[] { 1.0f, 1.0f, 1.0f, 0.6f };
    }
}
