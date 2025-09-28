package src.principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import src.entidades.*;
import src.estrategia.*;
import src.relatorio.Relatorio;
import src.util.*;

public class MainCsv {

    public static void main(String[] args) {
        // Caminhos padrão (pode passar por args: args[0]=config, args[1]=pedidos)
        String configPath  = args.length > 0 ? args[0] : "data/config.csv";
        String pedidosPath = args.length > 1 ? args[1] : "data/pedidos.csv";
        try {
            // 1) Carrega config do drone/bateria
            Drone drone = carregarDroneDeCsv(configPath);

            // 2) Carrega pedidos
            List<Pedido> pedidos = carregarPedidosDeCsv(pedidosPath);

            // 3) Execução
            Deposito base = new Deposito(0, 0);
            Distancia distancia = new Euclidiana();
            AlocacaoEstrategia estrategia = new GreedyMinTrips();

            List<Viagem> viagens = estrategia.planejar(pedidos, drone, base, distancia);

            // 4) Saída
            Relatorio.imprimir(viagens, drone);
            Relatorio.imprimirMapaAscii(viagens, base, 12, 8);

        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // ==================== CSV: CONFIG ====================

    private static Drone carregarDroneDeCsv(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine();
            if (header == null) throw new IllegalArgumentException("config.csv vazio");

            Map<String, Integer> idx = indices(header, ",",
                "drone_id", "capacidade_kg", "alcance_km", "bateria_cap_wh", "consumo_wh_km", "recarga_wh_min");

            String line = br.readLine();
            if (line == null) throw new IllegalArgumentException("config.csv sem dados");

            String[] c = splitCsv(line);

            int id               = parseInt(c[idx.get("drone_id")],        "drone_id");
            double capacidadeKg  = parseDouble(c[idx.get("capacidade_kg")],"capacidade_kg");
            double alcanceKm     = parseDouble(c[idx.get("alcance_km")],   "alcance_km");
            double capWh         = parseDouble(c[idx.get("bateria_cap_wh")],"bateria_cap_wh");
            double consWhKm      = parseDouble(c[idx.get("consumo_wh_km")],"consumo_wh_km");
            double recWhMin      = parseDouble(c[idx.get("recarga_wh_min")],"recarga_wh_min");

            Bateria bat = new Bateria(capWh, consWhKm, recWhMin);
            return new Drone(id, capacidadeKg, alcanceKm, bat);
        }
    }

    // ==================== CSV: PEDIDOS ====================

    private static List<Pedido> carregarPedidosDeCsv(String path) throws IOException {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine();
            if (header == null) throw new IllegalArgumentException("pedidos.csv vazio");

            Map<String, Integer> idx = indices(header, ",",
                "id", "x", "y", "peso", "prioridade");

            String line;
            int linha = 1;
            while ((line = br.readLine()) != null) {
                linha++;
                if (line.isBlank()) continue;
                String[] c = splitCsv(line);
                try {
                    int id          = parseInt(c[idx.get("id")],   "id");
                    int x           = parseInt(c[idx.get("x")],    "x");
                    int y           = parseInt(c[idx.get("y")],    "y");
                    double peso     = parseDouble(c[idx.get("peso")], "peso");
                    String prioStr  = c[idx.get("prioridade")].trim().toUpperCase(Locale.ROOT);

                    Prioridade prio = switch (prioStr) {
                        case "ALTA"  -> Prioridade.ALTA;
                        case "MEDIA" -> Prioridade.MEDIA;
                        case "BAIXA" -> Prioridade.BAIXA;
                        default -> throw new IllegalArgumentException("prioridade inválida: " + prioStr);
                    };

                    pedidos.add(new Pedido(id, new Ponto(x, y), peso, prio));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Linha " + linha + " inválida em " + path + ": " + e.getMessage(), e);
                }
            }
        }
        if (pedidos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum pedido lido de " + path);
        }
        return pedidos;
    }

    // ==================== Helpers ====================

    private static Map<String, Integer> indices(String header, String sep, String... camposObrigatorios) {
        String[] cols = splitCsv(header);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < cols.length; i++) {
            map.put(cols[i].trim().toLowerCase(Locale.ROOT), i);
        }
        for (String campo : camposObrigatorios) {
            if (!map.containsKey(campo.toLowerCase(Locale.ROOT))) {
                throw new IllegalArgumentException("Coluna obrigatória ausente: " + campo);
            }
        }
        return map;
    }

    private static String[] splitCsv(String line) {
        // simples: separa por vírgula e faz trim; se for usar campos com vírgula/aspas, troque para uma lib (OpenCSV)
        String[] parts = line.split(",", -1);
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }

    private static int parseInt(String s, String campo) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inteiro inválido para " + campo + ": '" + s + "'");
        }
    }

    private static double parseDouble(String s, String campo) {
        try {
            return Double.parseDouble(s.replace(",", ".")); // aceita vírgula decimal
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor numérico inválido para " + campo + ": '" + s + "'");
        }
    }
}
