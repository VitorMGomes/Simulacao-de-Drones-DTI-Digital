package src.principal;

import java.util.Arrays;
import java.util.List;

import src.entidades.*;
import src.estrategia.*;
import src.util.*;

public class Testes {
    public static void main(String[] args) {
        // Ative asserts com: java -ea ...
        Distancia d = new Euclidiana();

        // Teste 1: distância euclidiana 3-4-5
        assert Math.abs(d.entre(new Ponto(0, 0), new Ponto(3, 4)) - 5.0) < 1e-9 : "Distância 3-4-5 falhou";

        Deposito base = new Deposito();

        // Teste 2: capacidade faz quebrar em 2 viagens
        // Bateria grande para não interferir neste teste
        Bateria bat1 = new Bateria(10_000, 1, 0);
        Drone droneCap = new Drone(1, 5.0, 100.0, bat1);

        List<Pedido> psCap = Arrays.asList(
                new Pedido(1, new Ponto(1, 0), 3.0, Prioridade.MEDIA),
                new Pedido(2, new Ponto(2, 0), 3.0, Prioridade.MEDIA));

        var vsCap = new GreedyMinTrips().planejar(psCap, droneCap, base, d);
        assert vsCap.size() == 2 : "Deveria quebrar em 2 viagens por capacidade";

        // Teste 3: alcance curto força 2 viagens
        Bateria bat2 = new Bateria(10_000, 1, 0);
        Drone droneAlc = new Drone(2, 100.0, 6.0, bat2); // ida+volta até (3,0) = 6 km

        List<Pedido> psAlc = Arrays.asList(
                new Pedido(3, new Ponto(3, 0), 1.0, Prioridade.ALTA),
                new Pedido(4, new Ponto(3, 0), 1.0, Prioridade.ALTA));

        var vsAlc = new GreedyMinTrips().planejar(psAlc, droneAlc, base, d);
        assert vsAlc.size() == 2 : "Deveria quebrar em 2 viagens por alcance";

        // (Opcional) Teste 4: pedido impossível por bateria insuficiente
        // Bateria com pouca carga para não suportar 6 km (consumo = 5 Wh/km → 30 Wh
        // necessários; daremos só 20 Wh)
        try {
            Bateria bat3 = new Bateria(20, 5, 0); // capacidade=20 Wh, consumo=5 Wh/km
            Drone droneBat = new Drone(3, 100.0, 100.0, bat3);
            List<Pedido> psBat = Arrays.asList(
                    new Pedido(5, new Ponto(3, 0), 1.0, Prioridade.ALTA) // ida+volta = 6 km, precisa 30 Wh
            );
            new GreedyMinTrips().planejar(psBat, droneBat, base, d);
            assert false : "Deveria lançar exceção por bateria insuficiente";
        } catch (IllegalArgumentException expected) {
            // ok
        }

        System.out.println("Todos os testes passaram ✅");
    }
}
