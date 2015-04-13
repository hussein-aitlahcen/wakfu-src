package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.framework.text.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class AbstractMRUAction
{
    protected static final Logger m_logger;
    public static final String STYLE_NAME = "mru";
    protected boolean m_enabled;
    protected boolean m_usable;
    public static final String DEFAULT_TOOLTIP_COLOR;
    public static final String OK_TOOLTIP_COLOR;
    public static final String NOK_TOOLTIP_COLOR;
    public static final String PROBABLY_OK_TOOLTIP_COLOR = "9ed34b";
    public static final String PROBABLY_NOK_TOOLTIP_COLOR = "f48140";
    public static final AbstractMRUAction[] EMPTY_ARRAY;
    protected Object m_source;
    
    public AbstractMRUAction() {
        super();
        this.m_enabled = true;
        this.m_usable = true;
    }
    
    public abstract MRUActions tag();
    
    public abstract void run();
    
    public abstract boolean isRunnable();
    
    public boolean isUsable() {
        return this.m_usable;
    }
    
    public void setUsable(final boolean usable) {
        this.m_usable = usable;
    }
    
    public void initFromSource(Object source) {
        if (source instanceof FightRepresentationHelper.MRUableElement) {
            source = ((FightRepresentationHelper.MRUableElement)source).getFightLeader();
        }
        this.m_source = source;
    }
    
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? AbstractMRUAction.DEFAULT_TOOLTIP_COLOR : AbstractMRUAction.NOK_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey()));
        sb._b();
        return sb.finishAndToString();
    }
    
    public abstract String getTranslatorKey();
    
    public String getStyle() {
        return "mru" + this.getGFXId();
    }
    
    protected int getGFXId() {
        return this.tag().getActionId();
    }
    
    public String getTooltip() {
        return this.getLabel();
    }
    
    @Nullable
    public String getComplementaryTooltip() {
        return null;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public Iterable<ParticleDecorator> getParticleDecorators() {
        return (Iterable<ParticleDecorator>)Collections.emptyList();
    }
    
    public abstract AbstractMRUAction getCopy();
    
    protected final void tryToInteract(final InteractiveElementAction action) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final WakfuClientMapInteractiveElement interactiveElement = (WakfuClientMapInteractiveElement)this.m_source;
        final ArrayList<InteractiveElementActivationPattern> patterns = InteractiveElementActivationPattern.getPatterns(interactiveElement.getActivationPattern());
        final boolean remoteIE = interactiveElement.getApproachPoints().isEmpty() && patterns.isEmpty();
        if (remoteIE || interactiveElement.isInApproachPoint(localPlayer.getActor().getWorldCoordinates())) {
            this.doAction(action, localPlayer, interactiveElement);
        }
        else {
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("too.far.to.interact"));
            chatMessage.setPipeDestination(3);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
    }
    
    protected void doAction(final InteractiveElementAction action, final InteractiveElementUser localPlayer, final ClientMapInteractiveElement interactiveElement) {
        interactiveElement.fireAction(action, localPlayer);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMRUAction.class);
        DEFAULT_TOOLTIP_COLOR = Color.WHITE.getRGBtoHex();
        OK_TOOLTIP_COLOR = Color.GREEN.getRGBtoHex();
        NOK_TOOLTIP_COLOR = Color.RED.getRGBtoHex();
        EMPTY_ARRAY = new AbstractMRUAction[0];
    }
}
