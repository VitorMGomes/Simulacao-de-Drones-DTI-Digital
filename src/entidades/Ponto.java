package src.entidades;

public class Ponto {
    private final int x;
    private final int y;

    public Ponto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        String string = "(" + x + "," + y + ")";
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resp = false;

        if (this == obj) {
            resp = true;
        } else if (obj instanceof Ponto) {
            Ponto ponto = (Ponto) obj;
            resp = (ponto.x == this.x) && (ponto.y == this.y);
        }

        return resp;
    }

    //cada Ponto com coordenadas diferentes tera um hash unico, pode haver colisao de hash. Nao entendi muito bem por que usar mas o gpt sugeriu e falou que poderia ser importante para comparar objetos iguais
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

}
