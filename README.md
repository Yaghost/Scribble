# Scribble

| ![Logo do Scribble](./Documentação/assets/logotipo.png) | Scribble é um sistema desktop desenvolvido em Java, cujo objetivo é facilitar a criação, edição e salvamento de diagramas Molic (Modeling Language for Interaction as Conversation). Ele foi projetado para ser uma ferramenta prática e intuitiva para diagramar e documentar interações em projetos de design conversacional. |
|:--:|:--|


---
# Clientes 👥

Nossa principal cliente é a professora Ingrid Monteiro, do campus de Quixadá da UFC. Além dela, as partes que se beneficiarão do sistema incluem alunos da disciplina de Interação Humano-Computador (IHC), pesquisadores e designers de interação.
Durante a fase de elicitação de requisitos, foram conduzidas entrevistas semiestruturadas com nossa principal cliente. Durante o processo de desenvolvimento, foram realizadas reuniões com o intuito de receber feedbacks e sugestões de melhoria.

---
# Requisitos e Histórias de Usuário 📄

Este documento tem como objetivo definir e detalhar os requisitos funcionais e não funcionais do sistema Scribble. Ele serve como uma referência para descrever as funcionalidades, restrições e comportamentos esperados, garantindo que o sistema atenda às necessidades dos usuários e cumpra as especificações técnicas.  Como também as histórias de usuários descritas, que têm como objetivo capturar as necessidades dos usuários finais em relação ao sistema Scribble. Essas histórias orientam o desenvolvimento de funcionalidades de maneira centrada no usuário, ajudando a garantir que o produto final atenda aos requisitos práticos e facilite a experiência.  

Link: [Requisitos e Histórias de Usuário](./Documentação/Requisitos%20Funcionais%20e%20Não-Funcionais%20e%20Histórias%20de%20Usuário.pdf)

---

# Modelagem 📐

A modelagem de classes define a estrutura estática, detalhando entidades como diagramas e elementos gráficos, suas propriedades e interações. Já a modelagem de atividades representa o fluxo dinâmico do sistema, descrevendo as etapas e processos envolvidos na criação, edição e salvamento de diagramas Molic. Juntas, essas modelagens oferecem uma visão completa dos processos do Scribble, garantindo um planejamento e implementação eficazes.

Link: [Modelagem](Documentação/Modelagem%20&%20Arquitetura.pdf)

---

# Arquitetura ⚙
![Arquitetura](./Documentação/assets/arquitetura.jfif)

---

# Testes 🔎
No projeto Scribble, os testes unitários validaram o funcionamento de componentes isolados, enquanto os testes sistêmicos manuais avaliaram o comportamento do sistema como um todo, garantindo que os fluxos de criação, edição e salvamento de diagramas funcionassem corretamente.

## Testes Unitários
Os testes unitários foram realizados para validar as funcionalidades de persistência e manipulação das anotações no banco de dados. Eles abrangem a manipulação de dados na tabela de anotações, interações com o banco de dados via mock do DAO, e as funcionalidades da interface gráfica para adicionar, editar e excluir anotações. A seguir, um resumo dos testes, com objetivo, descrição e resultado de cada um:

Link: [Testes Automatizados](./Documentação/Testes.pdf)
## Testes Sistêmicos
Os testes foram realizados pela equipe para validação do sistema e para correções/melhorias de funções do sistema. A seguir, segue o link de acesso para os testes para uma melhor visualização dos mesmos:

Link: [Testes Manuais](https://docs.google.com/spreadsheets/d/19H1wCPnC2xXhk8pEj9W9kB_1EBQ89W3a/edit?gid=173414780#gid=173414780)
