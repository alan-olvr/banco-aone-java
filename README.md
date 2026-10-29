# 🏦 Conta Bancária

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=flat-square&logo=java)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-IDE-black?style=flat-square&logo=intellijidea)
![POO](https://img.shields.io/badge/Paradigma-POO-orange?style=flat-square)
![Status](https://img.shields.io/badge/Status-Em%20evolu%C3%A7%C3%A3o-yellow?style=flat-square)
![Versão](https://img.shields.io/badge/Vers%C3%A3o-1.0-purple?style=flat-square)

> O **Banco AOne** é um projeto da minha trilha de estudos em **Java**, desenvolvido com o objetivo de consolidar os fundamento da Programação Orientada a Objetos.
> A aplicação, executada no console, simula um sistema bancário com conta corrente e conta poupança: o usuário escolhe o tipo de conta, realiza depósitos e saques, 
> consulta o extrato e, na poupança, acompanha o rendimento acumulado.
> O foco não é a interface, e sim o **desenho do domínio**: hierarquia de classes, polimorfismo, validação de dados, tratamento de exceções personalizadas, encapsulamento e separação de responsabilidades, pensados desde o início para receber persistência em banco de dados e uma API no futuro.

<!-- TODO: ajuste a versão do Java e a badge de status se necessário -->

## 📑 Índice

- [Previews](#-previews)
- [Funcionalidades](#-funcionalidades)
- [Regras de Negócio](#-regras-de-negócio)
- [Como Rodar](#️-como-rodar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Decisões de Design](#-decisões-de-design)
- [Testes Realizados](#-testes-realizados)
- [Tecnologias e Conceitos](#️-tecnologias-e-conceitos-utilizados)
- [Roadmap](#-roadmap)
- [Aprendizados](#-aprendizados)
- [Autor](#-autor)

## 📸 Previews

<!-- TODO: adicione prints do terminal e o diagrama UML (Astah) na pasta assets/ -->
![preview](./assets/prev1.png)
![preview](./assets/prev2.png)

### Diagrama de Classes

<!-- TODO: exporte o diagrama do Astah como imagem e salve em assets/ -->
![diagrama](./assets/diagrama-classes.png)

## ✅ Funcionalidades

- [x] Criação de contas **corrente** e **poupança** com validação dos dados no cadastro (idade mínima, CPF, número da conta e saldo inicial).
- [x] Depósito e saque com validação de valor (rejeita zero, negativo, `NaN` e infinito).
- [x] Menu próprio por tipo de conta, com confirmação para continuar ou encerrar uma ação.
- [x] Extrato completo ou filtrado por tipo de operação (depósito ou saque).
- [x] Data e hora das operações em formato legível para o usuário.
- [x] Limite **diário** de operações, calculado a partir do histórico de transações.
- [x] Exceções personalizadas para regras de negócio.
- [x] Rendimento (`yield`) aplicado conforme o tipo de conta.
- [ ] Persistência em banco de dados (veja o [Roadmap](#-roadmap)).

<!-- TODO: confirme cada item acima com o código final e acrescente transferência, se estiver implementada -->

## 📏 Regras de negócio

| Regra | Conta Corrente | Conta Poupança |
|---|---|---|
| Operações disponíveis | Depositar, sacar, consultar extrato | Depositar, consultar rendimento, consultar extrato |
| Limite de saques | 4 | 2 |
| Rendimento | — | Taxa de 0,1 aplicada sobre cada depósito |
| Limite de depósitos com rendimento | — | 3 (evita bônus infinito) |

**Regras de cadastro (comuns às duas contas):**

- Titular: não aceita nulo, vazio ou com dígitos.
- CPF: exatamente 11 dígitos.
- Idade: mínima de 16 anos.
- Número da conta: entre 100.000 e 999.999.
- Saldo: não aceita `NaN`, infinito ou valor negativo.

## ▶️ Como Rodar

### Pré-requisitos

- Java 17+ instalado (`java -version` para conferir).
- Opcional: IntelliJ IDEA.

### Instalação

```bash
git clone https://github.com/alan-olvr/banco-aone-java.git
cd banco-aone-java
```

### Executando

**Pelo IntelliJ:** abra a pasta do projeto, localize a classe `Main` em `src/` e clique em **Run**.

**Pelo terminal:**

```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

No Windows (PowerShell):

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out Main
```

<!-- TODO: se a Main estiver dentro de um pacote, troque "Main" por "pacote.Main" -->

## 📁 Estrutura do Projeto

```
banco-aone-java/
└── src/
    ├── entities/
    │   ├── Account.java            -> Superclasse com as regras comuns
    │   ├── CheckingAccount.java    -> Conta corrente
    │   ├── SavingAccount.java      -> Conta poupança
    │   ├── Transaction.java        -> Registro imutável de cada transação
    │   └── TransactionType.java    -> Enum: tipos de transação
    ├── exceptions/
    │   ├── InsufficientBalanceException.java
    │   ├── InvalidValueException.java
    │   └── InvalidDataAccountException.java
    ├── util/
    ├── InterfaceMenu.java -> Menus e interação com o usuário
    └── Main.java                   -> Ponto de entrada da aplicação
```

<!-- TODO: ajuste esta árvore para refletir exatamente os arquivos e pacotes do projeto (inclua a classe do banco, se existir) -->

| Camada | Arquivo | Responsabilidade |
|---|---|---|
| Entidade | `Account.java` | Validação dos dados, depósito, extrato, `countToday()` e `validateValue()` |
| Entidade | `CheckingAccount.java` / `SavingAccount.java` | Saque e regras específicas de cada tipo |
| Entidade | `Transaction.java` | Valor, tipo e data/hora de cada operação |
| Enum | `TransactionType.java` | Tipos de transação (depósito, saque) |
| Exceção | `exceptions/*` | Erros de regra de negócio com significado próprio |
| Entrada | `Main.java` | Interação com o usuário; a saída no console fica fora do domínio |

## 🧠 Decisões de Design

As escolhas abaixo foram tomadas de propósito e estão explicadas nos comentários do código:

- **Superclasse `Account`:** o que é comum às duas contas (dados do titular, depósito, extrato) fica na superclasse. Cada subclasse implementa só o que é específico dela.
- **Polimorfismo no lugar de `if`/`switch` por tipo:** cada conta sabe se comportar conforme o seu tipo.
- **Transações como objeto:** cada movimentação vira uma `Transaction`, classificada por um `enum` (`TransactionType`). O extrato é montado a partir desse histórico.
- **Dois métodos `consultExtract`:** um devolve o extrato completo e outro filtra por tipo de operação.
- **Limite diário derivado do histórico:** `countToday(TransactionType)` conta as transações do dia, eliminando contadores `static` e estado duplicado.
- **Integridade no construtor:** `Account` valida os dados recebidos e lança a exceção `InvalidDataAccountException` quando algum é inválido, impedindo objetos em estado inconsistente.
- **Interface separada do domínio:** `InterfaceMenu` concentra mensagens e interação, mantendo as entidades focadas nas regras.

## 🧪 Testes realizados

Testes manuais, com entradas inválidas propositais:

| Cenário | Resultado |
|---|---|
| Titular nulo, em branco ou com dígitos | Recusado |
| Número da conta fora de 100.000–999.999 | Recusado |
| CPF nulo ou com quantidade de dígitos diferente de 11 | Recusado |
| Idade menor que 16 | Recusado |
| Saldo `NaN`, infinito ou negativo | Recusado |
| Depósito ou saldo inicial igual a zero | `InvalidValueException` |
| Saque igual a zero | Sistema pede um valor maior que zero |
| Saque maior que o saldo | Aviso de saldo insuficiente |
| Saque além do limite | Aviso de limite atingido |
| Opção inexistente no menu | Tratada sem encerrar o programa |

Ainda não há testes automatizados (veja o [Roadmap](#-roadmap)).


## ⚙️ Tecnologias e Conceitos Utilizados

**Linguagem:** Java 17+
**IDE:** IntelliJ IDEA
**Modelagem:** UML com Astah
**Paradigma:** Programação Orientada a Objetos

### Pilares da POO aplicados

| Pilar | Aplicação no Projeto |
|---|---|
| Encapsulamento | Atributos privados; o histórico de transações é exposto apenas como visão imutável |
| Herança | `CheckingAccount` e `SavingAccount` estendem `Account` |
| Polimorfismo | Comportamentos específicos por tipo de conta sem checagem manual de tipo |
| Abstração | Cada classe expõe apenas o necessário para as demais |

### Outros conceitos

| Conceito | Aplicação no Projeto |
|---|---|
| Enum | `TransactionType` representa os tipos de transação |
| Exceções personalizadas | Três exceções de negócio tratadas na `Main` |
| Imutabilidade | `Transaction` não pode ser alterada após criada |
| Constantes | Limites e regras de cadastro definidos como constantes |
| Expressões lambda | `forEach` no extrato e na `Main` para percorrer as transações |
| Clean Architecture (princípios) | Entidades e exceções organizadas por responsabilidade |

## 🚀 Roadmap

- [ ] Migrar valores monetários de `double` para `BigDecimal`
- [ ] Interface `ContaRepository` para isolar a persistência do domínio
- [ ] Persistência em banco de dados
- [ ] Testes unitários com JUnit
- [ ] Encapsular melhor a lista de transações e remover duplicação de código entre as subclasses
- [ ] Evoluir para uma API REST com Spring Boot

<!-- TODO: remova da lista o que já estiver pronto -->

## 💡 Aprendizados

- Desenhar o diagrama de classes **antes** de codar evitou retrabalho e deixou claro o que pertence a cada classe.
- Concentrar tudo em uma classe central gera um modelo anêmico; mover as regras para as contas deixou o código mais coeso.
- Estado derivado do histórico (`countToday()`) é mais seguro do que contadores guardados em memória.
- Exceções próprias deixam as regras de negócio explícitas e fáceis de testar.
- Revisar o próprio código depois de pronto revelou duplicações e parâmetros sem uso que passaram despercebidos durante o desenvolvimento.

## 👤 Autor

Feito por **Alan Oliveira** — [LinkedIn](https://www.linkedin.com/in/alan-oliveira-668515365/) · [GitHub](https://github.com/alan-olvr)
