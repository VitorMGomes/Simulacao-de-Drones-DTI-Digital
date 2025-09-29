# O que aprendi implementando os arquivos aqui presentes?
- `Euclidiana`: Não lembrada da utilização da Euclidiana para problemas reais, como o uso de drones. 

# Documentação da pasta `util`

Esta pasta contém classes utilitárias responsáveis por cálculos de distância e definição de rotas dentro do sistema de entrega com drones.

---

## 📌 Distancia.java
**Tipo:** Interface

Define o contrato para cálculo de distância entre dois pontos (`Ponto a`, `Ponto b`).  
Permite implementar diferentes métricas de distância.

**Método principal:**
- `double entre(Ponto a, Ponto b)` → calcula a distância entre dois pontos.

---

## 📌 Euclidiana.java
**Tipo:** Implementação da interface `Distancia`

Responsável por calcular a **distância euclidiana** entre dois pontos.

**Método:**
- `double entre(Ponto a, Ponto b)`  
  Calcula a distância com a fórmula:  
  \( d = \sqrt{(x1 - x2)^2 + (y1 - y2)^2} \)

---

## 📌 RotaDireta.java
**Tipo:** Classe utilitária

Responsável por calcular a **distância total de uma rota**, onde um drone sai do depósito, visita todos os pedidos em sequência e retorna à base.

### Principais métodos:
- `double calcular(Deposito base, List<Pedido> pedidos)`  
  Calcula a distância total de ida e volta, considerando os pedidos em ordem.

- `double calcularCom(PassoAtual passo, Pedido novo, Deposito base)`  
  Calcula a distância de uma rota parcial (`PassoAtual`) adicionando um novo pedido.

### Classe interna:
- `PassoAtual`  
  Estrutura imutável que representa uma lista parcial de pedidos já visitados.


