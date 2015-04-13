package com.ankamagames.wakfu.client.ui.protocol.message.aptitude;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.component.*;

public class UIAptitudeCommonMessage extends UIMessage
{
    private Button m_validateButton;
    private Container m_buttonContainer;
    
    public void setValidateButton(final Button validateButton) {
        this.m_validateButton = validateButton;
    }
    
    public void setButtonContainer(final Container buttonContainer) {
        this.m_buttonContainer = buttonContainer;
    }
    
    public Button getValidateButton() {
        return this.m_validateButton;
    }
    
    public Container getButtonContainer() {
        return this.m_buttonContainer;
    }
}
