package com.ankamagames.wakfu.client.ui.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.xulor2.component.table.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.group.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;

public class UIGuildManagementFrame extends MessageRunnerFrame
{
    protected static final Logger m_logger;
    private static final UIGuildManagementFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIGuildManagementFrame getInstance() {
        return UIGuildManagementFrame.m_instance;
    }
    
    private UIGuildManagementFrame() {
        super(new MessageRunner[] { new GuildAddMemberRunner(), new GuildApplyRankModificationRunner(), new GuildBuyBonusRunner(), new GuildCreateRankRunner(), new GuildDeleteRankRunner(), new GuildMoveRankRunner(), new GuildDisbandRunner(), new GuildDisplayDisconnectedMembersRunner(), new GuildEditDescriptionRunner(), new GuildEditMessageRunner(), new GuildEditPersonalDescriptionRunner(), new GuildIncrementLevelRunner(), new GuildLeaveRunner(), new GuildRankSwitchRequestRunner(), new GuildRemoveMemberRunner(), new GuildSelectSmileyRunner(), new GuildActivateBonusRunner(), new GuildChangeNationMessageRunner() });
    }
    
    private void addActionClasses() {
        Xulor.getInstance().putActionClass("wakfu.guildManagement", GuildManagementDialogActions.class);
        Xulor.getInstance().putActionClass("wakfu.guildImprovement", GuildImprovementDialogActions.class);
        Xulor.getInstance().putActionClass("wakfu.guildHavenWorld", GuildHavenWorldDialogActions.class);
    }
    
    private void removeActionClasses() {
        Xulor.getInstance().removeActionClass("wakfu.guildManagement");
        Xulor.getInstance().removeActionClass("wakfu.guildImprovement");
        Xulor.getInstance().removeActionClass("wakfu.guildHavenWorld");
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
        model.putSorter("rank", new TableSorter(new Comparator<WakfuGuildMemberView>() {
            @Override
            public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
                final GuildRankView rankView1 = WakfuGuildView.getInstance().getRankView(o1.getRank());
                final GuildRankView rankView2 = WakfuGuildView.getInstance().getRankView(o2.getRank());
                if (rankView2.getRank().getPosition() > rankView1.getRank().getPosition()) {
                    return 1;
                }
                if (rankView2.getRank().getPosition() < rankView1.getRank().getPosition()) {
                    return -1;
                }
                return rankView1.getName().compareTo(rankView2.getName());
            }
        }));
        model.putSorter("name", new TableSorter(new Comparator<WakfuGuildMemberView>() {
            @Override
            public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }));
        model.putSorter("level", new TableSorter(new Comparator<WakfuGuildMemberView>() {
            @Override
            public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
                return o1.getLevel() - o2.getLevel();
            }
        }));
        model.putSorter("guildPoints", new TableSorter(new Comparator<WakfuGuildMemberView>() {
            @Override
            public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
                if (o1.getGuildPoints() > o2.getGuildPoints()) {
                    return 1;
                }
                if (o1.getGuildPoints() < o2.getGuildPoints()) {
                    return -1;
                }
                return 0;
            }
        }));
        return model;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (!WakfuGameEntity.getInstance().getLocalPlayer().isInGuild()) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("guildManagementDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIGuildManagementFrame.getInstance());
                    }
                }
            };
            final boolean isGuildDisplayDisconnectedMembers = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.GUILD_DISPLAY_DISCONNECTED_MEMBERS_KEY);
            WakfuGuildView.getInstance().setDisplayDisconnectedMembers(isGuildDisplayDisconnectedMembers);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("guildManagementDialog", Dialogs.getDialogPath("guildManagementDialog"), 32769L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("guildManagementDialog");
            if (map != null) {
                final TableModel model = this.getTableModel();
                final Table guildTable = (Table)map.getElement("guildTable");
                if (guildTable != null) {
                    guildTable.setTableModel(model);
                }
            }
            WakfuGuildView.getInstance().initSelectedRank();
            this.addActionClasses();
            PropertiesProvider.getInstance().setPropertyValue("rankModificationDirty", false);
            PropertiesProvider.getInstance().setPropertyValue("draggedGuildRank", null);
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", 0, "guildManagementDialog");
            PropertiesProvider.getInstance().setPropertyValue("guild", WakfuGuildView.getInstance());
            final GuildHavenWorlInfoRequestMessage guildHavenWorlInfoRequestMessage = new GuildHavenWorlInfoRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(guildHavenWorlInfoRequestMessage);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventGuildPanelOpened());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final HavenWorldView havenWorldView = (HavenWorldView)PropertiesProvider.getInstance().getObjectProperty("havenWorld");
            if (havenWorldView != null) {
                havenWorldView.removeFromTimeManager();
            }
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("guildManagementDialog");
            this.removeActionClasses();
            PropertiesProvider.getInstance().removeProperty("guild");
            WakfuSoundManager.getInstance().playGUISound(600013L);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIGuildManagementFrame.class);
        m_instance = new UIGuildManagementFrame();
    }
}
