package src.entidades;

public enum Prioridade { //nao sabia que existia tipo enum, mas gostei
    BAIXA(1), MEDIA(2), ALTA(3);

    private final int score;

    Prioridade(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

}
