package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DialogBinaryData implements BinaryData
{
    protected int m_id;
    protected String m_criteria;
    protected Answer[] m_answers;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public Answer[] getAnswers() {
        return this.m_answers;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_criteria = null;
        this.m_answers = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_criteria = buffer.readUTF8().intern();
        final int answerCount = buffer.getInt();
        this.m_answers = new Answer[answerCount];
        for (int iAnswer = 0; iAnswer < answerCount; ++iAnswer) {
            (this.m_answers[iAnswer] = new Answer()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DIALOG.getId();
    }
    
    public static class Answer
    {
        protected int m_id;
        protected String m_criterion;
        protected int m_type;
        protected boolean m_clientOnly;
        
        public int getId() {
            return this.m_id;
        }
        
        public String getCriterion() {
            return this.m_criterion;
        }
        
        public int getType() {
            return this.m_type;
        }
        
        public boolean isClientOnly() {
            return this.m_clientOnly;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_criterion = buffer.readUTF8().intern();
            this.m_type = buffer.getInt();
            this.m_clientOnly = buffer.readBoolean();
        }
    }
}
