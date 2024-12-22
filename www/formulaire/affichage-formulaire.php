

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <header>
        <?php include("include/header.php"); ?>
    </header>

    <main>
        <section>
        <?php
            if (isset($_GET["nom"])) {
                echo "GET: ".$_GET["nom"];
            }

            if (isset($_POST["nom"])) {
                echo "POST: ".$_POST["nom"];
            }

            ?>
        </section>
    </main>

    <footer>
        <?php include("include/footer.php"); ?>
    </footer>


</body> 
</html>