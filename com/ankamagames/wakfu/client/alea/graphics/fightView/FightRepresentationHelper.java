package com.ankamagames.wakfu.client.alea.graphics.fightView;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.group.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import java.awt.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class FightRepresentationHelper
{
    private static final Logger m_logger;
    private static final Comparator<CharacterInfo> LEVEL_SORTER;
    private static final Comparator<CharacterInfo> NAME_SORTER;
    private static final Comparator<CharacterInfo> NATION_SORTER;
    private static final Comparator<CharacterInfo> BREED_SORTER;
    private static final Comparator<CharacterInfo> SORTER;
    
    private static int getNationId(final CharacterInfo info) {
        try {
            return info.getCitizenComportment().getNationId();
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    public static AnimatedInteractiveElement createRepresentation(final ExternalFightInfo fightInfo, final int teamId, final Point3 center) {
        final MRUableElement sword = createFightRepresentation(center, fightInfo, teamId);
        sword.addSelectionChangedListener(new InteractiveElementSelectionChangeListener() {
            @Override
            public void selectionChanged(final AnimatedInteractiveElement object, final boolean selected) {
                if (selected) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    if (widget != null && widget != MasterRootContainer.getInstance()) {
                        return;
                    }
                    MobileColorizeHelper.onHover(object);
                    final UIShowOverHeadInfosMessage msg = new UIShowOverHeadInfosMessage(object, 0);
                    if (getOverHeadInfos(fightInfo, sword.getFightLeader(), msg)) {
                        Worker.getInstance().pushMessage(msg);
                    }
                }
                else {
                    UIOverHeadInfosFrame.getInstance().hideOverHead(object);
                    MobileColorizeHelper.onLeave(object);
                }
            }
        });
        return sword;
    }
    
    private static MRUableElement createFightRepresentation(final Point3 center, final ExternalFightInfo fightInfo, final int teamId) {
        CharacterInfo leader = (teamId == 1) ? fightInfo.getAttackerCreator() : fightInfo.getDefenderCreator();
        if (leader == null) {
            final Iterator i$ = fightInfo.getFightersInTeam((byte)teamId).iterator();
            if (i$.hasNext()) {
                final CharacterInfo fighter = leader = i$.next();
            }
        }
        final MRUableElement sword = new MRUableElement(GUIDGenerator.getGUID(), center.getX(), center.getY(), center.getZ(), leader);
        final String fileName = "EpeeCombat.anm";
        sword.setGfxId("EpeeCombat");
        sword.setDirection((teamId == 1) ? Direction8.EAST : Direction8.WEST);
        sword.setVisualHeight((short)6);
        try {
            sword.load(WakfuConfiguration.getInstance().getString("ANMGUIPath") + "EpeeCombat.anm", true);
        }
        catch (Exception e) {
            FightRepresentationHelper.m_logger.error((Object)"", (Throwable)e);
        }
        final String animForNation = getAnimForNation(leader);
        sword.setAnimation(animForNation);
        if (sword.containsAnimation(animForNation)) {
            sword.setAnimation(animForNation);
        }
        else {
            sword.setAnimation("Anim-0");
        }
        return sword;
    }
    
    private static String getAnimForNation(final CharacterInfo leader) {
        if (leader instanceof NonPlayerCharacter) {
            return "Anim-Mob";
        }
        return "Anim-" + getNationId(leader);
    }
    
    private static boolean getOverHeadInfos(final ExternalFightInfo fightInfo, final CharacterInfo leader, final UIShowOverHeadInfosMessage msg) {
        try {
            final List<CharacterInfo> fightersInTeam = (List<CharacterInfo>)(List)fightInfo.getFightersInTeam(leader.getTeamId());
            Collections.sort(fightersInTeam, FightRepresentationHelper.SORTER);
            if (fightersInTeam.size() > 1) {
                int totalLevel = 0;
                for (final CharacterInfo fighter : fightersInTeam) {
                    totalLevel += fighter.getLevel();
                }
                CharacterActorSelectionChangeListener.setTotalLevel(msg, totalLevel);
            }
            final boolean extended = leader.getActor().displayExtendedInfos();
            CharacterActorSelectionChangeListener.setBreedIconUrl(msg, leader);
            for (final CharacterInfo fighter : fightersInTeam) {
                if (fighter instanceof NonPlayerCharacter) {
                    final NPCGroupInformation.NPCInformation info = NPCGroupInformation.getInfoFrom((NonPlayerCharacter)fighter);
                    CharacterActorSelectionChangeListener.selectNPC(extended, fighter, msg, info);
                }
                else {
                    if (!(fighter instanceof PlayerCharacter)) {
                        continue;
                    }
                    CharacterActorSelectionChangeListener.select(fighter.getActor());
                    final String overHeadInfos = CharacterActorSelectionChangeListener.getOverHeadInfos(fighter, extended);
                    msg.addInfo(overHeadInfos, CharacterActorSelectionChangeListener.getIconUrl(fighter));
                }
            }
            msg.addInfo(WakfuTranslator.getInstance().getString("desc.inFight"));
        }
        catch (Exception e) {
            FightRepresentationHelper.m_logger.error((Object)("probl\u00e8me de r\u00e9cup\u00e9ration d'info leader=" + leader + " fightInfo=" + fightInfo), (Throwable)e);
            return false;
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightRepresentationHelper.class);
        LEVEL_SORTER = new Comparator<CharacterInfo>() {
            @Override
            public int compare(final CharacterInfo o1, final CharacterInfo o2) {
                final int levelDiff = o2.getLevel() - o1.getLevel();
                if (levelDiff < 0) {
                    return -1;
                }
                if (levelDiff > 0) {
                    return 1;
                }
                return 0;
            }
        };
        NAME_SORTER = new Comparator<CharacterInfo>() {
            @Override
            public int compare(final CharacterInfo o1, final CharacterInfo o2) {
                return o2.getName().compareTo(o1.getName());
            }
        };
        NATION_SORTER = new Comparator<CharacterInfo>() {
            @Override
            public int compare(final CharacterInfo o1, final CharacterInfo o2) {
                final int nationDiff = getNationId(o2) - getNationId(o1);
                if (nationDiff < 0) {
                    return -1;
                }
                if (nationDiff > 0) {
                    return 1;
                }
                return 0;
            }
        };
        BREED_SORTER = new Comparator<CharacterInfo>() {
            @Override
            public int compare(final CharacterInfo o1, final CharacterInfo o2) {
                final int breedDiff = o2.getBreedId() - o1.getBreedId();
                if (breedDiff < 0) {
                    return -1;
                }
                if (breedDiff > 0) {
                    return 1;
                }
                return 0;
            }
        };
        SORTER = new Comparator<CharacterInfo>() {
            final Comparator[] sorters = { FightRepresentationHelper.LEVEL_SORTER, FightRepresentationHelper.NATION_SORTER, FightRepresentationHelper.NAME_SORTER };
            
            @Override
            public int compare(final CharacterInfo o1, final CharacterInfo o2) {
                int i;
                int result;
                for (i = 0, result = 0; result == 0 && i < this.sorters.length; result = this.sorters[i++].compare(o1, o2)) {}
                return result;
            }
        };
    }
    
    public static class MRUableElement extends AnimatedInteractiveElement implements MRUable
    {
        private final CharacterInfo m_fightLeader;
        
        private MRUableElement(final long id, final int x, final int y, final short z, final CharacterInfo leader) {
            super(id, x, y, z);
            this.m_fightLeader = leader;
        }
        
        public final CharacterInfo getFightLeader() {
            return this.m_fightLeader;
        }
        
        @Override
        public AbstractMRUAction[] getMRUActions() {
            if (this.m_fightLeader == null) {
                return AbstractMRUAction.EMPTY_ARRAY;
            }
            return this.m_fightLeader.getMRUActions();
        }
        
        @Override
        public boolean isMRUPositionable() {
            return true;
        }
        
        @Override
        public Point getMRUScreenPosition() {
            final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
            final Point2 p = IsoCameraFunc.getScreenPositionFromBottomLeft(scene, this.getWorldX(), this.getWorldY(), this.getAltitude() + this.getVisualHeight());
            return new Point((int)p.m_x, (int)p.m_y);
        }
        
        @Override
        public short getMRUHeight() {
            return (short)(this.getVisualHeight() * 10.0f);
        }
        
        @Override
        public boolean isVisible() {
            try {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                return localPlayer == null || localPlayer.getCurrentOrObservedFight() == null;
            }
            catch (Exception e) {
                return super.isVisible();
            }
        }
    }
}
