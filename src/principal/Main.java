package src.principal;

import java.util.Arrays;
import java.util.List;

import src.entidades.*;
import src.estrategia.*;
import src.principal.*;
import src.relatorio.*;
import src.util.*;


public class Main {
    public static void main(String[] args) {
        Deposito base = new Deposito(); // (0,0)
        Drone drone = new Drone(1, 10.0, 25.0);
        Distancia dist = new Euclidiana();

        List<Pedido> pedidos = Arrays.asList(
            new Pedido(1, new Ponto(2, 1), 3.0, Prioridade.ALTA),
            new Pedido(2, new Ponto(5, 2), 2.0, Prioridade.BAIXA),
            new Pedido(3, new Ponto(3, 4), 2.5, Prioridade.MEDIA),
            new Pedido(4, new Ponto(6, 1), 4.0, Prioridade.ALTA),
            new Pedido(5, new Ponto(1, 2), 1.5, Prioridade.MEDIA)
        );

        AlocacaoEstrategia estrategia = new GreedyMinTrips();
        List<Viagem> viagens = estrategia.planejar(pedidos, drone, base, dist);

        // Relatório completo
        Relatorio.imprimir(viagens);

        // Mapa ASCII (grade 10x10, pode aumentar se quiser)
        Relatorio.imprimirMapaAscii(viagens, base, 10, 10);
    }
}
