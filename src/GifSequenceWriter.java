import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

/**
 * Classe para criar GIFs animados usando a API padrão do Java
 * Baseada na implementação do ImageIO
 */
public class GifSequenceWriter {
    private ImageWriter gifWriter;
    private ImageWriteParam imageWriteParam;
    private IIOMetadata imageMetaData;
    
    /**
     * Cria um novo GifSequenceWriter
     * 
     * @param outputStream Stream de saída para o GIF
     * @param imageType Tipo da imagem (ex: BufferedImage.TYPE_INT_RGB)
     * @param delayTime Delay entre frames em milissegundos
     * @param loopContinuously Se true, o GIF será executado em loop infinito
     */
    public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int delayTime, boolean loopContinuously) throws IOException {
        // Obtém o writer GIF
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("gif");
        if (!writers.hasNext()) {
            throw new IIOException("Nenhum writer GIF disponível");
        }
        gifWriter = writers.next();
        
        imageWriteParam = gifWriter.getDefaultWriteParam();
        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        
        imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
        
        String metaFormatName = imageMetaData.getNativeMetadataFormatName();
        
        IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
        
        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delayTime / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");
        
        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by GifSequenceWriter");
        
        IIOMetadataNode appEntensionsNode = getNode(root, "ApplicationExtensions");
        
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");
        
        int loop = loopContinuously ? 0 : 1;
        
        child.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
        appEntensionsNode.appendChild(child);
        
        imageMetaData.setFromTree(metaFormatName, root);
        
        gifWriter.setOutput(outputStream);
        
        gifWriter.prepareWriteSequence(null);
    }
    
    public void writeToSequence(RenderedImage img) throws IOException {
        gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
    }
    
    /**
     * Fecha o writer e finaliza a sequência
     */
    public void close() throws IOException {
        gifWriter.endWriteSequence();    
    }
    
    /**
     * Retorna um nó existente ou cria um novo se não existir
     */
    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return((IIOMetadataNode) rootNode.item(i));
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }
}
