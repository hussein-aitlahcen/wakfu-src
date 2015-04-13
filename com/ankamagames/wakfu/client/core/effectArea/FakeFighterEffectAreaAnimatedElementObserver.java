package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;

class FakeFighterEffectAreaAnimatedElementObserver extends EffectAreaAnimatedElementObserver
{
    @Override
    protected String getFormattedString(final AbstractEffectArea area) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final EffectUser owner = area.getOwner();
        if (owner instanceof BasicCharacterInfo) {
            sb.append("[").append(((CharacterInfo)owner).getName()).append("] ");
        }
        sb.append(WakfuTranslator.getInstance().getString(6, (int)area.getBaseId(), new Object[0]));
        if (area instanceof FakeFighterEffectArea) {
            final FakeFighterEffectArea fakeFighterEffectArea = (FakeFighterEffectArea)area;
            if (area.hasCharacteristic(FighterCharacteristicType.HP)) {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("hp.var", area.getCharacteristicValue(FighterCharacteristicType.HP))).append(")");
            }
            if (area.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
                sb.append(" (").append(WakfuTranslator.getInstance().getString("hp.var.area", area.getCharacteristicValue(FighterCharacteristicType.AREA_HP))).append(")");
            }
            switch (fakeFighterEffectArea.getUserDefinedId()) {
                case 1: {
                    if (area.hasCharacteristic(FighterCharacteristicType.AP)) {
                        final int ap = area.getCharacteristicValue(FighterCharacteristicType.AP);
                        if (ap > 0) {
                            sb.append("\n . ").append(WakfuTranslator.getInstance().getString("xelors.dial.ap.var", ap));
                        }
                    }
                    if (area.hasCharacteristic(FighterCharacteristicType.INIT)) {
                        final int init = area.getCharacteristicValue(FighterCharacteristicType.INIT);
                        if (init > 0) {
                            sb.append("\n . ").append(WakfuTranslator.getInstance().getString("xelors.dial.init.var", init));
                        }
                        break;
                    }
                    break;
                }
                case 3: {
                    final RunningEffectManager rem = area.getRunningEffectManager();
                    for (final RunningEffect re : rem) {
                        if (re.getGenericEffect().getActionId() == RunningEffectConstants.VOODOOL_SPLIT_EFFECT.getId()) {
                            final VoodoolSplitEffect voodoolSplitEffect = (VoodoolSplitEffect)re;
                            final EffectUser effectUser = voodoolSplitEffect.getTarget();
                            if (!(effectUser instanceof CharacterInfo)) {
                                continue;
                            }
                            final CharacterInfo target = (CharacterInfo)effectUser;
                            sb.newLine().append(WakfuTranslator.getInstance().getString("desc.target")).append(" : ").append(target.getName());
                            if (target.hasCharacteristic(FighterCharacteristicType.HP)) {
                                sb.append(" (");
                                sb.append(WakfuTranslator.getInstance().getString("hp.var", target.getCharacteristicValue(FighterCharacteristicType.HP) + target.getCharacteristicValue(FighterCharacteristicType.VIRTUAL_HP)));
                                sb.append(")");
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 4: {
                    sb.append(" (");
                    if (fakeFighterEffectArea.hasState(1467L)) {
                        sb.append(WakfuTranslator.getInstance().getString("microbot.is.activated"));
                    }
                    else {
                        sb.append(WakfuTranslator.getInstance().getString("microbot.is.not.activated"));
                    }
                    sb.append(')');
                    break;
                }
            }
        }
        final RunningEffectManager rem2 = area.getRunningEffectManager();
        if (rem2 != null) {
            final ArrayList<WakfuEffect> list = new ArrayList<WakfuEffect>();
            for (final RunningEffect re : rem2) {
                if (re instanceof WakfuEffect) {
                    list.add((WakfuEffect)re);
                }
            }
            if (!rem2.isEmpty()) {
                final ArrayList<String> effects = CastableDescriptionGenerator.generateDescription(new DummyEffectContainerWriter(list, (int)area.getBaseId(), (short)0, true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, 0));
                for (int i = 0, size = effects.size(); i < size; ++i) {
                    sb.newLine().append(effects.get(i));
                }
            }
        }
        return sb.finishAndToString();
    }
}
