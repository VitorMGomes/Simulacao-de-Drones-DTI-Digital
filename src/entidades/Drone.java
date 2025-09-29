package src.entidades;

public class Drone {
    private final int id;
    private final double capacidade;
    private final double alcance;
    private final Bateria bateria;

    public Drone(int id, double capacidade, double alcance, Bateria bateria) {
        if (capacidade <= 0)
            throw new IllegalArgumentException("Capacidade (kg) deve ser > 0");
        if (alcance <= 0)
            throw new IllegalArgumentException("Alcance (km) deve ser > 0");
        if (bateria == null)
            throw new IllegalArgumentException("Bateria não pode ser nula");
        this.id = id;
        this.capacidade = capacidade;
        this.alcance = alcance;
        this.bateria = bateria;
    }

    //drone possui bateria, capacidade e alcance, portanto ele tera metodos que validem se eles conseguem suportar o peso pra viagem, se tem alcance, se tem bateria pra rota e controle da propria bateria

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

    public boolean suportaRotaPorBateria(double distanciaKm) {
        return bateria.suportaDistancia(distanciaKm);
    }

    public void consumirBateria(double distanciaKm) {
        bateria.consumir(distanciaKm);
    }

    public int recarregarTotalNaBase() {
        return bateria.recarregarAteCheio();
    }

    public Bateria getBateria() {
        return bateria;
    }

    @Override
    public String toString() {
        return "Drone #" + id +
                " | Capacidade=" + capacidade + "kg" +
                " | Alcance=" + alcance + "km" +
                " | Bateria=" + String.format("%.0f/%.0f Wh", bateria.getCargaWh(), bateria.getCapacidadeWh());
    }
}
