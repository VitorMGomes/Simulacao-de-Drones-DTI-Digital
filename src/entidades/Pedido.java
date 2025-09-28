package src.entidades;

public class Pedido {
    private final int id;
    private final Ponto posicao;
    private final double peso;
    private final Prioridade prioridade;


    public Pedido(int id, Ponto posicao, double peso, Prioridade prioridade) 
    {
        this.id = id;

        if (posicao == null) throw new IllegalArgumentException("posicao nula");
        this.posicao = posicao;


        if (peso <= 0) throw new IllegalArgumentException("peso deve ser > 0");
        this.peso = peso;


        if (prioridade == null) throw new IllegalArgumentException("prioridade nula");
        this.prioridade = prioridade;
    }


    public int getId()
    {
        return id;
    }

    public Ponto getPonto()
    {
        return posicao;
    }

    public double getPeso()
    {
        return peso;
    }

    public Prioridade getPrioridade()
    {
        return prioridade;
    }
    

    @Override
    public String toString() {
        String resp = "ID = " + id
                    + " | Posição = " + posicao
                    + " | Peso = " + peso + "kg"
                    + " | Prioridade = " + prioridade;
        return resp;
    }

}


