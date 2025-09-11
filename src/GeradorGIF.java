import java.io.File;
import java.io.IOException;
import estruturas.Lista;

/**
 * Classe responsável por gerar GIF a partir dos frames PNG
 */
public class GeradorGIF {
    
    /**
     * Gera um GIF animado a partir dos arquivos PNG na pasta frames
     * Utiliza ImageMagick através de linha de comando
     */
    public static void gerarGIF(String pastaFrames, String nomeGIF, int delay) throws IOException, InterruptedException {
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
        ordenarListaPorNome(listaArquivos);
        
        // Tenta usar ImageMagick primeiro
        if (tentarImageMagick(pastaFrames, nomeGIF, delay)) {
            System.out.println("GIF gerado com sucesso usando ImageMagick: " + nomeGIF);
            return;
        }
        
        // Se ImageMagick não estiver disponível, tenta usar Java puro
        try {
            gerarGIFJavaPuro(listaArquivos, nomeGIF, delay);
            System.out.println("GIF gerado com sucesso usando Java: " + nomeGIF);
        } catch (Exception e) {
            System.err.println("Erro ao gerar GIF: " + e.getMessage());
            criarScriptBackup(nomeGIF, delay);
        }
    }
    
    /**
     * Tenta gerar GIF usando ImageMagick
     */
    private static boolean tentarImageMagick(String pastaFrames, String nomeGIF, int delay) {
        try {
            String comando = String.format("convert -delay %d -loop 0 %s/*.png %s", 
                                         delay/10, pastaFrames, nomeGIF);
            
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", comando);
            Process processo = pb.start();
            int resultado = processo.waitFor();
            
            return resultado == 0;
        } catch (Exception e) {
            // ImageMagick não está disponível
            return false;
        }
    }
    
    /**
     * Ordena a lista de arquivos por nome usando selection sort
     */
    private static void ordenarListaPorNome(Lista<File> lista) {
        int tamanho = lista.getTamanho();
        
        // Cria um array temporário para facilitar a ordenação
        File[] arquivos = new File[tamanho];
        for (int i = 0; i < tamanho; i++) {
            arquivos[i] = lista.obter(i);
        }
        
        // Selection sort no array
        for (int i = 0; i < tamanho - 1; i++) {
            int menorIndice = i;
            for (int j = i + 1; j < tamanho; j++) {
                if (arquivos[j].getName().compareTo(arquivos[menorIndice].getName()) < 0) {
                    menorIndice = j;
                }
            }
            // Troca os elementos
            if (menorIndice != i) {
                File temp = arquivos[i];
                arquivos[i] = arquivos[menorIndice];
                arquivos[menorIndice] = temp;
            }
        }
        
        // Reconstrói a lista com os elementos ordenados
        lista.limpar();
        for (int i = 0; i < tamanho; i++) {
            lista.adicionar(arquivos[i]);
        }
    }
    
    /**
     * Gera GIF usando Java puro com implementação simplificada
     */
    private static void gerarGIFJavaPuro(Lista<File> arquivos, String nomeGIF, int delay) throws IOException {
        System.out.println("ImageMagick não encontrado. Usando gerador Java simplificado...");
        
        // Usar o AnimatedGifEncoder
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        
        if (gifEncoder.start(nomeGIF)) {
            gifEncoder.setDelay(delay);
            gifEncoder.setRepeat(0); // 0 = loop infinito
            
            // Adiciona cada frame
            for (int i = 0; i < arquivos.getTamanho(); i++) {
                File arquivo = arquivos.obter(i);
                try {
                    java.awt.image.BufferedImage imagem = javax.imageio.ImageIO.read(arquivo);
                    gifEncoder.addFrame(imagem);
                    System.out.println("Adicionando frame: " + arquivo.getName());
                } catch (Exception e) {
                    System.err.println("Erro ao processar frame: " + arquivo.getName());
                }
            }
            
            gifEncoder.finish();
            System.out.println("GIF criado com sucesso: " + nomeGIF);
        } else {
            throw new IOException("Falha ao iniciar o encoder GIF");
        }
    }
    
    private static void criarScriptBackup(String nomeGIF, int delay) {
        System.out.println("Criando script de backup para gerar GIF...");
        
        try {
            java.io.PrintWriter script = new java.io.PrintWriter("gerar_gif.sh");
            script.println("#!/bin/bash");
            script.println("echo 'Instalando ImageMagick...'");
            script.println("if command -v brew >/dev/null 2>&1; then");
            script.println("    brew install imagemagick");
            script.println("elif command -v apt-get >/dev/null 2>&1; then");
            script.println("    sudo apt-get install imagemagick");
            script.println("else");
            script.println("    echo 'Por favor, instale ImageMagick manualmente'");
            script.println("    exit 1");
            script.println("fi");
            script.println("echo 'Gerando GIF...'");
            script.println("convert -delay " + (delay/10) + " -loop 0 frames/*.png " + nomeGIF);
            script.println("echo 'GIF gerado: " + nomeGIF + "'");
            script.close();
            
            new File("gerar_gif.sh").setExecutable(true);
            System.out.println("Script 'gerar_gif.sh' criado.");
            System.out.println("Execute: ./gerar_gif.sh");
        } catch (Exception e) {
            System.err.println("Erro ao criar script: " + e.getMessage());
        }
    }
}
