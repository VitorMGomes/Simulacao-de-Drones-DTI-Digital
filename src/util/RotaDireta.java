package src.util;

import java.util.ArrayList;
import java.util.List;

import src.entidades.*;

public class RotaDireta {
    private final Distancia dist;

    public RotaDireta(Distancia dist) {
        this.dist = dist;
    }

    // Calcula a distância total percorrida pelo drone. O drone sai da base, visita
    // todos os pedidos em ordem, e retorna para o depósito
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

    // Calcula a distância de uma rota parcial, adicionando um novo pedido
    // a uma sequência já existente (PassoAtual)
    public double calcularCom(PassoAtual passo, Pedido novo, Deposito base) {
        List<Pedido> tmp = new ArrayList<>(passo.pedidos);
        tmp.add(novo);
        return calcular(base, tmp);
    }

    // uma lista de pedidos já visitados
    public static final class PassoAtual {
        public final List<Pedido> pedidos;

        public PassoAtual(List<Pedido> pedidos) {
            this.pedidos = pedidos;
        }
    }
}
