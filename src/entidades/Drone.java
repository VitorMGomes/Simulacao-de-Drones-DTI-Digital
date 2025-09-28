package src.entidades;

public class Drone {
    private final int id;
    private final double capacidade;
    private final double alcance;


    public Drone(int id, double capacidade, double alcance)
    {
        if(capacidade <= 0) throw new IllegalArgumentException("Valor impossivel para capacidade");
        if(alcance <= 0) throw new IllegalArgumentException("Valor impossivel para alcance");

        this.id = id;
        this.capacidade = capacidade;
        this.alcance = alcance;
    }

    public int getId() {
        return id;
    }

    public double getCapacidadeKg() {
        return capacidade;
    }

    public double getAlcanceKm() {
        return alcance;
    }


    public boolean suportaPeso(double pesoKg) {
        return pesoKg <= capacidade;
    }

    public boolean suportaRota(double distanciaKm) {
        return distanciaKm <= alcance;
    }


    @Override
    public String toString() {
        String resp = "ID = " + id
                    + " | Capacidade = " + capacidade + "kg"
                    + " | Alcance = " + alcance + "km";
        return resp;
    }
}
