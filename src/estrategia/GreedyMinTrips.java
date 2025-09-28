package src.estrategia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import src.entidades.*;
import src.util.*;


public class GreedyMinTrips implements AlocacaoEstrategia {

    @Override
    public List<Viagem> planejar(List<Pedido> pedidos, Drone drone, Deposito base, Distancia distancia) {
        List<Pedido> ordenados = new ArrayList<>(pedidos);

        ordenados.sort(Comparator
            .comparingInt((Pedido p) -> p.getPrioridade().getScore()).reversed()
            .thenComparing((Pedido p) -> p.getPeso(), Comparator.reverseOrder())
            .thenComparingDouble((Pedido p) -> distancia.entre(base.getLocalizacao(), p.getPonto()))
        );

        RotaDireta rota = new RotaDireta(distancia);
        List<Viagem> viagens = new ArrayList<>();
        List<Pedido> atual = new ArrayList<>();
        double pesoAtual = 0.0;

        for (Pedido p : ordenados) {
            boolean cabePeso = (pesoAtual + p.getPeso()) <= drone.getCapacidadeKg();

            if (cabePeso) {
                // testa alcance se eu incluir p
                RotaDireta.PassoAtual passo = new RotaDireta.PassoAtual(atual);
                double distSeIncluir = rota.calcularCom(passo, p, base);
                boolean cabeAlcance = distSeIncluir <= drone.getAlcanceKm();

                if (cabeAlcance) {
                    atual.add(p);
                    pesoAtual += p.getPeso();
                    continue;
                }
            }

            // não coube (peso ou alcance) → fecha viagem atual, se houver
            if (!atual.isEmpty()) {
                double dist = rota.calcular(base, atual);
                viagens.add(new Viagem(drone.getId(), atual, dist));
                atual = new ArrayList<>();
                pesoAtual = 0.0;
            }

            // tenta abrir nova viagem com p (se ainda não coube por alcance/peso sozinha, rejeita)
            List<Pedido> soP = new ArrayList<>();
            soP.add(p);
            double distSoP = rota.calcular(base, soP);

            if (p.getPeso() <= drone.getCapacidadeKg() && distSoP <= drone.getAlcanceKm()) {
                atual.add(p);
                pesoAtual = p.getPeso();
            } else {
                // pedido impossível com as restrições → aqui você pode:
                // (a) ignorar e logar uma msg; (b) lançar exceção; (c) acumular numa lista de rejeitados.
                throw new IllegalArgumentException("Pedido " + p.getId() + " não cabe em nenhuma viagem (capacidade/alcance).");
            }
        }

        // fecha a última
        if (!atual.isEmpty()) {
            double dist = rota.calcular(base, atual);
            viagens.add(new Viagem(drone.getId(), atual, dist));
        }

        return viagens;
    }
}
