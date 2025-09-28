package src.estrategia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import src.entidades.Deposito;
import src.entidades.Drone;
import src.entidades.Pedido;
import src.entidades.Viagem;
import src.util.Distancia;
import src.util.RotaDireta;

public class GreedyMinTrips implements AlocacaoEstrategia {

    @Override
    public List<Viagem> planejar(List<Pedido> pedidos, Drone drone, Deposito base, Distancia distancia) {
        if (pedidos == null)
            throw new IllegalArgumentException("Lista de pedidos nula");
        if (drone == null)
            throw new IllegalArgumentException("Drone nulo");
        if (base == null)
            throw new IllegalArgumentException("Depósito nulo");
        if (distancia == null)
            throw new IllegalArgumentException("Métrica de distância nula");

        Comparator<Pedido> cmp = Comparator
                .comparingInt((Pedido p) -> p.getPrioridade().getScore()).reversed()
                .thenComparing((Pedido p) -> p.getPeso(), Comparator.reverseOrder())
                .thenComparingDouble((Pedido p) -> distancia.entre(base.getLocalizacao(), p.getPonto()));

        PriorityQueue<Pedido> fila = new PriorityQueue<>(cmp);
        fila.addAll(pedidos);

        RotaDireta rota = new RotaDireta(distancia);
        List<Viagem> viagens = new ArrayList<>();

        List<Pedido> atual = new ArrayList<>();
        double pesoAtual = 0.0;

        while (!fila.isEmpty()) {
            Pedido p = fila.poll();

            boolean cabePorPeso = (pesoAtual + p.getPeso()) <= drone.getCapacidadeKg();
            if (cabePorPeso) {

                double distSeIncluir = rota.calcularCom(new RotaDireta.PassoAtual(atual), p, base);

                boolean cabePorAlcance = drone.suportaRota(distSeIncluir);
                boolean cabePorBateria = drone.suportaRotaPorBateria(distSeIncluir);

                if (cabePorAlcance && cabePorBateria) {
                    atual.add(p);
                    pesoAtual += p.getPeso();
                    continue;
                }
            }

            if (!atual.isEmpty()) {
                double distViagem = rota.calcular(base, atual);
                drone.consumirBateria(distViagem);
                viagens.add(new Viagem(drone.getId(), new ArrayList<>(atual), distViagem));

                drone.recarregarTotalNaBase();

                atual.clear();
                pesoAtual = 0.0;
            }

            List<Pedido> somenteP = new ArrayList<>(1);
            somenteP.add(p);
            double distSoP = rota.calcular(base, somenteP);

            boolean cabeSozinhoPeso = p.getPeso() <= drone.getCapacidadeKg();
            boolean cabeSozinhoAlcance = drone.suportaRota(distSoP);
            boolean cabeSozinhoBateria = drone.suportaRotaPorBateria(distSoP);

            if (cabeSozinhoPeso && cabeSozinhoAlcance && cabeSozinhoBateria) {
                atual.add(p);
                pesoAtual = p.getPeso();
            } else {
                throw new IllegalArgumentException(
                        "Pedido " + p.getId() + " não cabe em nenhuma viagem (capacidade/alcance/bateria).");
            }
        }

        if (!atual.isEmpty()) {
            double distViagem = rota.calcular(base, atual);
            drone.consumirBateria(distViagem);
            viagens.add(new Viagem(drone.getId(), new ArrayList<>(atual), distViagem));

            drone.recarregarTotalNaBase();
        }

        return viagens;
    }
}
