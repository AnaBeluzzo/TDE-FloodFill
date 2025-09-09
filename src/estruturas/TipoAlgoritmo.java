package estruturas;

/**
 * Enum para tipos de algoritmo de flood fill
 */
public enum TipoAlgoritmo {
    FILA_BFS("Fila (BFS - Breadth-First Search)"),
    PILHA_DFS("Pilha (DFS - Depth-First Search)");
    
    private final String descricao;
    
    TipoAlgoritmo(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
