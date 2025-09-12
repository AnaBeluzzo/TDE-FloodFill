package estruturas;

/**
 * Implementação de uma Lista
 * @param <T> Tipo dos elementos da lista
 */
public class Lista<T> {
    private Elemento<T> primeiro;
    private Elemento<T> ultimo;
    private int tamanho;
    
    public Lista() {
        this.primeiro = null;
        this.ultimo = null;
        this.tamanho = 0;
    }
    
    public void adicionar(T valor) {
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
    
    public T obter(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        
        Elemento<T> atual = primeiro;
        for (int i = 0; i < indice; i++) {
            atual = atual.getProximo();
        }
        
        return atual.getValor();
    }
    
    public boolean remover(T valor) {
        if (primeiro == null) {
            return false;
        }
        
        if (primeiro.getValor().equals(valor)) {
            primeiro = primeiro.getProximo();
            if (primeiro == null) {
                ultimo = null;
            }
            tamanho--;
            return true;
        }
        
        Elemento<T> atual = primeiro;
        while (atual.getProximo() != null) {
            if (atual.getProximo().getValor().equals(valor)) {
                Elemento<T> elementoRemover = atual.getProximo();
                atual.setProximo(elementoRemover.getProximo());
                if (elementoRemover == ultimo) {
                    ultimo = atual;
                }
                tamanho--;
                return true;
            }
            atual = atual.getProximo();
        }
        
        return false;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
    public boolean estaVazia() {
        return tamanho == 0;
    }
    
    public void limpar() {
        primeiro = null;
        ultimo = null;
        tamanho = 0;
    }
}
