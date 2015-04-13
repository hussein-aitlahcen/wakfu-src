package com.ankamagames.wakfu.common.game.nation.data;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public abstract class RawNationPart<R extends VersionableObject> extends NationPart
{
    @Override
    public final void serialize(final ByteBuffer buffer) {
        final R raw = this.createNewRaw();
        this.toRaw(raw);
        raw.serialize(buffer);
    }
    
    @Override
    public final void unSerialize(final ByteBuffer buffer, final int version) {
        final R raw = this.createNewRaw();
        raw.unserializeVersion(buffer, version);
        this.fromRaw(raw);
    }
    
    @Override
    public final int serializedSize() {
        final R raw = this.createNewRaw();
        this.toRaw(raw);
        return raw.serializedSize();
    }
    
    protected abstract R createNewRaw();
    
    protected abstract void toRaw(final R p0);
    
    protected abstract void fromRaw(final R p0);
}
