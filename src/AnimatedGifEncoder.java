import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Classe simples para codificar GIF animado
 * Baseada no formato GIF89a
 */
public class AnimatedGifEncoder {
    private BufferedOutputStream out;
    private int width, height;
    private int repeat = -1;
    private int delay = 0;
    private boolean started = false;
    private boolean firstFrame = true;
    private boolean sizeSet = false;
    
    public boolean start(String file) {
        boolean ok = true;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            writeString("GIF89a");
        } catch (IOException e) {
            ok = false;
        }
        return started = ok;
    }
    
    public boolean finish() {
        if (!started) return false;
        boolean ok = true;
        started = false;
        try {
            out.write(0x3b); // gif trailer
            out.flush();
            out.close();
        } catch (IOException e) {
            ok = false;
        }
        return ok;
    }
    
    public void setDelay(int ms) {
        delay = Math.round(ms / 10.0f);
    }
    
    public void setRepeat(int iter) {
        if (iter >= 0) {
            repeat = iter;
        }
    }
    
    public boolean addFrame(BufferedImage im) {
        if ((im == null) || !started) {
            return false;
        }
        boolean ok = true;
        try {
            if (!sizeSet) {
                setSize(im.getWidth(), im.getHeight());
            }
            writeGraphicCtrlExt();
            writeImageDesc();
            if (!firstFrame) {
                writePalette();
            }
            writePixels(im);
            firstFrame = false;
        } catch (IOException e) {
            ok = false;
        }
        return ok;
    }
    
    private void setSize(int w, int h) {
        if (started && !sizeSet) {
            width = w;
            height = h;
            if (width < 1) width = 320;
            if (height < 1) height = 240;
            sizeSet = true;
            writeHeader();
        }
    }
    
    private void writeHeader() {
        try {
            writeLSD();
            writePalette();
            if (repeat >= 0) {
                writeNetscapeExt();
            }
        } catch (IOException e) {
            // Handle error
        }
    }
    
    private void writeLSD() throws IOException {
        writeShort(width);
        writeShort(height);
        out.write((0x80 | // 1 : global color table flag = 1 (gct used)
                  0x70 | // 2-4 : color resolution = 7
                  0x00 | // 5 : gct sort flag = 0
                  0x07)); // 6-8 : gct size = 7 (256 colors)
        out.write(0); // background color index
        out.write(0); // pixel aspect ratio - assume 1:1
    }
    
    private void writePalette() throws IOException {
        // Escreve paleta de cores básica (256 cores)
        for (int i = 0; i < 256; i++) {
            out.write(i); // red
            out.write(i); // green  
            out.write(i); // blue
        }
    }
    
    private void writeGraphicCtrlExt() throws IOException {
        out.write(0x21); // extension introducer
        out.write(0xf9); // graphic control label
        out.write(4); // block size
        out.write(0); // packed fields
        writeShort(delay); // delay x 1/100 sec
        out.write(0); // transparent color index
        out.write(0); // block terminator
    }
    
    private void writeImageDesc() throws IOException {
        out.write(0x2c); // image separator
        writeShort(0); // image left
        writeShort(0); // image top
        writeShort(width); // image width
        writeShort(height); // image height
        out.write(0x00); // packed fields (no local color table)
    }
    
    private void writePixels(BufferedImage image) throws IOException {
        // Converte imagem para escala de cinza e escreve pixels
        out.write(8); // LZW minimum code size
        
        // Simplificação: converte para tons de cinza
        byte[] pixels = new byte[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(image.getRGB(x, y));
                int gray = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                pixels[y * width + x] = (byte) gray;
            }
        }
        
        // Compressão LZW simplificada
        writePixelsLZW(pixels);
        out.write(0); // block terminator
    }
    
    private void writePixelsLZW(byte[] pixels) throws IOException {
        // Implementação LZW muito simplificada
        int dataSize = pixels.length;
        int blockSize = Math.min(dataSize, 255);
        
        for (int i = 0; i < dataSize; i += blockSize) {
            int currentBlockSize = Math.min(blockSize, dataSize - i);
            out.write(currentBlockSize);
            out.write(pixels, i, currentBlockSize);
        }
    }
    
    private void writeNetscapeExt() throws IOException {
        out.write(0x21); // extension introducer
        out.write(0xff); // app extension label
        out.write(11); // block size
        writeString("NETSCAPE2.0");
        out.write(3); // sub-block size
        out.write(1); // loop sub-block id
        writeShort(repeat); // loop count (0 = forever)
        out.write(0); // block terminator
    }
    
    private void writeShort(int value) throws IOException {
        out.write(value & 0xff);
        out.write((value >> 8) & 0xff);
    }
    
    private void writeString(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            out.write((byte) s.charAt(i));
        }
    }
}
