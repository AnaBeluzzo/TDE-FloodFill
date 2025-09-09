package estruturas;

/**
 * Implementação de uma Fila (FIFO - First In, First Out) genérica
 * @param <T> Tipo dos elementos da fila
 */
public class Fila<T> {
    private Elemento<T> primeiro;
    private Elemento<T> ultimo;
    private int tamanho;
    
    public Fila() {
        this.primeiro = null;
        this.ultimo = null;
        this.tamanho = 0;
    }
    
    /**
     * Adiciona um elemento no final da fila (enqueue)
     */
    public void enfileirar(T valor) {
        Elemento<T> novoElemento = new Elemento<>(valor);
        
        if (primeiro == null) {
            primeiro = novoElemento;
            ultimo = novoElemento;
        } else {
            ultimo.setProximo(novoElemento);
            ultimo = novoElemento;
        }
        tamanho++;
    }
    
    /**
     * Remove e retorna o primeiro elemento da fila (dequeue)
     */
    public T desenfileirar() {
        if (primeiro == null) {
            throw new RuntimeException("Fila vazia");
        }
        
        T valor = primeiro.getValor();
        primeiro = primeiro.getProximo();
        
        if (primeiro == null) {
            ultimo = null;
        }
        
        tamanho--;
        return valor;
    }
    
    /**
     * Retorna o primeiro elemento sem removê-lo
     */
    public T primeiro() {
        if (primeiro == null) {
            throw new RuntimeException("Fila vazia");
        }
        return primeiro.getValor();
    }
    
    public boolean estaVazia() {
        return primeiro == null;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
    public void limpar() {
        primeiro = null;
        ultimo = null;
        tamanho = 0;
    }
}
