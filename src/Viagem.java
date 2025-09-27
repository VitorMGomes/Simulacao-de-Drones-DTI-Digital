package src;

import java.util.*;

public class Viagem {
    private final int droneId;
    private final List<Pedido> pedidos;
    private final double distanciaKm;

    public Viagem(int droneId, List<Pedido> pedidos, double distanciaKm)
    {
        this.droneId = droneId;
        //this.pedidos = pedidos;
        this.pedidos = Collections.unmodifiableList(new ArrayList<>(pedidos)); //ideia do GPT
        this.distanciaKm = distanciaKm;
    }

    public int getDroneId()
    { 
        return droneId; 
    }
    public List<Pedido> getPedidos() 
    {
        return pedidos;
    }
    public double getDistanciaKm()
    {
        return distanciaKm; 
    }

    public double getPesoTotal() 
    {
        double s = 0.0;
        for (Pedido p : pedidos) s += p.getPeso();
        return s;
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder("Viagem{drone=");
        sb.append(droneId).append(", pedidos=[");
        for (int i=0;i<pedidos.size();i++) {
            if (i>0) sb.append(", ");
            sb.append(pedidos.get(i).getId());
        }
        sb.append("], distancia=").append(String.format("%.2f", distanciaKm)).append("km}");
        return sb.toString();
    }
}
