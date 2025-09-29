# Simulação de Drones para Entregas

Sistema em **Java** que planeja rotas de entrega de drones considerando **capacidade de carga**, **alcance máximo**, **bateria** e **prioridades** dos pedidos.  
O projeto implementa uma estratégia **gulosa** (`GreedyMinTrips`) que utiliza fila de prioridade para definir a ordem de atendimento.

---

## 🚀 Pré-requisitos
- **Java 17+**
- Terminal ou IDE compatível (IntelliJ, VSCode, Eclipse)

---

## 📂 Estrutura do Projeto

- `src/entidades/` → Classes de domínio (Pedido, Drone, Depósito, etc.)
- `src/estrategia/` → Estratégias de alocação de pedidos (ex: GreedyMinTrips)
- `src/relatorio/` → Geração de relatórios
- `src/principal/` → Pontos de entrada (Main, MainCsv, Testes)
- `data/` → Arquivos de configuração e pedidos em CSV


---

## ▶️ Como Executar

### A) Demo com cenários prontos
Compilar:
```bash
javac -d out $(find src -name "*.java")
```
Rodar:
```bash
java -cp out src.principal.Main
```

### B) Usando arquivos CSV
Compilar:
```bash
javac -d out $(find src -name "*.java")
```
Rodar com CSV:
```bash
java -cp out src.principal.MainCsv data/config.csv data/pedidos.csv
```

### Formato dos CSVs

**config.csv**
```
drone_id,capacidade_kg,alcance_km,bateria_cap_wh,consumo_wh_km,recarga_wh_min
1,10,25,500,10,0
```

**pedidos.csv**
```
id,x,y,peso,prioridade
1,2,1,3.0,ALTA
2,5,2,2.0,BAIXA
3,3,4,2.5,MEDIA
4,6,1,4.0,ALTA
5,1,2,1.5,MEDIA
```

---

## 🧪 Testes Unitários

Este projeto inclui a classe `Testes.java` com asserts para validar:
- Distância Euclidiana (ex.: 3-4-5)
- Quebra em múltiplas viagens por capacidade
- Quebra em múltiplas viagens por alcance
- Exceção em bateria insuficiente

Para executar os testes:
```bash
javac -d out $(find src -name "*.java")
java -ea -cp out src.principal.Testes
```

Saída esperada:
```
Todos os testes passaram ✅
```

---

## 📊 Exemplo de Saída (Relatório)

```
===== RELATÓRIO =====

=== Viagem do Drone #1 ===
Pedidos: 
 - ID: 1 | Peso: 2.0kg | Prioridade: ALTA | Posição: (5,0)
Peso total: 2,00kg
Distância total: 10,00km

----- Resumo -----
Total de viagens: 1
Total de pedidos: 1
Distância total: 10,00 km
Distância média por viagem: 10,00 km
Peso total transportado: 2,00 kg
Bateria restante do Drone #1: 0%
Viagem mais longa: Drone #1 (10,00 km)
Viagem com mais pedidos: Drone #1 (1 pedidos)
Pedidos por prioridade: ALTA=1 | MEDIA=0 | BAIXA=0
Mapa das entregas:
D....A......
```

---

## ⚙️ Decisões de Projeto
- Uso de **PriorityQueue** para ordenar pedidos (prioridade, peso, distância).
- Drones sempre possuem **bateria obrigatória**, que é **recarregada automaticamente** ao retornar à base.
- Foi adotada uma estratégia gulosa **(GreedyMinTrips)** porque o problema de planejamento de rotas de drones é NP-difícil. O algoritmo guloso ordena os pedidos por prioridade, peso e distância e vai alocando sequencialmente, respeitando capacidade, alcance e bateria.
Assim, garante soluções rápidas e de boa qualidade, mesmo sem ser ótimo em todos os cenários.
- Relatório mostra estatísticas, distribuição por prioridade e mapa ASCII das entregas.

---

## 🤖 Como foi utilizada IA na construção do projeto

Durante o desenvolvimento, utilizei ferramentas de **Inteligência Artificial (IA)** como apoio, principalmente o **ChatGPT**, em três frentes:  

1. **Validação de ideias:**  
   - Usei a IA para revisar conceitos e confirmar a lógica de algumas soluções antes de implementá-las.  
   - Usei-a também para conseguir algumas ideias das quais eu não havia pensado ou não possuia o conhecimento necessário

2. **Apoio em algoritmos específicos:**  
   - Para o algoritmo **guloso (GreedyMinTrips)**, consultei a IA para estruturar a lógica de ordenação por prioridade, peso e distância.  
   - A IA auxiliou principalmente na clareza da implementação e organização das classes/interfaces.

3. **Boas práticas e organização:**  
   - A IA serviu como guia para revisar padrões de código em Java, uso adequado de `PriorityQueue`, estruturação de pacotes e boas práticas de testes unitários.  
   - Ajuda na implementação em métodos nativos do Java, como as estruturas de dados e ordenações, já que na faculdade sempre utilizei elas feitas manualmente (.sort como exemplo). 

4. **Depuração e erros:**  
   - Utilizei a IA para compreender mensagens de erro e exceções em Java, acelerando a correção de problemas sem depender apenas de tentativa e erro.  

5. **Exploração de alternativas:**  
   - Avaliei com apoio da IA diferentes abordagens para o planejamento de rotas, como busca exaustiva, mas optei pela estratégia gulosa por garantir melhor equilíbrio entre eficiência e tempo de execução.  

6. **Documentação e comunicação:**  
   - A IA auxiliou na organização do README e na escrita de explicações mais claras sobre o funcionamento do projeto e as decisões de design.  

7. **Aprendizado contínuo:**  
   - Usei a IA como recurso de aprendizado, consolidando conceitos de algoritmos e estruturas de dados já estudados na graduação.

8. **Revisar textos:**  
   - Utilizei IA para revisar textos, além de aumentar a coesão dos mesmos. 
