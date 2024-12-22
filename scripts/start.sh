#!/bin/bash

INSTALL_FLAG=".install_done"

if [ ! -f "$INSTALL_FLAG" ]; then
    echo "=== Bienvenue dans l'installation de votre serveur web ==="
    echo "Vérification des dépendances..."

    if ! command -v php-cgi >/dev/null 2>&1; then
        echo "php-cgi n'est pas installé. Installation..."
        sudo apt update && sudo apt install php-cgi -y
    else
        echo "php-cgi est déjà installé."
    fi

    if ! command -v javac >/dev/null 2>&1; then
        echo "JDK n'est pas installé. Installation..."
        sudo apt update && sudo apt install openjdk-21-jdk -y
    else
        echo "JDK est déjà installé."
    fi

    touch "$INSTALL_FLAG"
    echo "Installation terminée. Vous pouvez désormais démarrer votre serveur !"
else
    echo "L'installation a déjà été effectuée. Démarrage du serveur..."
fi

cd "$(dirname "$0")/../src"
find . -type f -name "*.java" > sources.txt
javac -d ../bin @sources.txt

echo "Démarrage du serveur web..."
cd ../bin
java server.Launcher
