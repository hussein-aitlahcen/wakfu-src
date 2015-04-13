package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public abstract class AbstractRawPersistantData implements VersionableObject
{
    public static AbstractRawPersistantData unserializeVirtual(final ByteBuffer buffer) {
        final byte type = buffer.get();
        AbstractRawPersistantData result = null;
        switch (type) {
            case 0: {
                result = new RawPersistantDataNone();
                break;
            }
            case 1: {
                result = new RawPersistantDataMerchantDisplay();
                break;
            }
            case 2: {
                result = new RawPersistantEquipableDummy();
                break;
            }
            case 3: {
                result = new RawPersistantBookcase();
                break;
            }
            case 4: {
                result = new RawPersistantKrosmozGameBoard();
                break;
            }
            case 5: {
                result = new RawPersistantKrosmozGameCollection();
                break;
            }
            case 6: {
                result = new RawPersistantStool();
                break;
            }
            case 7: {
                result = new RawPersistantLootChest();
                break;
            }
            case 8: {
                result = new RawPersistantGenericActivable();
                break;
            }
            default: {
                return null;
            }
        }
        if (!result.unserialize(buffer)) {
            return null;
        }
        return result;
    }
    
    public static AbstractRawPersistantData unserializeVirtualVersion(final ByteBuffer buffer, final int version) {
        final byte type = buffer.get();
        AbstractRawPersistantData result = null;
        switch (type) {
            case 0: {
                result = new RawPersistantDataNone();
                break;
            }
            case 1: {
                result = new RawPersistantDataMerchantDisplay();
                break;
            }
            case 2: {
                result = new RawPersistantEquipableDummy();
                break;
            }
            case 3: {
                result = new RawPersistantBookcase();
                break;
            }
            case 4: {
                result = new RawPersistantKrosmozGameBoard();
                break;
            }
            case 5: {
                result = new RawPersistantKrosmozGameCollection();
                break;
            }
            case 6: {
                result = new RawPersistantStool();
                break;
            }
            case 7: {
                result = new RawPersistantLootChest();
                break;
            }
            case 8: {
                result = new RawPersistantGenericActivable();
                break;
            }
            default: {
                return null;
            }
        }
        if (!result.unserializeVersion(buffer, version)) {
            return null;
        }
        return result;
    }
    
    public abstract byte getVirtualId();
    
    public abstract void internalToString(final StringBuilder p0, final String p1);
}
