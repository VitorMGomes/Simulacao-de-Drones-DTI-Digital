package src;

public class Euclidiana implements Distancia {
    @Override
    public double entre(Ponto a, Ponto b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
