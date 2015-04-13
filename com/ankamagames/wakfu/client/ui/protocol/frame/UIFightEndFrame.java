package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.xulor2.component.table.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.death.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;

public class UIFightEndFrame implements MessageFrame
{
    private static UIFightEndFrame m_instance;
    private boolean m_isPvpResult;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIFightEndFrame getInstance() {
        return UIFightEndFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17730: {
                WakfuGameEntity.getInstance().removeFrame(this);
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
    
    private TableModel getTableModel() {
        final TableModel model = new TableModel();
        model.putSorter("name", new TableSorter(new Comparator<PlayerHistoryFieldProvider>() {
            @Override
            public int compare(final PlayerHistoryFieldProvider o1, final PlayerHistoryFieldProvider o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }));
        model.putSorter("level", new TableSorter(new Comparator<PlayerHistoryFieldProvider>() {
            @Override
            public int compare(final PlayerHistoryFieldProvider o1, final PlayerHistoryFieldProvider o2) {
                return o1.getLevel() - o2.getLevel();
            }
        }));
        model.putSorter("xp", new TableSorter(new Comparator<PlayerHistoryFieldProvider>() {
            @Override
            public int compare(final PlayerHistoryFieldProvider o1, final PlayerHistoryFieldProvider o2) {
                return (int)(o1.getXP() - o2.getXP());
            }
        }));
        model.putSorter("kamas", new TableSorter(new Comparator<PlayerHistoryFieldProvider>() {
            @Override
            public int compare(final PlayerHistoryFieldProvider o1, final PlayerHistoryFieldProvider o2) {
                return (int)(o1.getKamas() - o2.getKamas());
            }
        }));
        model.putSorter("kamasTax", new TableSorter(new Comparator<PlayerHistoryFieldProvider>() {
            @Override
            public int compare(final PlayerHistoryFieldProvider o1, final PlayerHistoryFieldProvider o2) {
                return (int)(o1.getCollectedKams() - o2.getCollectedKams());
            }
        }));
        return model;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals(UIFightEndFrame.this.getDialogId())) {
                        WakfuGameEntity.getInstance().removeFrame(UIFightEndFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final boolean fightPremium = SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.FIGHT_PREMIUM_DISPLAY);
            PropertiesProvider.getInstance().setPropertyValue("resultFightPremium", fightPremium);
            Xulor.getInstance().load(this.getDialogId(), Dialogs.getDialogPath(this.getDialogId()), 1L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.getDialogId());
            if (map != null) {
                final TableModel model = this.getTableModel();
                final Table winners = (Table)map.getElement("winnersTable");
                if (winners != null) {
                    winners.setTableModel(model);
                }
                final Table losers = (Table)map.getElement("losersTable");
                if (losers != null) {
                    losers.setTableModel(model);
                }
            }
            Xulor.getInstance().putActionClass("wakfu.fightResult", FightResultDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600068L);
        }
    }
    
    private String getDialogId() {
        if (this.m_isPvpResult) {
            return "pvpFightResultDialog";
        }
        return "fightResultDialog";
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().unload("fightResultSpellsDetailsDialog");
            Xulor.getInstance().unload("fightResultSummonsDetailsDialog");
            Xulor.getInstance().unload(this.getDialogId());
            final AbstractOccupation occupation = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
            if (occupation != null && occupation.getOccupationTypeId() == 4) {
                final DeadOccupation deadOccupation = (DeadOccupation)occupation;
                deadOccupation.displayMessageBox();
            }
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("resultFightPremium");
            PropertiesProvider.getInstance().removeProperty("fight.resultDescription");
            PropertiesProvider.getInstance().removeProperty("osamodasSymbiotCreatureCapturedState");
            FightHistoryFieldProvider.INSTANCE.clear();
            Xulor.getInstance().removeActionClass("wakfu.fightResult");
            WakfuSoundManager.getInstance().exitFight();
        }
    }
    
    public void loadSpellsDetailsDialog() {
        final String dialogId = Xulor.getInstance().isLoaded("fightResultSummonsDetailsDialog") ? "fightResultSummonsDetailsDialog" : this.getDialogId();
        Xulor.getInstance().loadAsMultiple("fightResultSpellsDetailsDialog", Dialogs.getDialogPath("fightResultSpellsDetailsDialog"), this.getDialogId(), this.getDialogId(), this.getDialogId(), 32769L, (short)10000);
    }
    
    public void loadSummonsDetailsDialog() {
        final String dialogId = Xulor.getInstance().isLoaded("fightResultSpellsDetailsDialog") ? "fightResultSpellsDetailsDialog" : this.getDialogId();
        Xulor.getInstance().loadAsMultiple("fightResultSummonsDetailsDialog", Dialogs.getDialogPath("fightResultSummonsDetailsDialog"), this.getDialogId(), this.getDialogId(), this.getDialogId(), 32769L, (short)10000);
    }
    
    public void setPvpResult(final boolean isPvpResult) {
        this.m_isPvpResult = isPvpResult;
    }
    
    static {
        UIFightEndFrame.m_instance = new UIFightEndFrame();
    }
}
