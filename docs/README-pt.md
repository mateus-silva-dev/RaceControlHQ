# RaceControlHQ API

<p align="right">
  <code><a href="../README.md">Início</a></code> / 
  <code><a href="README-pt.md">Português</a></code> / 
  <code><a href="README-en.md">English</a></code>
</p>

**Documentação Funcional da API**

**Versão:** 1.0  
**Autor:** Mateus Silva  
**Data:** Abril, 2026  
**Ambiente:** Desenvolvimento

---

1. [📌 Sobre a Versão 1.0](#1-sobre-a-versão-10)
2. [🎯 Objetivo do Sistema](#2-objetivo-do-sistema)
3. [📝 Escopo Funcional](#3-escopo-funcional)
4. [⚖️ Regras de Negócio](#4-regras-de-negócio)
5. [🛠️ Recursos da API](#5-recursos-da-api)
6. [💻 Tecnologias](#6-tecnologias)

---

## 1. 📌 Sobre a Versão 1.0
Esta versão inicial foca no gerenciamento central da liga, permitindo o controle total sobre a estrutura das competições. O sistema foi desenhado para ser aberto e flexível para qualquer organizador que deseje gerenciar suas categorias de forma profissional.

## 2. 🎯 Objetivo do Sistema
O **RaceControlHQ** é uma solução desenvolvida para automatizar a complexa logística de campeonatos de automobilismo virtual e real. A API centraliza o gerenciamento de ligas, temporadas e competidores, fornecendo um motor de regras que garante a integridade dos resultados e o histórico de carreira dos pilotos.

* 👥 **Para quem:** Organizadores de ligas, administradores de comunidades de simracing e desenvolvedores de plataformas esportivas.
* 🛠️ **Problema resolvido:** Elimina a dependência de planilhas manuais propensas a erros, resolve conflitos de datas e automatiza o cálculo de rankings e pontuações complexas.

## 3. 📝 Escopo Funcional
A API cobre todo o ciclo de vida de uma competição:
* 🏆 **Gestão de Ligas e Seasons:** Estruturação de campeonatos de curto e longo prazo.
* 👤 **Gestão de Competidores:** Cadastro centralizado de pilotos e escuderias (equipes).
* 📝 **Contratos e Vínculos:** Registro de qual piloto corre por qual equipe em cada temporada.
* 📅 **Calendário de Corridas:** Agendamento e detalhamento de eventos de pista.
* 🏁 **Registro de Resultados:** Entrada de dados de performance pós-corrida.
* 📊 **Processamento de Rankings:** Cálculo automático de pontuação e classificação (Standings).

## 4. ⚖️ Regras de Negócio (Principais)
Para garantir a consistência dos dados, o sistema segue diretrizes rígidas:
* ⏳ **Integridade Cronológica:** Uma corrida só pode ser agendada dentro do intervalo de datas de sua respectiva temporada.
* 🤝 **Vínculo Temporário:** Um piloto pode defender equipes diferentes em temporadas diferentes, mas apenas uma equipe por vez dentro da mesma temporada.
* 🚫 **Consistência de Resultados:** O sistema impede o registro de resultados para pilotos que não estejam vinculados à temporada.
* 🆔 **Unicidade:** Cada piloto possui um Driver ID único para preservar seu histórico entre diferentes ligas.

## 5. 🛠️ Recursos da API

### 📂 Módulo: Liga & Temporada
* ✨ **Criar Liga:** Define o nome e a estrutura base.
* 🗓️ **Gerenciar Temporada:** Cria intervalos de tempo para competições (Início/Fim).
* 🔍 **Consultar Detalhes:** Exibe informações completas da temporada e seu progresso.

### 🏎️ Módulo: Piloto & Equipe
* 👤 **Gestão de Pilotos:** Criar, listar e atualizar dados de competidores.
* 🛡️ **Gestão de Equipes:** Criar e listar escuderias.
* 🔗 **Vincular Piloto:** Atribuir um piloto a uma equipe para uma temporada específica.

### 🏁 Módulo: Corrida & Resultados
* 🚦 **Agendar Corrida:** Define data, hora, circuito e categoria.
* 🏆 **Registrar Resultados:** Input das posições finais, tempos e bonificações.
* 📈 **Consultar Rankings:** Gera a tabela de pontuação atualizada em tempo real.

## 6. 💻 Tecnologias
* **Linguagem:** Java 21
* **Framework:** Spring Boot 3
* **Arquitetura:** Domain-Driven Design (DDD)
* **Testes:** JUnit 5, Mockito (Foco em TDD)

---
[English version](./README-en.md)