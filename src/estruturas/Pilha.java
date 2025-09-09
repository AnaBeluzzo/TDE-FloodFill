package estruturas;

/**
 * Implementação de uma Pilha (LIFO - Last In, First Out) genérica
 * @param <T> Tipo dos elementos da pilha
 */
public class Pilha<T> {
    private Elemento<T> topo;
    private int tamanho;
    
    public Pilha() {
        this.topo = null;
        this.tamanho = 0;
    }
    
    /**
     * Adiciona um elemento no topo da pilha (push)
     */
    public void empilhar(T valor) {
        Elemento<T> novoElemento = new Elemento<>(valor);
        novoElemento.setProximo(topo);
        topo = novoElemento;
        tamanho++;
    }
    
    /**
     * Remove e retorna o elemento do topo da pilha (pop)
     */
    public T desempilhar() {
        if (topo == null) {
            throw new RuntimeException("Pilha vazia");
        }
        
        T valor = topo.getValor();
        topo = topo.getProximo();
        tamanho--;
        return valor;
    }
    
    /**
     * Retorna o elemento do topo sem removê-lo
     */
    public T topo() {
        if (topo == null) {
            throw new RuntimeException("Pilha vazia");
        }
        return topo.getValor();
    }
    
    public boolean estaVazia() {
        return topo == null;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
    public void limpar() {
        topo = null;
        tamanho = 0;
    }
}
