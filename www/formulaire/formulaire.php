<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <link rel="stylesheet" href="style.css">
    <title>Document</title>
</head>
<body>
    <header>
        <?php include("include/header.php"); ?>
    </header>

    <main>
        <section>
            <!-- Formulaire POST -->
            <h1>Formualaire POST</h1>
            <!-- <form action="affichage-formulaire.php?nom=hooooo" method="post"> -->
            <form action="affichage-formulaire.php" method="post">
                <input type="text" name="nom" id="nom">
                <input type="submit" value="Envoyer">
            </form>

            <!-- Formulaire GET -->
            <h1>Formaulaire GET</h1>
            <form action="affichage-formulaire.php" method="get">
                <input type="text" name="nom" id="nom">
                <input type="submit" value="Envoyer">
            </form>
        </section>
    </main>

    <footer>
        <?php include("include/footer.php"); ?>
    </footer>

    <script src="main.js"></script>
</body> 
</html>