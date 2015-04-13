package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.crime.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;

class ClientCrimePurgation extends CrimePurgationCooldown
{
    private static final Logger m_logger;
    private final PrisonFlagFieldProvider m_prisonFlagFieldProvider;
    private static final boolean DEBUG_MODE = false;
    private static final int LAST_CHANGE_DISPLAY_DURATION = 3000;
    private long m_lastChangeUpdateTime;
    private long m_previousRemainingTime;
    
    ClientCrimePurgation(final ClientCitizenComportment comportment) {
        super(comportment);
        this.m_prisonFlagFieldProvider = new PrisonFlagFieldProvider();
        this.m_lastChangeUpdateTime = -1L;
        this.m_previousRemainingTime = -1L;
    }
    
    @Override
    public void run() {
        final long currentTime = WakfuGameCalendar.getInstance().getInternalTimeInMs();
        final int old = this.m_comportment.getCrimePurgationScore();
        super.run();
        if (old != this.m_comportment.getCrimePurgationScore()) {
            ((ClientCitizenComportment)this.m_comportment).onJailCooldownUpdate(old);
            this.m_prisonFlagFieldProvider.setFirstUpdate(false);
        }
        if (this.m_previousRemainingTime != -1L) {
            final long remainingTime = this.getRemainingTime();
            UIJailInfoFrame.getInstance().highlightTimer(3000);
            this.m_prisonFlagFieldProvider.setLastTimerChange(remainingTime - this.m_previousRemainingTime);
            this.m_lastChangeUpdateTime = currentTime;
            this.m_previousRemainingTime = -1L;
        }
        if (this.m_lastChangeUpdateTime != -1L && currentTime - this.m_lastChangeUpdateTime > 3000L) {
            this.m_prisonFlagFieldProvider.setLastTimerChange(0L);
            this.m_lastChangeUpdateTime = -1L;
        }
        if (this.getRemainingTime() <= 0L) {
            this.onCoolDownExpired();
        }
        this.onCoolDownRecomputed();
    }
    
    @Override
    public void addPoints(final int toAdd, final int previousScore) {
        this.m_previousRemainingTime = this.getRemainingTime(previousScore);
        super.addPoints(toAdd, previousScore);
    }
    
    @Override
    public boolean onCoolDownExpired() {
        ClientCrimePurgation.m_logger.info((Object)"Purgation de peine termin\u00e9e");
        super.onCoolDownExpired();
        this.stopCooldown();
        final TriggerServerEvent msg = new TriggerServerEvent();
        msg.setEventId(10006703);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        return false;
    }
    
    @Override
    public void stopCooldown() {
        ClientCrimePurgation.m_logger.info((Object)"Purgation de peine stopp\u00e9e");
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        ((ClientCitizenComportment)localPlayer.getCitizenComportment()).removePurgationCrimeScore();
        PropertiesProvider.getInstance().setPropertyValue("jailFlag", null);
        if (WakfuGameEntity.getInstance().hasFrame(UIJailInfoFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIJailInfoFrame.getInstance());
        }
        ProcessScheduler.getInstance().remove(this);
    }
    
    @Override
    public void onCoolDownLaunched() {
        ClientCrimePurgation.m_logger.info((Object)("Purgation de peine d\u00e9marr\u00e9e avec une dur\u00e9e de " + this.getRemainingTime() + "ms"));
        PropertiesProvider.getInstance().setPropertyValue("jailFlag", this.m_prisonFlagFieldProvider);
        if (!WakfuGameEntity.getInstance().hasFrame(UIJailInfoFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIJailInfoFrame.getInstance());
        }
    }
    
    public void onCoolDownRecomputed() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_prisonFlagFieldProvider, "remainingTime");
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientCrimePurgation.class);
    }
    
    private class PrisonFlagFieldProvider extends ImmutableFieldProvider
    {
        public static final String LAST_TIMER_CHANGE = "lastTimerChange";
        public static final String FORMATED_LAST_TIMER_CHANGE = "formatedLastTimerChange";
        public static final String ICON_URL_FIELD = "iconUrl";
        public static final String REMAINING_TIME_FIELD = "remainingTime";
        public static final String DESCRIPTION_FIELD = "description";
        public final String[] FIELDS;
        private long m_lastTimerChange;
        private boolean m_firstUpdate;
        
        private PrisonFlagFieldProvider() {
            super();
            this.FIELDS = new String[] { "description", "iconUrl", "remainingTime" };
            this.m_firstUpdate = true;
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("lastTimerChange")) {
                return this.m_lastTimerChange;
            }
            if (fieldName.equals("formatedLastTimerChange")) {
                if (this.m_lastTimerChange == 0L) {
                    return null;
                }
                final boolean neg = this.m_lastTimerChange < 0L;
                final String desc = (neg ? "-" : "") + TimeUtils.getShortDescription(GameInterval.fromSeconds(Math.abs(this.m_lastTimerChange) / 1000L));
                ClientCrimePurgation.m_logger.info((Object)("Formated last timer : " + desc));
                return desc;
            }
            else {
                if (fieldName.equals("iconUrl")) {
                    return WakfuConfiguration.getInstance().getFlagIconUrl(-1);
                }
                if (fieldName.equals("description")) {
                    return WakfuTranslator.getInstance().getString("nation.pvp.prisonerOf", this.getNationName());
                }
                if (!fieldName.equals("remainingTime")) {
                    return null;
                }
                if (this.m_firstUpdate) {
                    return "-";
                }
                final GameInterval d = GameInterval.fromSeconds(ClientCrimePurgation.this.getRemainingTime() / 1000L);
                return TimeUtils.getShortDescription(d);
            }
        }
        
        private String getNationName() {
            return WakfuTranslator.getInstance().getString(39, ClientCrimePurgation.this.m_comportment.getCrimePurgationNationId(), new Object[0]);
        }
        
        public void setFirstUpdate(final boolean firstUpdate) {
            this.m_firstUpdate = firstUpdate;
        }
        
        public void setLastTimerChange(final long lastTimerChange) {
            this.m_lastTimerChange = lastTimerChange;
            ClientCrimePurgation.m_logger.info((Object)("LastTimerChange : " + lastTimerChange));
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "lastTimerChange", "formatedLastTimerChange");
        }
    }
}
