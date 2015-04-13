package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.decorator.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.component.table.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.group.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.background.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;

public class UIGuildLadderFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIGuildLadderFrame m_instance;
    private ParticleDecorator m_particleDecorator;
    private ArrayList<ModulationColorClient> m_appL;
    private GuildLadderView m_guildLadderView;
    private GuildLadderSortingType m_sortingType;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIGuildLadderFrame getInstance() {
        return UIGuildLadderFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18220: {
                final short newIndex = (short)(this.m_guildLadderView.getCurrentIndex() - 1);
                this.m_guildLadderView.setCurrentIndex(newIndex);
                this.sendRequest(newIndex);
                return false;
            }
            case 18221: {
                final short newIndex = (short)(this.m_guildLadderView.getCurrentIndex() + 1);
                this.m_guildLadderView.setCurrentIndex(newIndex);
                this.sendRequest(newIndex);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private TableModel getTableModel() {
        final TableModel model = new TableModel();
        model.setChangeListener(new TableModel.TableModelChangeListener() {
            @Override
            public void onColumnSortChanged(final String columnId, final boolean direct) {
                if ("level".equals(columnId)) {
                    UIGuildLadderFrame.this.m_sortingType = (direct ? GuildLadderSortingType.LEVEL_CRESCENT : GuildLadderSortingType.LEVEL_DESCENDING);
                }
                else if ("guildPoints".equals(columnId)) {
                    UIGuildLadderFrame.this.m_sortingType = (direct ? GuildLadderSortingType.GUILD_POINTS_CRESCENT : GuildLadderSortingType.GUILD_POINTS_DESCENDING);
                }
                else if ("conquestPoints".equals(columnId)) {
                    UIGuildLadderFrame.this.m_sortingType = (direct ? GuildLadderSortingType.CONQUEST_POINTS_CRESCENT : GuildLadderSortingType.CONQUEST_POINTS_DESCENDING);
                }
                UIGuildLadderFrame.this.m_guildLadderView.setCurrentIndex((short)0);
                UIGuildLadderFrame.this.sendRequest(UIGuildLadderFrame.this.m_guildLadderView.getCurrentIndex());
            }
        });
        return model;
    }
    
    public void sendRequest(final short index) {
        final GuildLadderConsultRequest guildLadderConsultRequest = new GuildLadderConsultRequest();
        guildLadderConsultRequest.setIndex(index);
        guildLadderConsultRequest.setSortingType((byte)((this.m_sortingType == null) ? -1 : this.m_sortingType.getId()));
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(guildLadderConsultRequest);
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
            if (this.m_guildLadderView == null) {
                UIGuildLadderFrame.m_logger.error((Object)"Impossible de charger l'affichage du ladder de guilde, il manque les donn\u00e9es !");
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("guildLadderDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIGuildLadderFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("guildLadder", this.m_guildLadderView);
            PropertiesProvider.getInstance().setPropertyValue("dungeonLadderBackgroundImage", WakfuConfiguration.getInstance().getDisplayBackgroundBackgroundImage(BackgroundDisplayType.POSTER.getId()));
            Xulor.getInstance().load("guildLadderDialog", Dialogs.getDialogPath("guildLadderDialog"), 256L, (short)30000);
            this.fadeOut();
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("guildLadderDialog");
            if (map != null) {
                final TableModel model = this.getTableModel();
                final Table table = (Table)map.getElement("ladderTable");
                if (table != null) {
                    table.setTableModel(model);
                }
            }
            Xulor.getInstance().putActionClass("wakfu.guildLadder", GuildLadderDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600120L);
        }
    }
    
    private void fadeOut() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("guildLadderDialog");
        final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
        Widget w = (Widget)map.getElement("background");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("closeButton");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        if (w != null) {
            final Color c1 = new Color(Color.WHITE_ALPHA.get());
            final Color c2 = new Color(Color.WHITE.get());
            w.addTween(new ModulationColorListTween(c1, c2, mcc, 0, 500, 1, TweenFunction.PROGRESSIVE));
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_guildLadderView = null;
            this.m_sortingType = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("guildLadder");
            PropertiesProvider.getInstance().removeProperty("dungeonLadderBackgroundImage");
            Xulor.getInstance().unload("guildLadderDialog");
            Xulor.getInstance().removeActionClass("wakfu.guildLadder");
            WakfuSoundManager.getInstance().playGUISound(600121L);
        }
    }
    
    public void loadGuildLadder(final GuildLadderView guildLadderView) {
        this.m_guildLadderView = guildLadderView;
        WakfuGameEntity.getInstance().pushFrame(getInstance());
    }
    
    public GuildLadderView getGuildLadderView() {
        return this.m_guildLadderView;
    }
    
    public GuildLadderSortingType getSortingType() {
        return this.m_sortingType;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIGuildLadderFrame.class);
        UIGuildLadderFrame.m_instance = new UIGuildLadderFrame();
    }
}
