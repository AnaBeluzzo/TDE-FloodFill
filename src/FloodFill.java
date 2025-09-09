import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import estruturas.Fila;
import estruturas.Pilha;
import estruturas.Lista;
import estruturas.TipoAlgoritmo;
import estruturas.Ponto;

/**
 * Classe responsável pelo algoritmo de Flood Fill com geração de frames
 */
public class FloodFill {
    
    private BufferedImage imagem;
    private int largura;
    private int altura;
    private Lista<BufferedImage> frames;
    private int intervaloPorFrame;
    private int contadorOperacoes;
    private TipoAlgoritmo tipoAlgoritmo;
    
    public FloodFill(String caminhoImagem) throws IOException {
        this(caminhoImagem, TipoAlgoritmo.FILA_BFS);
    }
    
    public FloodFill(String caminhoImagem, TipoAlgoritmo tipoAlgoritmo) throws IOException {
        this.imagem = ImageIO.read(new File(caminhoImagem));
        this.largura = imagem.getWidth();
        this.altura = imagem.getHeight();
        this.frames = new Lista<>();
        this.intervaloPorFrame = 100; // padrão: salvar a cada 100 operações
        this.contadorOperacoes = 0;
        this.tipoAlgoritmo = tipoAlgoritmo;
        
        // Adiciona o frame inicial
        salvarFrameAtual();
    }
    
    public void setIntervaloPorFrame(int intervalo) {
        this.intervaloPorFrame = intervalo;
    }
    
    public int getLargura() {
        return largura;
    }
    
    public int getAltura() {
        return altura;
    }
    
    public TipoAlgoritmo getTipoAlgoritmo() {
        return tipoAlgoritmo;
    }
    
    public void setTipoAlgoritmo(TipoAlgoritmo tipoAlgoritmo) {
        this.tipoAlgoritmo = tipoAlgoritmo;
    }
    
    /**
     * Executa o flood fill a partir de uma coordenada com uma cor específica
     */
    public void executarFloodFill(int x, int y, Color novaCor) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            throw new IllegalArgumentException("Coordenadas fora dos limites da imagem");
        }
        
        Color corOriginal = new Color(imagem.getRGB(x, y));
        
        // Se a cor original é igual à nova cor, não há necessidade de preencher
        if (corOriginal.equals(novaCor)) {
            return;
        }
        
        floodFillIterativo(x, y, corOriginal, novaCor, tipoAlgoritmo);
        
        // Salva o frame final
        salvarFrameAtual();
    }
    
    /**
     * Implementação iterativa do flood fill usando fila (BFS) ou pilha (DFS)
     */
    private void floodFillIterativo(int x, int y, Color corOriginal, Color novaCor, TipoAlgoritmo algoritmo) {
        if (algoritmo == TipoAlgoritmo.FILA_BFS) {
            floodFillComFila(x, y, corOriginal, novaCor);
        } else {
            floodFillComPilha(x, y, corOriginal, novaCor);
        }
    }
    
    /**
     * Implementação usando Fila (BFS - Breadth-First Search)
     * Preenche de forma mais uniforme, espalhando em todas as direções
     */
    private void floodFillComFila(int x, int y, Color corOriginal, Color novaCor) {
        Fila<Ponto> fila = new Fila<>();
        fila.enfileirar(new Ponto(x, y));
        
        while (!fila.estaVazia()) {
            Ponto ponto = fila.desenfileirar();
            int px = ponto.getX();
            int py = ponto.getY();
            
            // Verifica se o ponto está dentro dos limites
            if (px < 0 || px >= largura || py < 0 || py >= altura) {
                continue;
            }
            
            // Verifica se a cor atual é a cor original
            Color corAtual = new Color(imagem.getRGB(px, py));
            if (!corAtual.equals(corOriginal)) {
                continue;
            }
            
            // Pinta o pixel
            imagem.setRGB(px, py, novaCor.getRGB());
            contadorOperacoes++;
            
            // Verifica se deve salvar um frame
            if (contadorOperacoes % intervaloPorFrame == 0) {
                salvarFrameAtual();
            }
            
            // Adiciona os pontos adjacentes à fila
            fila.enfileirar(new Ponto(px + 1, py)); // direita
            fila.enfileirar(new Ponto(px - 1, py)); // esquerda
            fila.enfileirar(new Ponto(px, py + 1)); // baixo
            fila.enfileirar(new Ponto(px, py - 1)); // cima
        }
    }
    
    /**
     * Implementação usando Pilha (DFS - Depth-First Search)
     * Preenche seguindo um caminho mais profundo antes de explorar outras áreas
     */
    private void floodFillComPilha(int x, int y, Color corOriginal, Color novaCor) {
        Pilha<Ponto> pilha = new Pilha<>();
        pilha.empilhar(new Ponto(x, y));
        
        while (!pilha.estaVazia()) {
            Ponto ponto = pilha.desempilhar();
            int px = ponto.getX();
            int py = ponto.getY();
            
            // Verifica se o ponto está dentro dos limites
            if (px < 0 || px >= largura || py < 0 || py >= altura) {
                continue;
            }
            
            // Verifica se a cor atual é a cor original
            Color corAtual = new Color(imagem.getRGB(px, py));
            if (!corAtual.equals(corOriginal)) {
                continue;
            }
            
            // Pinta o pixel
            imagem.setRGB(px, py, novaCor.getRGB());
            contadorOperacoes++;
            
            // Verifica se deve salvar um frame
            if (contadorOperacoes % intervaloPorFrame == 0) {
                salvarFrameAtual();
            }
            
            // Adiciona os pontos adjacentes à pilha
            // Ordem inversa para manter consistência visual
            pilha.empilhar(new Ponto(px, py - 1)); // cima
            pilha.empilhar(new Ponto(px, py + 1)); // baixo
            pilha.empilhar(new Ponto(px - 1, py)); // esquerda
            pilha.empilhar(new Ponto(px + 1, py)); // direita
        }
    }
    
    /**
     * Salva um frame da imagem atual
     */
    private void salvarFrameAtual() {
        BufferedImage copia = new BufferedImage(largura, altura, imagem.getType());
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                copia.setRGB(x, y, imagem.getRGB(x, y));
            }
        }
        frames.adicionar(copia);
    }
    
    /**
     * Salva todos os frames como arquivos PNG na pasta frames
     */
    public void salvarFrames(String pastaFrames) throws IOException {
        // Limpa a pasta de frames
        File pasta = new File(pastaFrames);
        if (pasta.exists()) {
            File[] arquivos = pasta.listFiles();
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    if (arquivo.getName().endsWith(".png")) {
                        arquivo.delete();
                    }
                }
            }
        } else {
            pasta.mkdirs();
        }
        
        // Salva cada frame
        for (int i = 0; i < frames.getTamanho(); i++) {
            BufferedImage frame = frames.obter(i);
            String nomeArquivo = String.format("frame_%04d.png", i);
            File arquivo = new File(pasta, nomeArquivo);
            ImageIO.write(frame, "PNG", arquivo);
        }
        
        System.out.println("Salvos " + frames.getTamanho() + " frames na pasta: " + pastaFrames);
    }
    
    /**
     * Obtém a cor de um pixel em formato RGB
     */
    public Color getCorPixel(int x, int y) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            throw new IllegalArgumentException("Coordenadas fora dos limites da imagem");
        }
        return new Color(imagem.getRGB(x, y));
    }
    
    public int getNumeroFrames() {
        return frames.getTamanho();
    }
}
