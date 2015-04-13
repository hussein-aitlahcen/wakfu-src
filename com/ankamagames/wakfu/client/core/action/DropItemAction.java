package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class DropItemAction extends AbstractFightTimedAction
{
    private ArrayList<FloorItem> m_floorItems;
    private static long DROP_ANIMATION_DURATION;
    
    public DropItemAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final ArrayList<FloorItem> floorItems) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_floorItems = new ArrayList<FloorItem>();
        this.m_floorItems = floorItems;
    }
    
    @Override
    protected void onActionFinished() {
        for (int i = 0; i < this.m_floorItems.size(); ++i) {
            final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(15106);
            final FloorItemInteractiveElement floorItemIE = this.m_floorItems.get(i).getFloorItemInteractiveElement();
            system.setPosition(floorItemIE.getWorldCellX(), floorItemIE.getWorldCellY(), floorItemIE.getWorldCellAltitude());
            system.setFightId(this.getFightId());
            IsoParticleSystemManager.getInstance().addParticleSystem(system);
            this.m_floorItems.get(i).setVisible(true);
            this.m_floorItems.get(i).getFloorItemInteractiveElement().notifyViews();
        }
        super.onActionFinished();
    }
    
    public long onRun() {
        try {
            this.chatLog();
        }
        catch (Exception e) {
            DropItemAction.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        return DropItemAction.DROP_ANIMATION_DURATION;
    }
    
    private void chatLog() {
        final String objects = "";
        final TextWidgetFormater f = new TextWidgetFormater();
        for (int i = 0; i < this.m_floorItems.size(); ++i) {
            final FloorItem floorItem = this.m_floorItems.get(i);
            if (floorItem != null) {
                final Item item = floorItem.getItem();
                if (item != null) {
                    final CharacterInfo fighterDroper = this.getFighterById(floorItem.getInstigatorId());
                    final Point3 position = fighterDroper.getPosition();
                    if (fighterDroper != null && position != null) {
                        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(15105);
                        final FloorItemInteractiveElement floorItemIE = floorItem.getFloorItemInteractiveElement();
                        system.setPosition(position.getX(), position.getY(), position.getZ());
                        final ParabolicTween tween = new ParabolicTween(system, floorItemIE.getWorldCellX(), floorItemIE.getWorldCellY(), floorItemIE.getWorldCellAltitude(), 50.0, 1.6f);
                        system.setFightId(this.getFightId());
                        IsoParticleSystemManager.getInstance().addParticleSystem(system);
                        TweenManager.getInstance().addTween(tween);
                    }
                    else {
                        DropItemAction.m_logger.warn((Object)"figtherDroper null : probablement parce que c'\u00e9tait le dernier du combat et qu'il a \u00e9t\u00e9 despawn avant d'afficher l'aps");
                    }
                    if (i == 0) {
                        f.b().append(item.getName())._b();
                    }
                    else if (i == this.m_floorItems.size() - 1) {
                        f.append(" et ").b().append(item.getName())._b();
                    }
                    else {
                        f.append(", ").b().append(item.getName())._b();
                    }
                    final String icon = WakfuConfiguration.getInstance().getItemSmallIconUrl(item.getGfxId());
                    String lockNames = "";
                    for (final long ids : this.m_floorItems.get(i).getLock()) {
                        if (!lockNames.isEmpty()) {
                            lockNames += ", ";
                        }
                        if (CharacterInfoManager.getInstance().getCharacter(ids) != null) {
                            lockNames += CharacterInfoManager.getInstance().getCharacter(ids).getName();
                        }
                    }
                }
                else {
                    DropItemAction.m_logger.error((Object)("Pas d'Item associ\u00e9 au FloorItem \u00e0 la position " + i));
                }
            }
            else {
                DropItemAction.m_logger.error((Object)("Pas de FloorItem \u00e0 la position " + i));
            }
        }
        DropItemAction.m_fightLogger.info(new TextWidgetFormater().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append(WakfuTranslator.getInstance().getString("fight.drop", f.finishAndToString())).finishAndToString());
    }
    
    static {
        DropItemAction.DROP_ANIMATION_DURATION = 800L;
    }
}
