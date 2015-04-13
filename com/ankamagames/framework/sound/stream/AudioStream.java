package com.ankamagames.framework.sound.stream;

import java.nio.*;
import java.io.*;

public interface AudioStream
{
    boolean reinitialize();
    
    boolean initialize(AudioStreamProvider p0);
    
    String getDescription();
    
    int read(ByteBuffer p0, int p1) throws IOException;
    
    int getNumChannels();
    
    int getSampleRate();
    
    void setSwap(boolean p0);
    
    void reset();
    
    void close();
    
    int getDurationInMs();
    
    long rawTell();
    
    long pcmTell();
    
    float timeTell();
    
    int timeSeek(float p0);
    
    int pcmSeek(long p0);
    
    int rawSeek(long p0);
    
    int getCurrentBytesPerSample();
    
    int getWeight();
    
    void setWeight(int p0);
    
    int getRefCount();
    
    void addRefCount();
    
    void subRefCount();
    
    String getFileId();
}
