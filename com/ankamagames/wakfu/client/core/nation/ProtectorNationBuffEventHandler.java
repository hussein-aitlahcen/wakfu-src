package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.synchronizing.*;
import com.ankamagames.wakfu.client.core.game.synchronizing.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class ProtectorNationBuffEventHandler implements NationBuffEventHandler, NationMembersEventHandler
{
    public static ProtectorNationBuffEventHandler INSTANCE;
    private TIntObjectHashMap<IntArray> m_prevBuffs;
    private TIntLongHashMap m_prevBuffsStartTime;
    protected static final Logger m_logger;
    
    @Override
    public void onMemberAdded(final Citizen citizen) {
    }
    
    @Override
    public void onMemberRemoved(final Nation nation, final Citizen character) {
    }
    
    @Override
    public void onNationBuffsChanged(final int protectorId, final Nation nation) {
        BarrierManager.INSTANCE.blockOrExecute(new Runnable() {
            @Override
            public void run() {
                ProtectorNationBuffEventHandler.this.reloadUi(WakfuGameEntity.getInstance().getLocalPlayer(), protectorId);
            }
        }, SynchroBarriers.LOCAL_PLAYER_LOADED);
    }
    
    private void reloadUi(final Citizen character, final int protectorId) {
        if (protectorId == -1) {
            return;
        }
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        if (lpc == null) {
            return;
        }
        if (lpc != character) {
            return;
        }
        final CitizenComportment citizen = lpc.getCitizenComportment();
        if (citizen == null) {
            return;
        }
        final Nation nation = citizen.getNation();
        final Protector protector = ProtectorManager.INSTANCE.getStaticProtector(protectorId);
        if (protector == null) {
            return;
        }
        if (!lpc.isOnFight()) {
            lpc.reloadProtectorBuffs();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), "boughtBuffs");
        final String protectorName = protector.getName();
        final IntArray newBuffs = nation.getProtectorBuffs(protectorId);
        if (newBuffs == null) {
            return;
        }
        if (newBuffs.size() > 1) {
            ProtectorNationBuffEventHandler.m_logger.error((Object)"[DESIGN] pas pr\u00e9vu : on a plusieurs buffs diff\u00e9rents sur un m\u00eame mdc, impossible de cr\u00e9er le popup de notification pour le joueur");
            return;
        }
        if (this.m_prevBuffs == null) {
            this.m_prevBuffs = new TIntObjectHashMap<IntArray>();
            this.m_prevBuffsStartTime = new TIntLongHashMap();
        }
        final IntArray prevBuffs = this.m_prevBuffs.get(protectorId);
        if (prevBuffs == null) {
            this.m_prevBuffs.put(protectorId, newBuffs);
            this.m_prevBuffsStartTime.put(protectorId, -1L);
            return;
        }
        if (this.m_prevBuffsStartTime.get(protectorId) != -1L && System.currentTimeMillis() - this.m_prevBuffsStartTime.get(protectorId) < 150000.0) {
            ProtectorNationBuffEventHandler.m_logger.info((Object)("[DESIGN] Message de changement de bonus spam\u00e9 sur le mdc d'id=" + protectorId));
            return;
        }
        if (prevBuffs.size() == 0) {
            final ProtectorBuff buffAdded = ProtectorBuffManager.INSTANCE.getBuff(newBuffs.getQuick(0));
            final String buffDesc = ProtectorBuffView.getProtectorBuffs(buffAdded, false);
            if (buffDesc == null) {
                return;
            }
            final String title = WakfuTranslator.getInstance().getString("notification.protectorBuffAddedTitle");
            final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.protectorBuffAddedText", protectorName, buffDesc), NotificationMessageType.PROTECTOR_WEATHER);
        }
        else if (newBuffs.size() == 0) {
            final ProtectorBuff removedBuff = ProtectorBuffManager.INSTANCE.getBuff(prevBuffs.getQuick(0));
            final String buffDesc = ProtectorBuffView.getProtectorBuffs(removedBuff, false);
            if (buffDesc == null) {
                return;
            }
            final String title = WakfuTranslator.getInstance().getString("notification.protectorBuffRemovedTitle");
            final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.protectorBuffRemovedText", protectorName, buffDesc), NotificationMessageType.PROTECTOR_WEATHER);
        }
        else {
            final ProtectorBuff buffAdded = ProtectorBuffManager.INSTANCE.getBuff(newBuffs.getQuick(0));
            final ProtectorBuff removedBuff2 = ProtectorBuffManager.INSTANCE.getBuff(prevBuffs.getQuick(0));
            final String buffAddedDesc = ProtectorBuffView.getProtectorBuffs(buffAdded, false);
            final String buffRemovedDesc = ProtectorBuffView.getProtectorBuffs(removedBuff2, false);
            if (buffAddedDesc == null || buffRemovedDesc == null) {
                return;
            }
            final String title2 = WakfuTranslator.getInstance().getString("notification.protectorBuffUpdatedTitle");
            final String text2 = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.protectorBuffUpdatedText", protectorName, buffRemovedDesc, buffAddedDesc), NotificationMessageType.PROTECTOR_WEATHER);
        }
        this.m_prevBuffs.put(protectorId, newBuffs);
        this.m_prevBuffsStartTime.put(protectorId, System.currentTimeMillis());
    }
    
    public void clean() {
        if (this.m_prevBuffs != null) {
            this.m_prevBuffs.clear();
            this.m_prevBuffsStartTime.clear();
        }
    }
    
    static {
        ProtectorNationBuffEventHandler.INSTANCE = new ProtectorNationBuffEventHandler();
        m_logger = Logger.getLogger((Class)ProtectorNationBuffEventHandler.class);
    }
}
