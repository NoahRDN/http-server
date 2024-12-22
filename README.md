# http-server
Auteur: ETU003614 - ETU003235

Structure du Projet

src/ : Contient le code source Java

bin/ : Contiendra les fichiers compilés

scripts/ : Contient le script d'installation et de démarrage (start.sh)

Commandes de l'interface en ligne de commande

Lorsque le serveur est en cours d'exécution, vous pouvez interagir avec lui via une interface en ligne de commande.
Commandes disponibles :

    stop : Arrête le serveur.

    show config : Affiche la configuration actuelle du serveur (PHP activé/désactivé, répertoire public, port).

    edit config : Permet de modifier la configuration du serveur en temps réel. Vous serez invité à entrer les nouvelles valeurs pour :
        enable_php (true/false)
        public_directory (chemin vers le répertoire public)
        port (numéro de port)

Après avoir effectué des modifications, la configuration est automatiquement sauvegardée dans le fichier server.conf.