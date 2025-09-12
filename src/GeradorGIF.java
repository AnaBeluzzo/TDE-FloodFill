import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.util.Arrays;
import estruturas.Lista;

/**
 * Classe responsável por gerar GIF a partir dos frames PNG
 * Combina funcionalidades de geração de GIF 
 */
public class GeradorGIF {
    private String pastaFrames;
    private String nomeArquivoSaida;
    private int delay;
    private boolean loopInfinito;
    
    /**
     * Construtor padrão 
     */
    private GeradorGIF() {}
    
    /**
     * Construtor
     * 
     * @param pastaFrames Pasta contendo as imagens dos frames
     * @param nomeArquivoSaida Nome do arquivo GIF de saída
     * @param delay Delay entre frames em milissegundos
     * @param loopInfinito Se true, o GIF será executado em loop infinito
     */
    public GeradorGIF(String pastaFrames, String nomeArquivoSaida, int delay, boolean loopInfinito) {
        this.pastaFrames = pastaFrames;
        this.nomeArquivoSaida = nomeArquivoSaida;
        this.delay = delay;
        this.loopInfinito = loopInfinito;
    }
    
    /**
     * Método estático para compatibilidade com versão anterior
     * Gera um GIF animado a partir dos arquivos PNG na pasta frames
     * Utiliza apenas Java com ImageIO
     */
    public static void gerarGIF(String pastaFrames, String nomeGIF, int delay) throws IOException {
        File pasta = new File(pastaFrames);
        
        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new IOException("Pasta de frames não encontrada: " + pastaFrames);
        }
        
        // Lista todos os arquivos PNG da pasta
        File[] arquivos = pasta.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        
        if (arquivos == null || arquivos.length == 0) {
            throw new IOException("Nenhum arquivo PNG encontrado na pasta: " + pastaFrames);
        }
        
        // Converte array para Lista personalizada e ordena os arquivos por nome
        Lista<File> listaArquivos = new Lista<>();
        for (File arquivo : arquivos) {
            listaArquivos.adicionar(arquivo);
        }
        
        try {
            gerarGIFComImageIO(listaArquivos, nomeGIF, delay);
            System.out.println("GIF gerado com sucesso: " + nomeGIF);
            System.out.println("Total de frames: " + listaArquivos.getTamanho());
        } catch (Exception e) {
            System.err.println("Erro ao gerar GIF: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Falha na geração do GIF", e);
        }
    }
    
    /**
     * Método para gerar GIF
     * 
     * @throws IOException Se houver erro na leitura/escrita dos arquivos
     */
    public void gerarGifAnimado() throws IOException {
        File dir = new File(pastaFrames);
        File[] files = dir.listFiles((d, name) -> 
            name.toLowerCase().endsWith(".png")
        );

        if (files == null || files.length == 0) {
            throw new IOException("Nenhuma imagem encontrada na pasta: " + pastaFrames);
        }

        Arrays.sort(files);

        try {
            // lê primeira imagem
            BufferedImage primeira = ImageIO.read(files[0]);
            ImageOutputStream output = new FileImageOutputStream(new File(nomeArquivoSaida));

            GifSequenceWriter writer = new GifSequenceWriter(output, primeira.getType(), delay, loopInfinito);
            writer.writeToSequence(primeira);

            // escreve os outros frames
            for (int i = 1; i < files.length; i++) {
                BufferedImage img = ImageIO.read(files[i]);
                writer.writeToSequence(img);
            }

            writer.close();
            output.close();

            System.out.println("GIF gerado com sucesso: " + nomeArquivoSaida);
            System.out.println("Total de frames processados: " + files.length);
            
        } catch (Exception e) {
            throw new IOException("Erro ao gerar GIF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Método privado para compatibilidade com versão estática
     */
    private static void gerarGIFComImageIO(Lista<File> arquivos, String nomeGIF, int delay) throws IOException {
        if (arquivos.getTamanho() == 0) {
            throw new IOException("Nenhum arquivo para processar");
        }
        
        // Cria uma instância do GeradorGIF
        GeradorGIF gifGenerator = new GeradorGIF("frames", nomeGIF, delay, true);
        
        // Gera o GIF 
        gifGenerator.gerarGifAnimado();
    }
}
