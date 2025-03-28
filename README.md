# HTTP Server

**Auteur**: ETU003614 - ETU003235
## Description

Ce projet implémente un serveur HTTP configurable, écrit en Java. Il inclut une interface en ligne de commande qui permet de gérer le serveur et de modifier sa configuration en temps réel.
## Prérequis

    Système d'exploitation : Linux (Ubuntu recommandé)
    Dépendances requises :
        php-cgi
        openjdk-21-jdk
    Fichier de configuration : server.conf

## Installation

    Cloner le projet :

git clone <repository-url>
cd <repository-directory>

## Rendre le script exécutable :

chmod +x scripts/start.sh

## Exécuter le script d'installation et de démarrage :

    ./scripts/start.sh

## Commandes de l'Interface en Ligne de Commande

Lorsque le serveur est en cours d'exécution, vous pouvez interagir avec lui via l'interface en ligne de commande. Les commandes disponibles sont les suivantes :
### Commandes Disponibles
Commande	Description
stop	Arrête le serveur.
show config	Affiche la configuration actuelle du serveur :
	- PHP activé/désactivé
	- Répertoire public
	- Port d'écoute
edit config	Permet de modifier la configuration du serveur en temps réel. Vous serez invité à entrer :
	- enable_php : Activer/désactiver PHP (true/false)
	- public_directory : Chemin vers le répertoire public
	- port : Numéro du port d'écoute
Sauvegarde de la Configuration

Les modifications effectuées via la commande edit config sont automatiquement sauvegardées dans le fichier server.conf.