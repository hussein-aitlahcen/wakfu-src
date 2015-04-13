package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

final class WallEffectAreaAnimatedElementObserver implements GraphicalAreaAnimatedElementObserver
{
    @Override
    public void onAnimatedElementChanged(final GraphicalArea area) {
        final AnimatedInteractiveElement animatedElement = area.getAnimatedElement();
        if (animatedElement == null) {
            return;
        }
        final AbstractEffectArea linkedArea = area.getLinkedArea();
        animatedElement.addSelectionChangedListener(new InteractiveElementSelectionChangeListener() {
            @Override
            public void selectionChanged(final AnimatedInteractiveElement object, final boolean selected) {
                if (selected) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    if (widget != null && widget != MasterRootContainer.getInstance()) {
                        return;
                    }
                    final EffectUser owner = linkedArea.getOwner();
                    final StringBuilder sb = new StringBuilder();
                    if (owner != null) {
                        sb.append(" [").append(((CharacterInfo)owner).getName()).append("] ");
                    }
                    sb.append(WakfuTranslator.getInstance().getString(6, (int)linkedArea.getBaseId(), new Object[0]));
                    final AbstractCharacteristic displayedCharacteristic = linkedArea.getDisplayedCharacteristic();
                    final CharacteristicType displayedType = displayedCharacteristic.getType();
                    if (displayedType == FighterCharacteristicType.AREA_HP || displayedType == FighterCharacteristicType.HP) {
                        final String translationKey = (displayedType == FighterCharacteristicType.AREA_HP) ? "hp.var.area" : "hp.var";
                        sb.append(" (").append(WakfuTranslator.getInstance().getString(translationKey, linkedArea.getCharacteristicValue(displayedType))).append(")");
                    }
                    final UIShowOverHeadInfosMessage msg = new UIShowOverHeadInfosMessage(animatedElement, 0);
                    msg.addInfo(sb.toString(), null);
                    Worker.getInstance().pushMessage(msg);
                }
                else {
                    UIOverHeadInfosFrame.getInstance().hideOverHead(animatedElement);
                }
            }
        });
    }
}
