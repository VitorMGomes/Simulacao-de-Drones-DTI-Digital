package src.principal;

import java.util.Arrays;
import java.util.List;

import src.entidades.*;
import src.estrategia.*;
import src.relatorio.*;
import src.util.*;

/**
 * Cenários testados:
 * A) Pedido único que ZERA a bateria (recarga/min = 0 → permanece 0% após a viagem).
 * B) Uma viagem com várias paradas (sobra de bateria).
 * C) Bateria insuficiente (espera exceção).
 * D) Duas viagens forçadas por capacidade; bateria zera ao final (recarga/min = 0).
 * E) RECARGA ENTRE VIAGENS: plano que falharia sem recarga passa a funcionar (recarga/min > 0).
 * F) Alcance curto força 2 viagens (mas viáveis), usando recarga/min = 0 só pra não interferir.
 *
 * Observações:
 * - Distância Euclidiana; com pontos no eixo X, ida+volta até (K,0) = 2*K.
 * - Consumo padronizado em 10 Wh/km para facilitar contas mentais.
 * - A estratégia GreedyMinTrips recarrega ao final de CADA viagem; quando recarga/min = 0,
 *   a recarga automática não muda a carga (útil para “congelar” o estado final).
 */
public class Main {
    public static void main(String[] args) {
        Distancia dist = new Euclidiana();
        Deposito base = new Deposito(); // (0,0)
        AlocacaoEstrategia estrategia = new GreedyMinTrips();

        // ========= Caso A: ZERA a bateria em UMA viagem =========
        // Pedido em (5,0): rota total 10 km → 10 km * 10 Wh/km = 100 Wh.
        // Bateria 100 Wh, recarga 0 Wh/min → zera e permanece 0%.
        {
            titulo("CASO A: Pedido único que ZERA a bateria (recarga/min = 0)");
            Bateria batA = new Bateria(100, 10, 0);        // cap=100Wh, cons=10Wh/km, recarga=0
            Drone droneA = new Drone(1, 10.0, 15.0, batA); // alcance >= 10 km
            List<Pedido> pedidosA = Arrays.asList(
                new Pedido(1, new Ponto(5, 0), 2.0, Prioridade.ALTA)
            );

            List<Viagem> viagensA = estrategia.planejar(pedidosA, droneA, base, dist);
            Relatorio.imprimir(viagensA, droneA);
            Relatorio.imprimirMapaAscii(viagensA, base, 12, 5);
        }

        // ========= Caso B: Várias paradas na mesma viagem (sobra bateria) =========
        // Rota: (0,0)->(2,0)->(5,0)->(0,0) = 2 + 3 + 5 = 10 km → 100 Wh.
        // Bateria 120 Wh, recarga 0 → sobra 20 Wh ≈ 17%.
        {
            titulo("CASO B: Uma viagem com várias paradas (sobra de bateria, recarga/min = 0)");
            Bateria batB = new Bateria(120, 10, 0);
            Drone droneB = new Drone(2, 10.0, 20.0, batB);
            List<Pedido> pedidosB = Arrays.asList(
                new Pedido(2, new Ponto(2, 0), 1.5, Prioridade.MEDIA),
                new Pedido(3, new Ponto(5, 0), 2.0, Prioridade.ALTA)
            );

            List<Viagem> viagensB = estrategia.planejar(pedidosB, droneB, base, dist);
            Relatorio.imprimir(viagensB, droneB);
            Relatorio.imprimirMapaAscii(viagensB, base, 12, 5);
        }

        // ========= Caso C: Bateria insuficiente (deve lançar exceção) =========
        // Precisaria de 100 Wh para 10 km; bateria tem 80 Wh → erro.
        {
            titulo("CASO C: Bateria insuficiente (espera exceção)");
            try {
                Bateria batC = new Bateria(80, 10, 0);
                Drone droneC = new Drone(3, 10.0, 20.0, batC);
                List<Pedido> pedidosC = Arrays.asList(
                    new Pedido(4, new Ponto(5, 0), 1.0, Prioridade.ALTA)
                );

                List<Viagem> viagensC = estrategia.planejar(pedidosC, droneC, base, dist);
                System.out.println("ERRO: Esperava exceção por bateria insuficiente, mas o plano foi gerado:");
                Relatorio.imprimir(viagensC, droneC);
            } catch (IllegalArgumentException e) {
                System.out.println("OK (exceção capturada): " + e.getMessage());
            }
        }

        // ========= Caso D: Duas viagens por CAPACIDADE; bateria zera ao final =========
        // Dois pedidos em (5,0), cada um 3 kg; capacidade 5 kg → 2 viagens.
        // Cada viagem = 10 km → 100 Wh; bateria 200 Wh, recarga 0 → zera após a 2ª.
        {
            titulo("CASO D: Duas viagens (capacidade força split) e bateria zera ao final (recarga/min = 0)");
            Bateria batD = new Bateria(200, 10, 0);
            Drone droneD = new Drone(4, 5.0, 30.0, batD);
            List<Pedido> pedidosD = Arrays.asList(
                new Pedido(5, new Ponto(5, 0), 3.0, Prioridade.ALTA),
                new Pedido(6, new Ponto(5, 0), 3.0, Prioridade.MEDIA)
            );

            List<Viagem> viagensD = estrategia.planejar(pedidosD, droneD, base, dist);
            Relatorio.imprimir(viagensD, droneD);
            Relatorio.imprimirMapaAscii(viagensD, base, 12, 5);
        }

        // ========= Caso E: RECARGA ENTRE VIAGENS habilita o plano =========
        // Mesmo cenário de 2 viagens, mas bateria inicial 120 Wh (seria insuficiente para 2*100 Wh=200 Wh).
        // Com recarga/min alta (>0), cada retorno enche a bateria e o plano passa.
        {
            titulo("CASO E: Recarga automática ENTRE viagens habilita o plano");
            Bateria batE = new Bateria(120, 10, 60); // 120 Wh, cons 10, RECARGA 60 Wh/min
            Drone droneE = new Drone(5, 5.0, 30.0, batE);
            List<Pedido> pedidosE = Arrays.asList(
                new Pedido(7, new Ponto(5, 0), 3.0, Prioridade.ALTA),
                new Pedido(8, new Ponto(5, 0), 3.0, Prioridade.MEDIA)
            );

            List<Viagem> viagensE = estrategia.planejar(pedidosE, droneE, base, dist);
            Relatorio.imprimir(viagensE, droneE); // aqui a bateria deve terminar CHEIA (120 Wh) após recarga final
            Relatorio.imprimirMapaAscii(viagensE, base, 12, 5);
        }

        // ========= Caso F: Alcance curto força 2 viagens (viáveis) =========
        // Alcance 6 km; pedidos em (3,0) → ida+volta = 6 km (ok). Dois pedidos, 1 kg cada.
        // Bateria 10_000 Wh só pra não interferir; recarga/min = 0.
        {
            titulo("CASO F: Alcance curto força 2 viagens (cada uma 6 km)");
            Bateria batF = new Bateria(10_000, 1, 0);
            Drone droneF = new Drone(6, 100.0, 6.0, batF); // alcance 6 km
            List<Pedido> pedidosF = Arrays.asList(
                new Pedido(9,  new Ponto(3, 0), 1.0, Prioridade.ALTA),
                new Pedido(10, new Ponto(3, 0), 1.0, Prioridade.MEDIA)
            );

            List<Viagem> viagensF = estrategia.planejar(pedidosF, droneF, base, dist);
            Relatorio.imprimir(viagensF, droneF);
            Relatorio.imprimirMapaAscii(viagensF, base, 12, 5);
        }

        // ========= (Opcional) Caso G: Demonstração da fila de prioridade =========
        // Três pedidos a distâncias semelhantes; o de prioridade ALTA deve sair antes do MEDIA, depois BAIXA;
        // pesos maiores também têm preferência quando prioridade empata.
        {
            titulo("CASO G (opcional): Ordem por prioridade/peso/distância (fila de prioridade)");
            Bateria batG = new Bateria(500, 10, 0);
            Drone droneG = new Drone(7, 20.0, 100.0, batG);
            List<Pedido> pedidosG = Arrays.asList(
                new Pedido(11, new Ponto(2, 0), 5.0, Prioridade.MEDIA),
                new Pedido(12, new Ponto(3, 0), 4.0, Prioridade.ALTA),
                new Pedido(13, new Ponto(1, 0), 6.0, Prioridade.MEDIA),
                new Pedido(14, new Ponto(2, 0), 1.0, Prioridade.BAIXA)
            );

            List<Viagem> viagensG = estrategia.planejar(pedidosG, droneG, base, dist);
            Relatorio.imprimir(viagensG, droneG);
            Relatorio.imprimirMapaAscii(viagensG, base, 12, 5);
        }
    }

    private static void titulo(String s) {
        System.out.println("\n==================================================");
        System.out.println(s);
        System.out.println("==================================================");
    }
}
