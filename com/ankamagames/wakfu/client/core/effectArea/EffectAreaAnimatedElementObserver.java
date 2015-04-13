package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.effectArea.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;

class EffectAreaAnimatedElementObserver implements GraphicalAreaAnimatedElementObserver
{
    @Override
    public void onAnimatedElementChanged(final GraphicalArea area) {
        final AnimatedInteractiveElement animatedElement = area.getAnimatedElement();
        if (animatedElement == null) {
            return;
        }
        final AbstractEffectArea linkedArea = area.getLinkedArea();
        animatedElement.setStaticAnimationKey("AnimStatique");
        animatedElement.addSelectionChangedListener(new InteractiveElementSelectionChangeListener() {
            @Override
            public void selectionChanged(final AnimatedInteractiveElement object, final boolean selected) {
                if (selected) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    if (widget != null && widget != MasterRootContainer.getInstance()) {
                        return;
                    }
                    final UIShowOverHeadInfosMessage msg = new UIShowOverHeadInfosMessage(animatedElement, 0);
                    msg.addInfo(EffectAreaAnimatedElementObserver.this.getFormattedString(linkedArea), null);
                    Worker.getInstance().pushMessage(msg);
                }
                else {
                    UIOverHeadInfosFrame.getInstance().hideOverHead(animatedElement);
                }
                linkedArea.onSelectionChanged(selected);
            }
        });
        final AbstractUIMessage effectAreaMsg = new UIEffectAreaMessage(linkedArea);
        effectAreaMsg.setId(18106);
        effectAreaMsg.setBooleanValue(true);
        Worker.getInstance().pushMessage(effectAreaMsg);
    }
    
    protected String getFormattedString(final AbstractEffectArea area) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (area.getOwner() != null) {
            sb.append("[").append(((Citizen)area.getOwner()).getName()).append("] ");
        }
        sb.append(WakfuTranslator.getInstance().getString(6, (int)area.getBaseId(), new Object[0]));
        FighterCharacteristicType hp = FighterCharacteristicType.HP;
        String translationKey = "hp.var";
        if (area.getType() == EffectAreaType.BOMB.getTypeId()) {
            hp = FighterCharacteristicType.BOMB_COOLDOWN;
            translationKey = "remaining.turns";
        }
        else if (area.getType() == EffectAreaType.BARREL.getTypeId()) {
            hp = AbstractBarrelEffectArea.getHpCharac();
            translationKey = "hp.var.barrel";
        }
        else if (area.getType() == EffectAreaType.FECA_GLYPH.getTypeId()) {
            hp = FighterCharacteristicType.AREA_HP;
            translationKey = "hp.var.glyph";
        }
        else if (area.getType() == EffectAreaType.BEACON.getTypeId()) {
            hp = FighterCharacteristicType.AREA_HP;
            translationKey = "hp.var.beacon";
        }
        if (area.hasCharacteristic(hp)) {
            sb.append(" (");
            sb.append(WakfuTranslator.getInstance().getString(translationKey, area.getCharacteristicValue(hp)));
            sb.append(")");
        }
        final RunningEffectManager rem = area.getRunningEffectManager();
        if (rem != null) {
            final ArrayList<WakfuEffect> list = new ArrayList<WakfuEffect>();
            for (final RunningEffect re : rem) {
                if (re instanceof WakfuEffect) {
                    list.add((WakfuEffect)re);
                }
            }
            if (!rem.isEmpty()) {
                final ArrayList<String> effects = CastableDescriptionGenerator.generateDescription(new DummyEffectContainerWriter(list, (int)area.getBaseId(), (short)0, true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, 0));
                for (int i = 0, size = effects.size(); i < size; ++i) {
                    sb.newLine().append(effects.get(i));
                }
            }
        }
        return sb.finishAndToString();
    }
}
