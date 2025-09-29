# Entidades

### Bateria
**Responsabilidade:** Modela a bateria de um drone, controlando capacidade, consumo e recarga【31†source】.

**Principais campos**
- `capacidadeWh`: capacidade total da bateria em Wh.
- `cargaWh`: carga atual disponível em Wh.
- `consumoWhPorKm`: energia consumida por quilômetro percorrido.
- `recargaWhPorMin`: taxa de recarga em Wh/minuto.


**Métodos principais**
- `int recarregarAteCheio()`: recarrega até a capacidade máxima, retornando o tempo necessário.
- `boolean suportaDistancia(double km)`: verifica se há energia suficiente para a distância.
- `void consumir(double km)`: reduz a carga proporcional à distância percorrida.
- `void recarregarMinutos(int minutos)`: adiciona carga parcial limitada pela capacidade.

**Invariantes**
- `capacidadeWh > 0`, `consumoWhPorKm > 0`.

**Relações**
- Usada diretamente por `Drone` para verificar e gerenciar autonomia.

---

### Deposito
**Responsabilidade:** Representa a base de origem/retorno do drone【32†source】.

**Campos**
- `localizacao`: instância de `Ponto` com coordenadas do depósito.

**Construtores**
- Sem parâmetros: localização em (0,0).
- Com `Ponto` ou coordenadas `x,y`.

**Relações**
- Fundamental para cálculos de distância e planejamento de rotas.

---

### Drone
**Responsabilidade:** Modela um drone com capacidade de carga, alcance e bateria.

**Campos**
- `id`: identificador único.
- `capacidade`: carga máxima em kg.
- `alcance`: distância máxima em km.
- `bateria`: instância de `Bateria`.

**Métodos principais**
- `suportaPeso(double)`: verifica se um peso cabe no drone.
- `suportaRota(double)`: verifica se a distância cabe no alcance nominal.
- `suportaRotaPorBateria(double)`: verifica autonomia real pela bateria.
- `consumirBateria(double)`: reduz carga da bateria.
- `recarregarTotalNaBase()`: recarrega bateria ao máximo.

**Relações**
- Depende de `Bateria`. Usado em `Viagem` para transportar `Pedido`.

---

### Pedido
**Responsabilidade:** Modela um pedido de entrega com posição, peso e prioridade【34†source】.

**Campos**
- `id`: identificador do pedido.
- `posicao`: instância de `Ponto`.
- `peso`: peso em kg.
- `prioridade`: instância de `Prioridade`.

**Relações**
- Agrupados em `Viagem`.
- Usados para ordenar entregas em estratégias.

---


### Ponto
**Responsabilidade:** Representa uma coordenada cartesiana (x,y).

**Campos**
- `x`, `y`: inteiros que definem a posição.

**Métodos principais**
- `getX()`, `getY()`.
- `equals()` e `hashCode()`: permitem comparação e uso em coleções.
- `toString()`: exibe no formato `(x,y)`.

---

### Prioridade
**Responsabilidade:** Enumeração de níveis de prioridade de pedidos.

**Valores**
- `BAIXA(1)`
- `MEDIA(2)`
- `ALTA(3)`

**Métodos principais**
- `getScore()`: retorna valor numérico associado (1–3).

---

### Viagem
**Responsabilidade:** Representa uma viagem realizada por um drone com múltiplos pedidos.

**Campos**
- `droneId`: id do drone associado.
- `pedidos`: lista imutável de `Pedido`.
- `distanciaKm`: distância total percorrida.

**Métodos principais**
- `getPesoTotal()`: soma o peso dos pedidos.
- `toString()`: descrição formatada da viagem.

**Relações**
- Agrega `Pedido`, vinculado a um `Drone`.
