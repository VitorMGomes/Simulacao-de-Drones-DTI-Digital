package src;

public class Ponto {
    private final int x;
    private final int y;


    public Ponto(int x, int y)
    {
        this.x = x;
        this.y = y;
    }// não farei construtor vazio pois dada um local é 100% necessaria as coordenadas

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public String toString() //recriando o método toString
    {
        String string = "(" + x + "," + y + ")";
        return string;
    }


    @Override
    public boolean equals(Object obj) //recriando o metodo equals
    {
        boolean resp = false;
        
        if (this == obj) // verifica se são o mesmo objeto (mesma instancia)
        {
            resp = true;
        }
        else if (obj instanceof Ponto) //verifica se é um objeto do tipo Ponto
        {
            Ponto ponto = (Ponto) obj;
            resp = (ponto.x == this.x) && (ponto.y == this.y);
        }

        return resp;
    }

    @Override
    public int hashCode()
    {
        return 31 * x + y;
    }

}
