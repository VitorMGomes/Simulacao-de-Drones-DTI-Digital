package src.principal;

import java.util.Arrays;
import java.util.List;

import src.entidades.*;
import src.estrategia.*;
import src.util.*;


public class Testes {
    public static void main(String[] args) {
        // Distância básica
        Distancia d = new Euclidiana();
        assert Math.abs(d.entre(new Ponto(0,0), new Ponto(3,4)) - 5.0) < 1e-9;

        // Capacidade respeitada
        Deposito base = new Deposito();
        Drone drone = new Drone(1, 5.0, 100.0); // alcance alto pra não influenciar
        List<Pedido> ps = Arrays.asList(
            new Pedido(1, new Ponto(1,0), 3.0, Prioridade.MEDIA),
            new Pedido(2, new Ponto(2,0), 3.0, Prioridade.MEDIA)
        );
        List<Viagem> vs = new GreedyMinTrips().planejar(ps, drone, base, d);
        assert vs.size() == 2 : "deveria quebrar em 2 viagens por capacidade";

        // Alcance respeitado (força 2 viagens)
        Drone curto = new Drone(1, 100.0, 6.0); // muito peso, pouco alcance
        List<Pedido> ps2 = Arrays.asList(
            new Pedido(3, new Ponto(3,0), 1.0, Prioridade.ALTA), // ida+volta = 6
            new Pedido(4, new Ponto(3,0), 1.0, Prioridade.ALTA)  // somar dois estoura
        );
        List<Viagem> vs2 = new GreedyMinTrips().planejar(ps2, curto, base, d);
        assert vs2.size() == 2 : "deveria quebrar em 2 viagens por alcance";

        System.out.println("Todos os testes passaram ✅");
    }
}
