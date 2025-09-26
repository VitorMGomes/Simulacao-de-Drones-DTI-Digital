package src;

public class Main {
    public static void main(String[] args) {
        Pedido a = new Pedido(1, new Ponto(2, 1), 3.0, Prioridade.ALTA);
        Pedido b = new Pedido(2, new Ponto(5, 2), 2.0, Prioridade.BAIXA);

        System.out.println(a);
        System.out.println(b);

        Drone d = new Drone(1, 10.0, 25.0);
        System.out.println(d);

        Deposito base1 = new Deposito(); // (0,0)
        Deposito base2 = new Deposito(5, 5); // (5,5)
        System.out.println(base1);
        System.out.println(base2);

    }

}
