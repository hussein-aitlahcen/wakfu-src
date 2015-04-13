package com.ankamagames.wakfu.client.core.game.dialog;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.dialog.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class WakfuClientDialogView implements FieldProvider
{
    protected static final Logger m_logger;
    public static final String SPEAKER_ICON_URL_FIELD = "speakerIconUrl";
    public static final String SPEAKER_NAME_FIELD = "speakerName";
    public static final String CURRENT_TEXT_FIELD = "currentText";
    public static final String CURRENT_CHOICES_FIELD = "currentChoices";
    public static final String[] FIELDS;
    private Dialog m_dialog;
    private final DialogSourceType m_dialogSourceType;
    private final long m_sourceId;
    private final long m_refId;
    private ArrayList<WakfuClientDialogChoiceView> m_wakfuClientDialogChoiceView;
    private String[] m_currentQuestion;
    private static final int MAX_QUESTION_CHAR_NUMBER = 250;
    public static final int NEXT_MESSAGE_PART_CHOICE_ID = -2;
    public static final int END_DIALOG_CHOICE_ID = -1;
    private int m_currentQuestionIndex;
    private boolean m_insignificantAnswer;
    
    public WakfuClientDialogView(final Dialog dialog, final DialogSourceType dialogSourceType, final long sourceId) {
        super();
        this.m_currentQuestionIndex = 0;
        this.m_insignificantAnswer = false;
        this.m_dialogSourceType = dialogSourceType;
        this.m_sourceId = sourceId;
        if (dialogSourceType == DialogSourceType.NPC) {
            this.m_refId = CharacterInfoManager.getInstance().getCharacter(sourceId).getBreedId();
        }
        else {
            this.m_refId = this.m_sourceId;
        }
        this.setCurrentDialog(dialog);
    }
    
    @Override
    public String[] getFields() {
        return WakfuClientDialogView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("speakerIconUrl")) {
            return WakfuConfiguration.getInstance().getInteractiveDialogPortraitIconUrl(this.getFullId());
        }
        if (fieldName.equals("speakerName")) {
            switch (this.m_dialogSourceType) {
                case NPC: {
                    final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_sourceId);
                    return (info == null) ? null : info.getName();
                }
                case PROTECTOR: {
                    final Protector protector = ProtectorManager.INSTANCE.getProtector((int)this.m_sourceId);
                    return (protector == null) ? null : protector.getName();
                }
                case UNKNOWN: {
                    return null;
                }
            }
        }
        else {
            if (fieldName.equals("currentText")) {
                return (this.m_currentQuestion == null) ? "" : (this.m_currentQuestion[this.m_currentQuestionIndex] + (this.isCurrentQuestionEnd() ? "" : " [...]"));
            }
            if (fieldName.equals("currentChoices")) {
                return this.getCurrentChoices();
            }
        }
        return null;
    }
    
    public String getFullId() {
        return this.m_dialogSourceType.getIllustrationId(this.m_refId);
    }
    
    private void onSpeakerIconUrlLoaded(final Texture texture, final String message) {
        if (texture != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "speakerIconUrl");
        }
        else {
            WakfuClientDialogView.m_logger.warn((Object)message);
        }
    }
    
    private ArrayList<WakfuClientDialogChoiceView> getCurrentChoices() {
        if (this.m_wakfuClientDialogChoiceView != null) {
            return this.m_wakfuClientDialogChoiceView;
        }
        this.m_wakfuClientDialogChoiceView = new ArrayList<WakfuClientDialogChoiceView>();
        if (!this.isCurrentQuestionEnd()) {
            this.m_wakfuClientDialogChoiceView.add(new WakfuClientDialogChoiceView(-2, WakfuClientDialogChoiceType.NONE));
            this.m_insignificantAnswer = true;
            return this.m_wakfuClientDialogChoiceView;
        }
        if (this.m_dialog.getDialogChoiceSize() == 0) {
            this.m_insignificantAnswer = true;
            this.m_wakfuClientDialogChoiceView.add(new WakfuClientDialogChoiceView(-1, WakfuClientDialogChoiceType.NONE));
            return this.m_wakfuClientDialogChoiceView;
        }
        this.m_insignificantAnswer = false;
        final Iterator<DialogChoice> it = this.m_dialog.getDialogChoiceIterator();
        while (it.hasNext()) {
            final DialogChoice dialogChoice = it.next();
            if (dialogChoice.isValid(WakfuGameEntity.getInstance().getLocalPlayer(), this.getDialogSource())) {
                this.m_wakfuClientDialogChoiceView.add(new WakfuClientDialogChoiceView(dialogChoice.getId(), WakfuClientDialogChoiceType.getFromId(dialogChoice.getTypeId())));
            }
        }
        return this.m_wakfuClientDialogChoiceView;
    }
    
    private DialogSource getDialogSource() {
        switch (this.m_dialogSourceType) {
            case NPC: {
                return (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(this.m_sourceId);
            }
            case PROTECTOR: {
                return ((ProtectorManagerBase<DialogSource>)ProtectorManager.INSTANCE).getProtector((int)this.m_sourceId);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void setCurrentDialog(final Dialog dialog) {
        this.m_dialog = dialog;
        this.m_wakfuClientDialogChoiceView = null;
        this.m_currentQuestionIndex = 0;
        String text = WakfuTranslator.getInstance().getString(75, this.m_dialog.getId(), new Object[0]);
        final ArrayList<String> tmpSplittedText = new ArrayList<String>();
        while (text.length() > 250) {
            final String sub = text.substring(0, 250);
            final int endIndex = sub.lastIndexOf(" ");
            tmpSplittedText.add(text.substring(0, endIndex));
            text = text.substring(endIndex + 1);
        }
        tmpSplittedText.add(text);
        this.m_currentQuestion = new String[tmpSplittedText.size()];
        for (int i = 0; i < this.m_currentQuestion.length; ++i) {
            this.m_currentQuestion[i] = tmpSplittedText.get(i);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentText", "currentChoices");
    }
    
    public int getDialogId() {
        return this.m_dialog.getId();
    }
    
    public ArrayList<WakfuClientDialogChoiceView> getWakfuClientDialogChoiceView() {
        return this.m_wakfuClientDialogChoiceView;
    }
    
    public boolean isCurrentQuestionEnd() {
        return this.m_currentQuestionIndex == this.m_currentQuestion.length - 1;
    }
    
    public void setNextQuestionIndex() {
        ++this.m_currentQuestionIndex;
        if (this.isCurrentQuestionEnd()) {
            this.m_wakfuClientDialogChoiceView = null;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentText", "currentChoices");
    }
    
    public boolean isInsignificantAnswer() {
        return this.m_insignificantAnswer;
    }
    
    public void reset() {
        this.m_currentQuestion = null;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentText");
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuClientDialogView.class);
        FIELDS = new String[] { "speakerIconUrl", "speakerName", "currentText", "currentChoices" };
    }
}
