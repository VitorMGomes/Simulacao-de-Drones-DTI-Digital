package src.entidades;

import java.util.*;

public class Viagem {
    private final int droneId;
    private final List<Pedido> pedidos;
    private final double distanciaKm;

    public Viagem(int droneId, List<Pedido> pedidos, double distanciaKm) {
        this.droneId = droneId;
        
        // Cria uma cópia da lista recebida para garantir encapsulamento.
        // Em seguida, torna essa cópia imutável com Collections.unmodifiableList,
        // impedindo alterações externas após a criação da Viagem.
        // Assim, evitamos efeitos colaterais e garantimos consistência dos dados.
        this.pedidos = Collections.unmodifiableList(new ArrayList<>(pedidos)); // ideia do GPT
        this.distanciaKm = distanciaKm;
    }

    public int getDroneId() {
        return droneId;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public double getPesoTotal() {
        double s = 0.0;
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            s += p.getPeso();
        }
        return s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Viagem do Drone #").append(droneId).append(" ===\n");
        sb.append("Pedidos: ");
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            sb.append("\n - ID: ").append(p.getId())
                    .append(" | Peso: ").append(p.getPeso()).append("kg")
                    .append(" | Prioridade: ").append(p.getPrioridade())
                    .append(" | Posição: ").append(p.getPonto());
        }
        sb.append("\nPeso total: ").append(String.format("%.2f", getPesoTotal())).append("kg");
        sb.append("\nDistância total: ").append(String.format("%.2f", distanciaKm)).append("km\n");
        return sb.toString();
    }

}
