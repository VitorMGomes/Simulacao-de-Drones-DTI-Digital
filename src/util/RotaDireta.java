package src.util;

import java.util.ArrayList;
import java.util.List;

import src.entidades.*;

public class RotaDireta {
    private final Distancia dist;

    public RotaDireta(Distancia dist) {
        this.dist = dist;
    }

    public double calcular(Deposito base, List<Pedido> pedidos) {
        double total = 0.0;
        Ponto atual = base.getLocalizacao();
        for (Pedido p : pedidos) {
            total += dist.entre(atual, p.getPonto());
            atual = p.getPonto();
        }
        total += dist.entre(atual, base.getLocalizacao());
        return total;
    }

    public double calcularCom(PassoAtual passo, Pedido novo, Deposito base) {
        List<Pedido> tmp = new ArrayList<>(passo.pedidos);
        tmp.add(novo);
        return calcular(base, tmp);
    }

    public static final class PassoAtual {
        public final List<Pedido> pedidos;

        public PassoAtual(List<Pedido> pedidos) {
            this.pedidos = pedidos;
        }
    }
}
