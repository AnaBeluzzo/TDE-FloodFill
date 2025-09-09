package estruturas;
/**
 * Classe que representa um ponto (coordenada x, y) para o algoritmo de flood fill
 */
public class Ponto {
    private int x;
    private int y;
    
    public Ponto(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Ponto ponto = (Ponto) obj;
        return x == ponto.x && y == ponto.y;
    }
    
    @Override
    public int hashCode() {
        return x * 31 + y;
    }
    
    @Override
    public String toString() {
        return "Ponto(" + x + ", " + y + ")";
    }
}
