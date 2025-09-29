package src.entidades;

public class Bateria {
    private final double capacidadeWh;
    private double cargaWh;
    private final double consumoWhPorKm;
    private final double recargaWhPorMin;

    public Bateria(double capacidadeWh, double consumoWhPorKm, double recargaWhPorMin) {
        if (capacidadeWh <= 0 || consumoWhPorKm <= 0)
            throw new IllegalArgumentException("parametros invalidos"); //se capacidade for menor que 0, torna a viagem IMPOSSIVEL, e o consumo <= 0 também é impossivel
        this.capacidadeWh = capacidadeWh;
        this.cargaWh = capacidadeWh;
        this.consumoWhPorKm = consumoWhPorKm;
        this.recargaWhPorMin = recargaWhPorMin;
    }

    public int recarregarAteCheio() {
        double falta = capacidadeWh - cargaWh;
        if (falta <= 0)
            return 0;
        if (recargaWhPorMin <= 0) {
            return 0;
        }
        int minutos = (int) Math.ceil(falta / recargaWhPorMin);
        recarregarMinutos(minutos);
        return minutos;
    }

    public boolean suportaDistancia(double km) {
        return cargaWh >= km * consumoWhPorKm;
    }

    public void consumir(double km) { // quanto ela consome nesses km
        cargaWh = Math.max(0, cargaWh - km * consumoWhPorKm);
    }

    public void recarregarMinutos(int minutos) { // calcula quanto de energia a bateria receberia nessa quantidade de minutos
        cargaWh = Math.min(capacidadeWh, cargaWh + recargaWhPorMin * Math.max(0, minutos));
    }

    public double getCargaWh() {
        return cargaWh;
    }

    public double getCapacidadeWh() {
        return capacidadeWh;
    }
}
