# emuredis

Programa que emula alguns comands do Redis.

## Requisitos implementados

- Parte 1: Núcleo do Redis (todos os comandos do desafio)
- Parte 2: Camada de rede
    - 2.1: API de comandos genéricos
    - 2.2: API REST

## Tempo de execução

30 horas

## Decisões de projeto

### Domain-driven Development e Layered Architecture

Seguindo os princípios de DDD (Domain-driven Development), eu implementei todas as regras de negócio
de cada comando do Redis utilizando a própria linguagem utilizada para descrever os requisitos da
aplicação. No pacote `domain.model`, é possível encontrar os vários conceitos que compõe o Redis,
como chave, valor, string e sorted set.

Para estruturar a aplicação, eu usei uma arquitetura em camadas (Layered Architecture), em que as
camadas mais internas da aplicação não têm acesso à camadas mais superiores. O programa é divido nas
seguintes camadas:

- Domain Model: implementa todas as regras de negócio.
- Application: responsável por descrever casos de uso e também pelo controle transacional.
- Infrastructure: responsável pela implementação de conceitos das camadas inferiores, assim como
a implementação de ports e adapters para acessar a camada de Application.

### Domain Model

- Chaves com tempo de expiração: Conceitualmente o que expira é uma chave, porém, quando uma chave é
sobreescrita, o tempo de expiração é apagado/renovado. Logo, fez mais sentido modelar a expiração
como parte do valor, em vez da chave.

- Sorted Set: As implementações padrão do Java de Sorted Set requerem que o `equals` seja consistente
com `compareTo`. Isso faz com que elas não sejam adequadas para a estrutura de dados Sorted Set do
Redis, pois os membros do Set são ordenados pelo score, mas são únicos no Set pelo seu valor. A
solução escolhida foi utilizando o `SortedSet` do Java, entretanto, utilizando uma cópia do SortedSet
como `ArrayList` para verificar se um valor é membro do Sorted Set, e utilizando o método `removeIf`
do `SortedSet` para remover o membro de acordo com o `equals`. Na implementação do comando `ZRANGE`,
também é feita uma cópia do `SortedSet` para um `ArrayList`. Isso tudo implica que tanto inserção
quanto consulta no Sorted Set tem complexidade `O(n)`, enquanto na implementação do Redis é
utilizada uma Skip List, cuja complexidade de inserção e consulta é `O(log n)`.

### Application

- Atomicidade: A garantia de atomicidade é alcançada por meio da execução do núcleo do programa em
uma única thread, que é a mesma abordagem real utilizada pelo Redis. Na prática, os comandos
executam muito rápido, e o gargalo tende a ser a rede. Para solucionar esse gargalo, o Redis real
tem artifícios como Pipelining, que permite executar vários comandos em sequência com apenas uma
requisição e também comandos que operam em bulk, como o comando `ZADD`, que permite adicionar vários
membros no mesmo comando. Executar o núcleo do programa em apenas um core ainda permite múltiplas
conexões simultâneas, e torna a implementação mais simples e menos suscetível a erros.

### Infrastructure

- Read Eval Print Loop (REPL): Foi implementado um REPL simples que faz o parsing de uma linha de
comando e chama os métodos dos Application Services da camada inferior. O parser desse componente
foi depois reutilizado na implementação da API HTTP com comandos genéricos e também na API REST.

## Execução do programa

### Requisitos

- Java 11
- Maven 3.6.3

### Executando

1. Rodar o comando `mvn clean package` na raiz do projeto.
2. Rodar o script `run.sh`.

O servidor HTTP será iniciado em background na porta 8080. Na pasta `postman` tem duas collections
que podem ser importadas no programa Postman com exemplos de cada requisição possível. O REPL é
iniciado em foreground, onde é possível digitar comandos via `stdin` e ler as respostas do programa
no `stdout`.
