package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.ui.protocol.message.nation.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.xulor2.component.table.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.nation.*;

public class UINationFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UINationFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private String m_lastGovernmentMemberDetailDialogId;
    private ArrayList<String> m_openedGovernmentMemberDetail;
    private ArrayList<Long> m_changedLaws;
    
    public UINationFrame() {
        super();
        this.m_openedGovernmentMemberDetail = new ArrayList<String>();
        this.m_changedLaws = new ArrayList<Long>();
    }
    
    public static UINationFrame getInstance() {
        return UINationFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18403:
            case 18417:
            case 18418: {
                final UIMessage uiMessage = (UIMessage)message;
                final String speech = uiMessage.getStringValue();
                final NationSpeechModificationRequestMessage ngsmrqm = new NationSpeechModificationRequestMessage();
                ngsmrqm.setNewSpeech(speech);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(ngsmrqm);
                PropertiesProvider.getInstance().setPropertyValue("speechDirty", false);
                PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", true);
                return false;
            }
            case 18406: {
                final NationEconomyInformationRequestMessage neirqm = new NationEconomyInformationRequestMessage();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(neirqm);
                this.setNetEnable(false);
                return false;
            }
            case 18415: {
                final GovernorPopularityInformationRequestMessage gpirm = new GovernorPopularityInformationRequestMessage();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(gpirm);
                this.setNetEnable(false);
                return false;
            }
            case 18416: {
                final ChangeGovernorPopularityRequestMessage neirqm2 = new ChangeGovernorPopularityRequestMessage();
                neirqm2.setOpinionId(NationDisplayer.getInstance().getCurrentOpinionId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(neirqm2);
                this.setNetEnable(false);
                return false;
            }
            case 18407: {
                final UIMessage uiMessage = (UIMessage)message;
                final long rankId = uiMessage.getLongValue();
                final String name = uiMessage.getStringValue();
                final NationGovernmentNominationRequestMessage nationGovernmentNominationRequestMessage = new NationGovernmentNominationRequestMessage();
                nationGovernmentNominationRequestMessage.setId(rankId);
                nationGovernmentNominationRequestMessage.setName(name);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(nationGovernmentNominationRequestMessage);
                return false;
            }
            case 18409: {
                final UIMessage uiMessage = (UIMessage)message;
                final long rankId = uiMessage.getLongValue();
                final String dialogId = "governmentMemberDetailsDialog_" + rankId;
                if (Xulor.getInstance().isLoaded(dialogId)) {
                    this.openCloseGovernmentMemberDetails(null, rankId);
                }
                else {
                    this.openCloseGovernmentMemberDetails(NationDisplayer.getInstance().getNation().getGovernment().getMember(NationRank.getById(rankId)));
                }
                return false;
            }
            case 18408: {
                final UIMessage uiMessage = (UIMessage)message;
                final long rankId = uiMessage.getLongValue();
                final Nation nation = NationDisplayer.getInstance().getNation();
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                final NationRank rank = player.getCitizenComportment().getRank();
                if (rank == null) {
                    return false;
                }
                nation.requestGovernmentRevoke(rank, NationRank.getById(rankId), RevokeReason.BY_GOVERNMENT_MEMBER);
                return false;
            }
            case 18404: {
                final UIMessage uiMessage = (UIMessage)message;
                final short titleId = uiMessage.getShortValue();
                final NationGovernorTitleModificationRequestMessage ngtmrqm = new NationGovernorTitleModificationRequestMessage();
                ngtmrqm.setNewTitle(titleId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(ngtmrqm);
                return false;
            }
            case 18410: {
                final UIMessage uiMessage = (UIMessage)message;
                final long lawId = uiMessage.getLongValue();
                if (this.m_changedLaws.contains(lawId)) {
                    this.m_changedLaws.remove(lawId);
                }
                else {
                    this.m_changedLaws.add(lawId);
                }
                NationDisplayer.getInstance().updateLawsViewPoints();
                PropertiesProvider.getInstance().setPropertyValue("lawsDirty", !this.m_changedLaws.isEmpty());
                return false;
            }
            case 18412: {
                if (this.m_changedLaws.isEmpty()) {
                    return false;
                }
                final TLongHashSet laws = NationDisplayer.getInstance().getActivatedLaws();
                final NationLawsModificationRequestMessage nationLawsModificationRequestMessage = new NationLawsModificationRequestMessage();
                nationLawsModificationRequestMessage.setActivatedLaws(laws);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(nationLawsModificationRequestMessage);
                this.m_changedLaws.clear();
                PropertiesProvider.getInstance().setPropertyValue("lawsDirty", false);
                return false;
            }
            case 18411: {
                NationDisplayer.getInstance().cleanAllLaws();
                this.m_changedLaws.clear();
                NationDisplayer.getInstance().updateLawsViewPoints();
                PropertiesProvider.getInstance().setPropertyValue("lawsDirty", false);
                return false;
            }
            case 18413: {
                final UIChangeNationRelationShipMessage uiChangeNationRelationShipMessage = (UIChangeNationRelationShipMessage)message;
                final NationDiplomacyChangeRequestMessage nationDiplomacyChangeRequestMessage = new NationDiplomacyChangeRequestMessage();
                nationDiplomacyChangeRequestMessage.setNationId(uiChangeNationRelationShipMessage.getNationId());
                nationDiplomacyChangeRequestMessage.setAlignmentId(uiChangeNationRelationShipMessage.getAlignmentId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(nationDiplomacyChangeRequestMessage);
                this.setNetEnable(false);
                return false;
            }
            case 18414: {
                final UIMessage uiMessage = (UIMessage)message;
                final OtherNationLawsRequestMessage onlrm = new OtherNationLawsRequestMessage();
                onlrm.setNationId(uiMessage.getIntValue());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(onlrm);
                this.setNetEnable(false);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void setNetEnable(final boolean enable) {
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("nationLock", enable);
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
        model.putSorter("territories", new TableSorter(new Comparator<ProtectorInListView>() {
            @Override
            public int compare(final ProtectorInListView o1, final ProtectorInListView o2) {
                final String name = o1.getTerritoryName();
                final String name2 = o2.getTerritoryName();
                if (name == null || name2 == null) {
                    return 0;
                }
                return name.compareTo(name2);
            }
        }));
        model.putSorter("wills", new TableSorter(new Comparator<ProtectorInListView>() {
            @Override
            public int compare(final ProtectorInListView o1, final ProtectorInListView o2) {
                return o1.getTotalSatisfaction() - o2.getTotalSatisfaction();
            }
        }));
        model.putSorter("taxes", new TableSorter(new Comparator<ProtectorInListView>() {
            @Override
            public int compare(final ProtectorInListView o1, final ProtectorInListView o2) {
                return (int)(o1.getTaxValue() * 100.0f - o2.getTaxValue() * 100.0f);
            }
        }));
        model.putSorter("cash", new TableSorter(new Comparator<ProtectorInListView>() {
            @Override
            public int compare(final ProtectorInListView o1, final ProtectorInListView o2) {
                return o1.getCash() - o2.getCash();
            }
        }));
        return model;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNation().equals(Nation.VOID_NATION)) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("nationDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UINationFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("speechDirty", false);
            PropertiesProvider.getInstance().setPropertyValue("speechVisualisationMode", true);
            PropertiesProvider.getInstance().setPropertyValue("lawsDirty", false);
            if (PropertiesProvider.getInstance().getProperty("nationCurrentPageIndex") == null) {
                PropertiesProvider.getInstance().setPropertyValue("nationCurrentPageIndex", 0);
            }
            NetEnabledWidgetManager.INSTANCE.createGroup("nationLock");
            Xulor.getInstance().load("nationDialog", Dialogs.getDialogPath("nationDialog"), 32769L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("nationDialog");
            if (map != null) {
                final TableModel model = this.getTableModel();
                final Table guildTable = (Table)map.getElement("protectorTable");
                if (guildTable != null) {
                    guildTable.setTableModel(model);
                }
            }
            final NationGovernmentInformationRequestMessage ngirqm = new NationGovernmentInformationRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(ngirqm);
            Xulor.getInstance().putActionClass("wakfu.nation", NationDialogActions.class);
            WakfuSoundManager.getInstance().windowFadeIn();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.clean();
            NationDisplayer.getInstance().clearAllNation();
            PropertiesProvider.getInstance().removeProperty("speechDirty");
            PropertiesProvider.getInstance().removeProperty("speechVisualisationMode");
            PropertiesProvider.getInstance().removeProperty("nationCurrentPageIndex");
            PropertiesProvider.getInstance().removeProperty("lawsDirty");
            Xulor.getInstance().unload("nationDialog");
            NetEnabledWidgetManager.INSTANCE.destroyGroup("nationLock");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.nation");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    private void clean() {
        for (final String itemDetailId : this.m_openedGovernmentMemberDetail) {
            Xulor.getInstance().unload(itemDetailId);
        }
        this.m_openedGovernmentMemberDetail.clear();
        this.cleanLawChanges();
        this.m_lastGovernmentMemberDetailDialogId = null;
    }
    
    public void cleanLawChanges() {
        this.m_changedLaws.clear();
    }
    
    public void openCloseGovernmentMemberDetails(final GovernmentInfo governmentInfo) {
        this.openCloseGovernmentMemberDetails(governmentInfo, governmentInfo.getRank().getId());
    }
    
    public void openCloseGovernmentMemberDetails(final GovernmentInfo governmentInfo, final long rankId) {
        final String dialogPath = "governmentMemberDetailsDialog";
        final String dialogId = "governmentMemberDetailsDialog_" + rankId;
        if (!Xulor.getInstance().isLoaded(dialogId) && governmentInfo != null) {
            final String id = (this.m_lastGovernmentMemberDetailDialogId == null) ? "nationDialog" : this.m_lastGovernmentMemberDetailDialogId;
            Xulor.getInstance().loadAsMultiple(dialogId, Dialogs.getDialogPath("governmentMemberDetailsDialog"), id, "nationDialog", "governmentMemberDetailsDialog", 1L, (short)10000);
            this.m_lastGovernmentMemberDetailDialogId = dialogId;
            final GovernmentMemberDisplayer displayer = new GovernmentMemberDisplayer(governmentInfo, NationDisplayer.getInstance().getRank(governmentInfo.getRank().getId()));
            PropertiesProvider.getInstance().setLocalPropertyValue("governmentMemberDetails", displayer, dialogId);
            this.m_openedGovernmentMemberDetail.add(dialogId);
        }
        else {
            Xulor.getInstance().unload(dialogId);
            this.m_openedGovernmentMemberDetail.remove(dialogId);
            if (dialogId.equals(this.m_lastGovernmentMemberDetailDialogId)) {
                this.m_lastGovernmentMemberDetailDialogId = null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UINationFrame.class);
        UINationFrame.m_instance = new UINationFrame();
    }
}
