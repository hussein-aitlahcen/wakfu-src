package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import com.ankamagames.wakfu.common.game.guild.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class KillMonstersInRadiusItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_radius;
    private int m_x;
    private int m_y;
    
    public KillMonstersInRadiusItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        if (params.length != 1) {
            KillMonstersInRadiusItemAction.m_logger.error((Object)"[LD] Mauvais nombre de param\u00e8tres sur une action d'item");
            return;
        }
        this.m_radius = Integer.parseInt(params[0]);
    }
    
    public int getRadius() {
        return this.m_radius;
    }
    
    @Override
    public boolean run(final Item item) {
        if (!this.checkUsability()) {
            return false;
        }
        final MouseReleasedListener mouseListener = new MouseReleasedListener() {
            @Override
            public boolean run(final Event event) {
                if (event == null || (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 3)) {
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
        if (item != null) {
            if (WakfuGameEntity.getInstance().hasFrame(UIKillMonsterInteractionFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIKillMonsterInteractionFrame.getInstance());
            }
            else {
                UIKillMonsterInteractionFrame.getInstance().setItem(item);
                WakfuGameEntity.getInstance().pushFrame(UIKillMonsterInteractionFrame.getInstance());
            }
        }
        return true;
    }
    
    private boolean checkUsability() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
        if (exchanger != null) {
            return false;
        }
        if (!isInOwnHavenWorld()) {
            ChatHelper.pushErrorMessage("error.notInOwnHavenWorld", new Object[0]);
            return false;
        }
        if (!this.hasRights()) {
            ChatHelper.pushErrorMessage("guild.rank.missing", WakfuTranslator.getInstance().getString(GuildRankAuthorisation.MANAGE_HAVEN_WORLD.name()));
            return false;
        }
        return true;
    }
    
    private static boolean isInOwnHavenWorld() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final short instanceId = localPlayer.getInstanceId();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final int havenWorldId = guildHandler.getHavenWorldId();
        final HavenWorldDefinition currentInstanceWorld = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId);
        if (currentInstanceWorld == null) {
            return false;
        }
        final HavenWorldDefinition ownWorld = HavenWorldDefinitionManager.INSTANCE.getWorld(havenWorldId);
        return ownWorld != null && ownWorld.getWorldInstanceId() == instanceId;
    }
    
    private boolean hasRights() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return GuildHelper.hasRights(localPlayer.getGuildHandler(), localPlayer.getGuildHandler().getMember(localPlayer.getId()), GuildRankAuthorisation.MANAGE_HAVEN_WORLD);
    }
    
    public void sendRequest(final Item item, final int x, final int y) {
        this.m_x = x;
        this.m_y = y;
        this.sendRequest(item.getUniqueId());
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putInt(this.m_x);
        buffer.putInt(this.m_y);
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 4 + 4;
    }
    
    @Override
    public void clear() {
        this.m_x = 0;
        this.m_y = 0;
        this.setCastPosition(null);
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.KILL_MONSTERS_IN_RADIUS;
    }
    
    static {
        m_logger = Logger.getLogger((Class)KillMonstersInRadiusItemAction.class);
    }
}
