# EventFlow Angular + Spring Boot + PostgreSQL

## Description

Ce projet est une application web avec un **front-end Angular**, un **back-end Spring Boot**, et une **base de données PostgreSQL**. L'application permet de gérer des événements et des utilisateurs. L'authentification et la gestion des utilisateurs sont intégrées.

### Fonctionnalités :

- **Front-End** : Angular
- **Back-End** : Spring Boot
- **Base de données** : PostgreSQL
- **Authentification** : JWT (JSON Web Token)

---

## Architecture

1. **Front-End** : 
   - Développé en Angular
   - Permet aux utilisateurs de s'inscrire, de se connecter, de voir et de créer des événements.
   
2. **Back-End** :
   - Développé en Spring Boot 
   - Authentification via JWT.

3. **Base de données** :
   - PostgreSQL est utilisé pour stocker les données utilisateurs et événements.


---

## Prérequis

Avant de démarrer, assurez-vous que vous avez installé les outils suivants :

- [Node.js](https://nodejs.org/) (pour le front-end Angular)
- [Angular CLI](https://angular.io/cli) (pour gérer le projet Angular)
- [Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (pour le back-end Spring Boot)
- [Maven](https://maven.apache.org/) (pour gérer le projet Spring Boot)
- [PostgreSQL](https://www.postgresql.org/) (base de données)

---

## Installation et Démarrage

### 1. Démarrer le Back-End (Spring Boot)

1. Clonez le projet back-end :

```bash
git clone  https://github.com/Elabadi17/EventFlow.git
```

### 2. Back-End (Spring Boot)



1. Configurer la base de données dans `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/event_management
spring.datasource.username=event_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

2. Lancer l'application :
```bash
mvn spring-boot:run
```

Le serveur démarrera sur `http://localhost:8080`

### 3. Front-End (Angular)


1. Installer les dépendances :
```bash
npm install
```

2. Lancer l'application :
```bash
ng serve
```

L'application sera accessible sur `http://localhost:4200`



---




