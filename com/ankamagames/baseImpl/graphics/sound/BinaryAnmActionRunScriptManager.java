package com.ankamagames.baseImpl.graphics.sound;

import com.ankamagames.baseImpl.graphics.sound.binary.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import gnu.trove.*;

public class BinaryAnmActionRunScriptManager extends AnmActionRunScriptManager
{
    private final TLongObjectHashMap<AnimatedElementRunScriptData> m_data;
    private static final int VERSION = 1;
    
    public BinaryAnmActionRunScriptManager() {
        super();
        this.m_data = new TLongObjectHashMap<AnimatedElementRunScriptData>();
    }
    
    public void loadBinary(final String file) throws IOException {
        final ExtendedDataInputStream is = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(file));
        final int version = is.readInt();
        for (int numData = is.readInt(), i = 0; i < numData; ++i) {
            final long id = is.readLong();
            final int type = is.readInt();
            final AnimatedElementRunScriptData data = AnimatedElementRunScriptDataFactoryManager.INSTANCE.createData(type, is);
            this.m_data.put(id, data);
        }
        is.close();
    }
    
    public void saveBinary(final String file) throws IOException {
        final OutputBitStream os = new OutputBitStream(FileHelper.createFileOutputStream(file));
        os.writeInt(1);
        os.writeInt(this.m_data.size());
        final TLongObjectIterator<AnimatedElementRunScriptData> it = this.m_data.iterator();
        while (it.hasNext()) {
            it.advance();
            os.writeLong(it.key());
            os.writeInt(it.value().getType());
            it.value().save(os);
        }
        os.close();
    }
    
    public void putData(final long id, final AnimatedElementRunScriptData data) {
        this.m_data.put(id, data);
    }
    
    @Override
    protected void doPlayAnmAction(final AnimatedObject elem, final long actionId) {
        final AnimatedElementRunScriptData data = this.m_data.get(actionId);
        if (data == null) {
            return;
        }
        data.play(elem);
    }
}
