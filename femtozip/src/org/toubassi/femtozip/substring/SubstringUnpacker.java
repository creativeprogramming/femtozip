package org.toubassi.femtozip.substring;

import java.io.ByteArrayOutputStream;

public class SubstringUnpacker implements SubstringPacker.Consumer {
    private byte[] dictionary;
    private ByteOutput bytesOut = new ByteOutput();
    private byte[] unpackedBytes;
    
    public SubstringUnpacker(byte[] dictionary) {
        this.dictionary = dictionary == null ? new byte[0] : dictionary;
    }
    
    public void encodeLiteral(int aByte) {
        bytesOut.write(aByte);
    }
    
    public byte[] getUnpackedBytes() {
        if (unpackedBytes == null) {
            unpackedBytes = bytesOut.toByteArray();
            bytesOut = new ByteOutput();
        }
        return unpackedBytes;
    }

    public void encodeSubstring(int offset, int length) {
        int currentIndex = bytesOut.size();
        if (currentIndex + offset < 0) {
            int startDict = currentIndex + offset + dictionary.length;
            int endDict = startDict + length;
            int end = 0;
            
            if (endDict > dictionary.length) {
                end = endDict - dictionary.length;
                endDict = dictionary.length;
            }
            for (int i = startDict; i < endDict; i++) {
                bytesOut.write(dictionary[i]);
            }
            
            if (end > 0) {
                for (int i = 0; i < end; i++) {
                    bytesOut.write(bytesOut.get(i));
                }
            }
        }
        else {
            for (int i = currentIndex + offset, count = currentIndex + offset + length; i < count; i++) {
                bytesOut.write(bytesOut.get(i));
            }
        }
    }
    
    public void endEncoding() {
    }
    
    private static class ByteOutput extends ByteArrayOutputStream {
        public byte get(int i) {
            return buf[i];
        }
    }
}
