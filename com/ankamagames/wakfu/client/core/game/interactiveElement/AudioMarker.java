package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.framework.sound.group.field.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class AudioMarker extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private static final Pattern PARAMETER_PATTERN;
    private int m_audioMarkerType;
    private boolean m_localized;
    
    private AudioMarker() {
        super();
        this.setIsDummy();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setVisible(true);
        this.m_audioMarkerType = 0;
        this.m_localized = false;
        this.setState((short)0);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(false);
        this.setUseSpecificAnimTransition(false);
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        AudioMarker.m_logger.debug((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.NONE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public byte getHeight() {
        return 0;
    }
    
    public int getAudioMarkerType() {
        return this.m_audioMarkerType;
    }
    
    public void launchLocalizedAudioMarkerEvent() {
        if (!this.m_isSpawned || !this.m_localized || !this.isVisible()) {
            return;
        }
        final int x = this.getWorldCellX();
        final int y = this.getWorldCellY();
        final short z = this.getWorldCellAltitude();
        final int groupId = WakfuClientInstance.getInstance().getWorldScene().getGroupId(x, y, z);
        WakfuSoundManager.getInstance().onAudioMarkerSpawn(this.m_id, AudioMarkerType.getFromId(this.m_audioMarkerType), new StaticPositionProvider(x, y, z, false, groupId));
    }
    
    @Override
    public void onSpawn() {
        super.onSpawn();
        this.launchLocalizedAudioMarkerEvent();
    }
    
    @Override
    public void onDeSpawn() {
        this.stopLocalizedAudioMarkerEvent();
        super.onDeSpawn();
    }
    
    private void stopLocalizedAudioMarkerEvent() {
        if (this.m_localized) {
            WakfuSoundManager.getInstance().onAudioMarkerDespawn(this.m_id);
        }
    }
    
    @Override
    public void setVisible(final boolean visible) {
        final boolean oldVisible = this.isVisible();
        super.setVisible(visible);
        if (oldVisible == visible) {
            return;
        }
        if (visible) {
            this.launchLocalizedAudioMarkerEvent();
        }
        else {
            this.stopLocalizedAudioMarkerEvent();
        }
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final String[] params = AudioMarker.PARAMETER_PATTERN.split(this.m_parameter);
        if (params.length != 1) {
            AudioMarker.m_logger.error((Object)("[LD] L'audioMarker " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        int parameterId;
        try {
            parameterId = Integer.valueOf(params[0]);
        }
        catch (NumberFormatException e) {
            AudioMarker.m_logger.error((Object)("[LD] L'audioMarker " + this.m_id + " a un parametre [" + params[0] + "] qui n'est pas du bon type (id attendu)"));
            return;
        }
        final IEAudioMarkerParameter param = (IEAudioMarkerParameter)IEParametersManager.INSTANCE.getParam(IETypes.AUDIO_MARKER, parameterId);
        if (param == null) {
            AudioMarker.m_logger.error((Object)("[LD] L'audioMarker " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_audioMarkerType = param.getAudioMarkerTypeId();
        this.m_localized = param.isLocalized();
    }
    
    @Override
    public boolean fireAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AudioMarker.class);
        PARAMETER_PATTERN = Pattern.compile(";");
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            AudioMarker audioMarker;
            try {
                audioMarker = (AudioMarker)Factory.m_pool.borrowObject();
                audioMarker.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                AudioMarker.m_logger.error((Object)"Erreur lors de l'extraction d'un AudioMarker du pool", (Throwable)e);
                audioMarker = new AudioMarker(null);
            }
            return audioMarker;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<AudioMarker>() {
                @Override
                public AudioMarker makeObject() {
                    return new AudioMarker(null);
                }
            });
        }
    }
}
