# Projet d'Observabilité et Logging

#### Par GAIDO Tristan et VIGUIER Enzo

Ce projet implémente un exemple de logging d'une application Java type "e-commerce" permettant d'identifier différents profils utilisateurs.   

## Prérequis
- Java JDK installé

## Instructions pour exécuter le projet

### 1. Télécharger ou cloner le projet
Vous pouvez soit télécharger l'archive ZIP du projet, soit cloner le dépôt Git si disponible.

Le ZIP contient tout le code source ainsi que toutes les classes compilées.

### 2. Exécution du projet
Pour tester le projet, suivez ces étapes :

1. Ouvrez un terminal ou une invite de commandes.
2. Déplacez-vous dans le dossier du projet `/TP3-Logging/`.
3. Exécutez la commande suivante pour mettre a jour ou installer les dépendances Maven :

Commande d'exécution :
`mvn clean install`

Ensuite, pour tester le projet, éxécutez la commande suivante, toujours a la racine du dossier :

Commande d'exécution :
`java -jar target/TP3-Logging-1.0-SNAPSHOT.jar`

### 3. Résultats
Après éxécution de la commande précédente, vous être présénté par un menu en CLI.  
Vous pouvez générer différents scénarios de tests en choississant l'option 6 (recommandé), ou bien vous pouvez interagir avec l'application directement en ajoutant, supprimant ou modifiant des produits.
  
Vous pouvez ensuite accèder aux différents profils généré dans le fichier `user_profiles.json` dans `TP3-Logging/profiles/user_profiles.json`


