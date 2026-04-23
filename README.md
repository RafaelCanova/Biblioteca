# Librarian - Sistema de Gestão de Bibliotecas

O **Librarian** é uma plataforma robusta para a automatização de bibliotecas, permitindo o controlo de acervo, gestão de empréstimos, reservas de títulos e autenticação segura de utilizadores. Este projeto foi desenvolvido como parte integrante do 8º semestre do curso de Engenharia de Software na **Universidade Mogi das Cruzes (UMC)**.

## 🚀 Tecnologias Utilizadas

O projeto segue uma arquitetura moderna baseada em microserviços/camadas, conforme definido no Diagrama de Arquitetura:

* **Backend:** Java 17 com **Spring Boot 3**
* **Base de Dados:** **MongoDB** (NoSQL) para alta flexibilidade de metadados de livros.
* **Segurança:** Spring Security com criptografia BCrypt e tokens de recuperação de senha.
* **Frontend:** Integração via Thymeleaf/Node.js (conforme planeado).
* **Gestão de Dependências:** Maven.

## 📋 Estrutura do Projeto (EAP)

O desenvolvimento foi guiado pela Estrutura Analítica do Projeto (EAP), cobrindo as seguintes fases:

1.  **Planeamento:** Definição de requisitos e aderência tecnológica.
2.  **Design de Arquitetura:** Modelagem de dados NoSQL e fluxos de autenticação.
3.  **Desenvolvimento:**
    * `UserService`: Gestão de perfis e níveis de acesso (Admin, Bibliotecário, Leitor).
    * `BookService`: Controlo dinâmico de inventário e exemplares.
    * `LoanService`: Fluxo de empréstimo e devolução.
    * `ReservationService`: Sistema de reserva para livros sem disponibilidade imediata.
    * `PasswordReset`: Fluxo de segurança para recuperação de conta via e-mail.

## 🛠️ Instalação e Execução

### Pré-requisitos
* JDK 17 ou superior.
* Instância do MongoDB (local ou Atlas) a correr.
* Maven instalado.

### Passos
1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/librarian.git
    ```
2.  **Configure as credenciais do MongoDB** no ficheiro `src/main/resources/application.properties`:
    ```properties
    spring.data.mongodb.uri=mongodb://localhost:27017/librarian
    spring.mail.host=smtp.seuservidor.com
    ```
3.  **Compile e execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

## 🔐 Segurança e Regras de Negócio

* **Autenticação:** Implementada via `CustomUserDetailsService`.
* **Níveis de Acesso:**
    * `ADMIN`: Gestão total de utilizadores e sistema.
    * `BIBLIOTECARIO`: Gestão de acervo e empréstimos.
    * `LEITOR`: Consulta de livros e solicitações de reserva.
* **Lógica de Inventário:** O sistema impede empréstimos de livros com `availableQuantity` igual a zero, sugerindo automaticamente a criação de uma reserva.

## 📊 Estatísticas (Work in Progress)
Em conformidade com o item 3.6 da EAP, o sistema está a ser preparado para exibir métricas de utilização, como os títulos mais lidos e taxa de rotatividade de exemplares.

---
**Instituição:** Universidade Mogi das Cruzes (UMC)  
**Semestre:** 8º Semestre - 2026
