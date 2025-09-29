# O que aprendi implementando as Estratégias de resolução?

- `Uso do .sort`: Eu ja conecia a utilização do método .sort porém nunca havia aprendido usar com critérios de desempate para a ordenação.

- `Greedy`: Os termos de algoritmos gulosos ainda são relativamente novos para mim, pois ainda estou aprendo sobre na faculdade na materia de Teoria Dos Grafos e Computabilidade

- `Estuturação de problema`: Senti um pouco de dificuldade de implementar um problema mais real, ficando um pouco confuso em quais caminhos seguir

---

## 🔑 Classes e Interfaces

### `AlocacaoEstrategia.java`
**Responsabilidade:**  
Define um contrato para qualquer estratégia de alocação de pedidos.  

**Método principal:**  
- `List<Viagem> planejar(List<Pedido> pedidos, Drone drone, Deposito base, Distancia distancia)`  
  - Recebe todos os pedidos, o drone, a base e uma métrica de distância.  
  - Retorna uma lista de viagens (`Viagem`), cada uma com o subconjunto de pedidos que podem ser atendidos naquela rota.  

---

### `GreedyMinTrips.java`
**Responsabilidade:**  
Implementa uma estratégia **gulosa** que busca **minimizar o número de viagens**.  
Os pedidos são priorizados e agrupados de acordo com regras definidas.  

**Etapas do algoritmo:**  
1. **Ordenação dos pedidos**:  
   - Primeiro por **prioridade** (`ALTA` > `MÉDIA` > `BAIXA`),  
   - Depois por **peso** (maiores primeiro),  
   - Por fim, pela **distância até a base** (mais próximos primeiro).  

2. **Alocação gulosa**:  
   - Vai incluindo pedidos em uma viagem enquanto respeitar:  
     - Capacidade do drone,  
     - Alcance máximo (km),  
     - Energia da bateria.  

3. **Fechamento da viagem**:  
   - Quando não cabe mais nenhum pedido, registra a viagem (`Viagem`), consome bateria e recarrega na base.  
   - Recomeça uma nova viagem com os pedidos restantes.  

4. **Validação individual**:  
   - Se um pedido não cabe nem sozinho (capacidade, alcance ou bateria insuficiente), lança erro.  

---

## ⚙️ Principais métodos

### `planejar(...)`
- Entrada:  
  - Lista de pedidos (`Pedido`),  
  - Drone (`Drone`),  
  - Depósito (`Deposito`),  
  - Distância (`Distancia`).  
- Saída:  
  - Lista de viagens (`List<Viagem>`).  


---

## 📌 Observações importantes
- A estratégia é **gulosa**, ou seja, não garante a solução ótima global, mas é rápida e prática.  
- A ordem de prioridade garante que pedidos mais importantes (e mais pesados) sejam alocados primeiro.  
- O consumo e a recarga da bateria são simulados a cada viagem para manter consistência.  