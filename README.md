# 🏦 Conta Bancária

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=flat-square&logo=java)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-IDE-black?style=flat-square&logo=intellijidea)
![POO](https://img.shields.io/badge/Paradigma-POO-orange?style=flat-square)
![Status](https://img.shields.io/badge/Status-Em%20evolu%C3%A7%C3%A3o-yellow?style=flat-square)
![Versão](https://img.shields.io/badge/Vers%C3%A3o-1.0-purple?style=flat-square)

> O **Conta Bancária** é um projeto de estudo em **Java puro** que simula o núcleo de um sistema bancário: contas correntes e poupança, depósitos, saques, extrato e regras de negócio.
> O foco não é a interface, e sim o **desenho do domínio**: hierarquia de classes, polimorfismo, encapsulamento, tratamento de exceções e separação de responsabilidades, pensados desde o início para receber persistência em banco de dados e uma API no futuro.

<!-- TODO: ajuste a versão do Java e a badge de status se necessário -->

## 📑 Índice

- [Previews](#-previews)
- [Funcionalidades](#-funcionalidades)
- [Como Rodar](#️-como-rodar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Decisões de Design](#-decisões-de-design)
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

- [x] Criação de contas **corrente** e **poupança** com validação dos dados no cadastro (idade mínima, CPF e número da conta).
- [x] Depósito e saque com validação de valor (rejeita zero, negativo, `NaN` e infinito).
- [x] Histórico de transações imutável, com extrato filtrável por tipo (depósito ou saque).
- [x] Limite **diário** de depósitos e saques, calculado a partir do histórico, sem contadores em memória.
- [x] Exceções próprias para regras de negócio (`InsufficientBalanceException`, `InvalidConditionException`).
- [x] Rendimento (`yield`) aplicado conforme o tipo de conta.
- [ ] Persistência em banco de dados (veja o [Roadmap](#-roadmap)).

<!-- TODO: confirme cada item acima com o código final e acrescente transferência, se estiver implementada -->

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
    │   ├── Account.java            -> Classe-base das contas (regras comuns)
    │   ├── CheckingAccount.java    -> Conta corrente
    │   ├── SavingAccount.java      -> Conta poupança
    │   ├── Transaction.java        -> Registro imutável de uma transação
    │   └── TransactionType.java    -> Enum: tipos de transação
    ├── exceptions/
    │   ├── InsufficientBalanceException.java
    │   └── InvalidConditionException.java
    └── Main.java                   -> Ponto de entrada da aplicação
```

<!-- TODO: ajuste esta árvore para refletir exatamente os arquivos e pacotes do projeto (inclua a classe do banco, se existir) -->

| Camada | Arquivo | Responsabilidade |
|---|---|---|
| Entidade | `Account.java` | Saldo, validações, depósito, saque, extrato e limites diários |
| Entidade | `CheckingAccount.java` / `SavingAccount.java` | Regras específicas de cada tipo de conta |
| Entidade | `Transaction.java` | Valor, tipo e data/hora de cada operação |
| Enum | `TransactionType.java` | Tipos de transação (depósito, saque) |
| Exceção | `exceptions/*` | Erros de regra de negócio com significado próprio |
| Entrada | `Main.java` | Interação com o usuário; a saída no console fica fora do domínio |

## 🧠 Decisões de Design

As escolhas abaixo foram tomadas de propósito e estão explicadas nos comentários do código:

- **Operações pertencem à conta:** depósito, saque, extrato e rendimento ficam nas classes de conta, evitando um modelo anêmico em que uma classe central faz tudo.
- **Polimorfismo no lugar de `if`/`switch` por tipo:** cada conta sabe se comportar conforme o seu tipo.
- **`Transaction` imutável:** uma vez criada, não muda. O extrato é devolvido como lista somente leitura.
- **Composição `Account` → `Transaction` (1 para 0..\*):** a conta possui seu histórico; a transação não conhece a conta.
- **Limite diário derivado do histórico:** `countToday(TransactionType)` conta as transações do dia, eliminando contadores `static` e estado duplicado.
- **Validação centralizada:** o construtor valida os dados com um helper privado e lança `InvalidConditionException`.
- **Sem `System.out.println` no domínio:** a lógica retorna dados ou lança exceções; quem imprime é a `Main`.

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
| Exceções personalizadas | Erros de negócio explícitos e tratados na `Main` |
| Imutabilidade | `Transaction` não pode ser alterada após criada |
| Constantes | Limites e regras de cadastro como `private static final` / `public static final` |
| Clean Architecture (princípios) | Domínio separado de entrada/saída e de persistência |

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
- Estado derivado (como o limite diário) é mais seguro do que contadores guardados em memória.
- Exceções próprias tornam as regras de negócio explícitas e fáceis de testar.
- Revisar o próprio código depois de pronto revelou pontos fracos que valem como próximas etapas.

## 👤 Autor

Feito por **Alan Oliveira** — [LinkedIn](https://www.linkedin.com/in/alan-oliveira-668515365/) · [GitHub](https://github.com/alan-olvr)
