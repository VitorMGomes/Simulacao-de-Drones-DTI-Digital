package src.entidades;

public class Deposito {
    private final Ponto localizacao;

    public Deposito() {
        this.localizacao = new Ponto(0, 0);
    }

    public Deposito(Ponto localizacao) { // construtor recebendo ponto
        if (localizacao == null)
            throw new IllegalArgumentException("localizacao nula");
        this.localizacao = localizacao;
    }

    public Deposito(int x, int y) { // recebendo coordenadas
        this(new Ponto(x, y));
    }

    public Ponto getLocalizacao() {
        return localizacao;
    }

    @Override
    public String toString() {
        String resp = "Depósito | Localização = " + localizacao;
        return resp;
    }

}
