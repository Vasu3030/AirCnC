# Projet : Plateforme de location de logements AirCnc

## Description
Ce projet académique est une plateforme de location de logements similaire à Airbnb. Il permet aux utilisateurs d'ajouter des logements à louer, d'effectuer des réservations et de laisser des commentaires. Un système de rôles est mis en place pour gérer les droits d'accès : utilisateur classique, propriétaire et administrateur.

## Technologies utilisées
- **Frontend** : ReactJS
- **Backend** : Java SpringBoot
- **Base de données** : MySQL
- **Authentification** : JWT (JSON Web Token)
- **ORM** : Sequelize

## Fonctionnalités
### Pour un utilisateur non connecté
- Créer un compte utilisateur
- Se connecter
- Consulter les adresses disponibles à la location
- Lire les commentaires

### Pour un utilisateur connecté
- Modifier son profil
- Supprimer son compte
- Ajouter une adresse à louer
- Modifier ou supprimer ses propres adresses
- Faire une demande de réservation pour une période donnée
- Annuler une réservation
- Accepter ou refuser une demande de réservation pour son logement
- Commenter une adresse

### Pour un administrateur
- Modifier et supprimer les profils utilisateurs
- Modifier et supprimer toutes les adresses
- Supprimer les commentaires


## Modèle de base de données
La base de données comprend au minimum **quatre tables** :
1. `user` : Stocke les informations des utilisateurs (nom, email, rôle, etc.)
2. `address` : Contient les logements disponibles à la location
3. `bookin` : Gère les demandes de réservation avec leur statut
4. `commentaries` : Stocke les commentaires laissés par les utilisateurs

## Sécurité et permissions
- L'authentification est gérée par JWT.
- Les utilisateurs ne peuvent modifier ou supprimer que leurs propres données.
- Un administrateur a des droits de modification/suppression globaux.

## Tests et validation
Le projet inclut une suite de tests unitaires et d'intégration, garantissant le bon fonctionnement des principales fonctionnalités liées aux utilisateurs. Ces tests, réalisés avec JUnit 5 et Spring Boot Test, utilisent MockMvc pour simuler des requêtes HTTP et valider les réponses de l'API.
