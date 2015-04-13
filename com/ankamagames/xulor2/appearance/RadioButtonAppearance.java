package com.ankamagames.xulor2.appearance;

import com.ankamagames.xulor2.component.*;
import gnu.trove.*;
import com.ankamagames.xulor2.nongraphical.*;

public class RadioButtonAppearance extends ToggleButtonAppearance
{
    public static final String TAG = "RadioButtonAppearance";
    
    @Override
    public String getTag() {
        return "RadioButtonAppearance";
    }
    
    @Override
    public boolean toggleButton() {
        if (this.m_checked) {
            return false;
        }
        final RadioGroup radioGroup = this.m_widget.getElementMap().getRadioGroup(((RadioButton)this.m_widget).getGroupId());
        if (radioGroup == null) {
            return false;
        }
        radioGroup.foreachRadioButton(new TObjectProcedure<RadioButton>() {
            @Override
            public boolean execute(final RadioButton radioButton) {
                final RadioButtonAppearance appearance = radioButton.getAppearance();
                if (appearance.isChecked() && appearance != RadioButtonAppearance.this) {
                    appearance.unselectButton();
                }
                return true;
            }
        });
        return super.toggleButton();
    }
    
    public void unselectButton() {
        if (this.m_checked) {
            super.toggleButton();
        }
    }
}
