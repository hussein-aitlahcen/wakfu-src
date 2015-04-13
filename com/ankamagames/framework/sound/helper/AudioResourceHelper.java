package com.ankamagames.framework.sound.helper;

import com.ankamagames.framework.sound.stream.*;
import java.io.*;

public interface AudioResourceHelper
{
    AudioStreamProvider fromId(long p0) throws IOException;
}
