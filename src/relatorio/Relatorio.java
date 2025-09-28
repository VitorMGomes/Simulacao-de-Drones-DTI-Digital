package src.relatorio;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.entidades.*;

public class Relatorio {

    public static void imprimir(List<Viagem> viagens, Drone drone) {
        System.out.println("===== RELATÓRIO =====");

        for (Viagem v : viagens) {
            System.out.println(v);
        }

        double distanciaTotal = somaDistancia(viagens);
        double pesoTotal = somaPeso(viagens);
        int totalPedidos = viagens.stream().mapToInt(v -> v.getPedidos().size()).sum();
        int totalViagens = viagens.size();
        String mediaDist = totalViagens == 0 ? "0.00" : String.format("%.2f", distanciaTotal / totalViagens);

        System.out.println("----- Resumo -----");
        System.out.println("Total de viagens: " + totalViagens);
        System.out.println("Total de pedidos: " + totalPedidos);
        System.out.println("Distância total: " + String.format("%.2f", distanciaTotal) + " km");
        System.out.println("Distância média por viagem: " + mediaDist + " km");
        System.out.println("Peso total transportado: " + String.format("%.2f", pesoTotal) + " kg");

        if (drone != null && drone.getBateria() != null) {
            double pct = 100.0 * drone.getBateria().getCargaWh() / drone.getBateria().getCapacidadeWh();
            System.out.println("Bateria restante do Drone #" + drone.getId() + ": " + String.format("%.0f%%", pct));
        }

        Viagem maisLonga = viagens.stream()
                .max(Comparator.comparingDouble(Viagem::getDistanciaKm))
                .orElse(null);

        Viagem maisPedidos = viagens.stream()
                .max(Comparator.comparingInt(v -> v.getPedidos().size()))
                .orElse(null);

        if (maisLonga != null) {
            System.out.println("Viagem mais longa: Drone #" + maisLonga.getDroneId() +
                    " (" + String.format("%.2f", maisLonga.getDistanciaKm()) + " km)");
        }
        if (maisPedidos != null) {
            System.out.println("Viagem com mais pedidos: Drone #" + maisPedidos.getDroneId() +
                    " (" + maisPedidos.getPedidos().size() + " pedidos)");
        }

        // Distribuição por prioridade
        Map<Prioridade, Integer> porPrioridade = contagemPorPrioridade(viagens);
        System.out.println("Pedidos por prioridade: " +
                "ALTA=" + porPrioridade.getOrDefault(Prioridade.ALTA, 0) + " | " +
                "MEDIA=" + porPrioridade.getOrDefault(Prioridade.MEDIA, 0) + " | " +
                "BAIXA=" + porPrioridade.getOrDefault(Prioridade.BAIXA, 0));

    }

    private static double somaDistancia(List<Viagem> viagens) {
        double s = 0.0;
        for (Viagem v : viagens)
            s += v.getDistanciaKm();
        return s;
    }

    private static double somaPeso(List<Viagem> viagens) {
        double s = 0.0;
        for (Viagem v : viagens)
            s += v.getPesoTotal();
        return s;
    }

    private static Map<Prioridade, Integer> contagemPorPrioridade(List<Viagem> viagens) {
        Map<Prioridade, Integer> mapa = new HashMap<>();
        for (Viagem v : viagens) {
            for (Pedido p : v.getPedidos()) {
                mapa.merge(p.getPrioridade(), 1, Integer::sum);
            }
        }
        return mapa;
    }

    public static void imprimirMapaAscii(List<Viagem> viagens, Deposito base, int width, int height) {
        char[][] grid = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = '.';
            }
        }

        int bx = base.getLocalizacao().getX();
        int by = base.getLocalizacao().getY();
        if (bx >= 0 && by >= 0 && bx < width && by < height) {
            grid[height - 1 - by][bx] = 'D';
        }

        for (Viagem v : viagens) {
            for (Pedido p : v.getPedidos()) {
                int px = p.getPonto().getX();
                int py = p.getPonto().getY();
                if (px >= 0 && py >= 0 && px < width && py < height) {
                    char marker = switch (p.getPrioridade()) {
                        case ALTA -> 'A';
                        case MEDIA -> 'M';
                        case BAIXA -> 'B';
                    };
                    grid[height - 1 - py][px] = marker;
                }
            }
        }

        System.out.println("Mapa das entregas:");
        for (int y = 0; y < height; y++) {
            System.out.println(new String(grid[y]));
        }
    }

}
