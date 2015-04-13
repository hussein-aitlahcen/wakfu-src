package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.ui.protocol.message.popupInfos.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;

public class UIPopupFrame implements MessageFrame
{
    private static UIPopupFrame m_instance;
    private PopupElement m_popupDialog;
    private FieldProvider m_fieldProvider;
    private boolean m_cancelTimedPop;
    private String m_property;
    private static final String SPELL_COMPONENT = "spellDetailPopup";
    private static final String SKILL_COMPONENT = "skillDescription2";
    private static final String ITEM_DETAIL_COMPONENT = "itemDetailPopup";
    private static final String ITEM_SIMPLE_COMPONENT = "itemSimplePopup";
    private String m_lastDialog;
    
    public UIPopupFrame() {
        super();
        this.m_lastDialog = "";
    }
    
    public static UIPopupFrame getInstance() {
        return UIPopupFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            final ClockMessage clockMessage = (ClockMessage)message;
            MessageScheduler.getInstance().removeClock(clockMessage.getClockId());
            if (this.m_cancelTimedPop) {
                return false;
            }
            this.popup();
            return false;
        }
        else {
            switch (message.getId()) {
                case 19301: {
                    final UIShowPopupInfosMessage msg = (UIShowPopupInfosMessage)message;
                    boolean ok = false;
                    if (this.m_popupDialog == null || this.m_fieldProvider == null) {
                        MessageScheduler.getInstance().removeAllClocks(this);
                        return false;
                    }
                    if (this.m_fieldProvider instanceof SpellLevel && msg.getContent() instanceof SpellLevel) {
                        final SpellLevel spellLevel1 = (SpellLevel)this.m_fieldProvider;
                        final SpellLevel spellLevel2 = (SpellLevel)msg.getContent();
                        ok = (spellLevel1.getSpell().getId() == spellLevel2.getSpell().getId());
                    }
                    else {
                        ok = this.m_fieldProvider.equals(msg.getContent());
                    }
                    if (ok) {
                        this.m_cancelTimedPop = !XulorActions.closePopup(null, this.m_popupDialog);
                        PropertiesProvider.getInstance().removeProperty(this.m_property);
                    }
                    return false;
                }
                case 19300: {
                    final UIShowPopupInfosMessage msg = (UIShowPopupInfosMessage)message;
                    this.m_fieldProvider = msg.getContent();
                    if (this.m_fieldProvider == null) {
                        return false;
                    }
                    if (msg.getIntValue() > 0) {
                        this.m_cancelTimedPop = false;
                        MessageScheduler.getInstance().addClock(this, msg.getIntValue(), -1, 1);
                    }
                    else {
                        this.popup();
                    }
                    return false;
                }
                default: {
                    return true;
                }
            }
        }
    }
    
    private void popup() {
        this.setContent(this.m_fieldProvider);
        if (this.m_popupDialog != null) {
            this.m_popupDialog.show(this.m_popupDialog.getParentOfType(Widget.class));
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            Xulor.getInstance().putActionClass("wakfu.popupInfos", PopupInfosActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeActionClass("wakfu.popupInfos");
            this.m_lastDialog = null;
            this.m_fieldProvider = null;
            this.m_popupDialog = null;
        }
    }
    
    @Override
    public long getId() {
        return 10L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setContent(final FieldProvider fieldProvider) {
        String dialog = "";
        if (fieldProvider instanceof SpellLevel) {
            dialog = "spellDetailPopup";
            this.m_property = "describedSpell";
        }
        else if (fieldProvider instanceof Item || fieldProvider instanceof ReferenceItem || fieldProvider instanceof MerchantInventoryItem || fieldProvider instanceof ReferenceItemFieldProvider) {
            Item item = null;
            ReferenceItem refItem;
            if (fieldProvider instanceof Item) {
                refItem = (ReferenceItem)((Item)fieldProvider).getReferenceItem();
                item = (Item)fieldProvider;
            }
            else if (fieldProvider instanceof ReferenceItem) {
                refItem = (ReferenceItem)fieldProvider;
            }
            else if (fieldProvider instanceof MerchantInventoryItem) {
                refItem = (ReferenceItem)((MerchantInventoryItem)fieldProvider).getItem().getReferenceItem();
            }
            else {
                if (!(fieldProvider instanceof ReferenceItemFieldProvider)) {
                    return;
                }
                refItem = ((ReferenceItemFieldProvider)fieldProvider).getReferenceItem();
            }
            final ReferenceItemDisplayer refItemDisplayer = refItem.getReferenceItemDisplayer();
            final ArrayList<String> characteristicsDesc = refItemDisplayer.getCharacteristicsString();
            final ArrayList<String> effectsDesc = refItemDisplayer.getEffectsString();
            if (effectsDesc == null && characteristicsDesc == null) {
                dialog = "itemSimplePopup";
            }
            else {
                dialog = "itemDetailPopup";
            }
            this.m_property = "itemPopupDetail";
        }
        else if (fieldProvider instanceof Skill) {
            dialog = "skillDescription2";
        }
        if (this.m_property != null && !this.m_property.isEmpty() && fieldProvider != null && !fieldProvider.equals(PropertiesProvider.getInstance().getObjectProperty(this.m_property))) {
            PropertiesProvider.getInstance().setPropertyValue(this.m_property, fieldProvider);
        }
        if (!dialog.isEmpty() && !dialog.equals(this.m_lastDialog)) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldAndFightBarDialog");
            if (map != null) {
                this.m_popupDialog = (PopupElement)map.getElement(dialog);
            }
            this.m_lastDialog = dialog;
        }
    }
    
    static {
        UIPopupFrame.m_instance = new UIPopupFrame();
    }
}
