package src.estrategia;

import java.util.List;

import src.entidades.*;
import src.util.*;

public interface AlocacaoEstrategia {
    List<Viagem> planejar(List<Pedido> pedidos, Drone drone, Deposito base, Distancia distancia);
}
