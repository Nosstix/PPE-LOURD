Architecture du projet Java

## 1. Point d'entrée — Main.java

L'application démarre dans `Main.java` (package `vue`). La méthode `main()` fait deux choses :
- Elle applique le thème visuel via `Theme.apply()`
- Elle lance la fenêtre de connexion `VueConnexion` via `SwingUtilities.invokeLater()`

C'est donc la vue de connexion qui s'affiche en premier. Tout le reste se déclenche depuis là.

---

## 2. Configuration — AppConfig.java & config.properties

Toute la config est centralisée dans un fichier `config.properties` chargé au démarrage par `AppConfig.java`. Il doit être placé dans `src/main/resources/config.properties`.

Contenu minimal :
```properties
db.host=localhost
db.port=3306
db.name=nom_de_ta_base
db.user=root
db.password=ton_mot_de_passe

table.user=utilisateur
table.categorie=categorie
table.marque=marque
table.vehicule=vehicule
table.vente=vente
table.config=config

col.user.id=id
col.user.nom=nom
col.user.prenom=prenom
col.user.email=email
col.user.mdp=mdp
col.user.role=role
col.user.discord=discord
```

⚠️ Si le fichier est absent ou qu'une clé manque, l'application plante au démarrage.

---

## 3. Connexion base de données — BDD.java

`BDD.java` lit les paramètres depuis `AppConfig` et construit une URL JDBC de la forme :
```
jdbc:mysql://localhost:3306/ma_base?useSSL=false&serverTimezone=Europe/Paris
```
La méthode `getConnection()` crée une nouvelle connexion à chaque appel. Il faut donc toujours la fermer après usage avec un `try-with-resources`.

---

## 4. Noms des tables & colonnes — Schema.java

`Schema.java` expose des méthodes statiques qui retournent les noms des tables et colonnes depuis la config. Ça évite d'écrire les noms SQL en dur dans le code. Exemple : `Schema.tUser()` retourne la valeur de `table.user`.

---

## 5. Couches Modèle & Contrôleur

**Modele.java** — contient toutes les requêtes SQL. C'est lui qui instancie `BDD` pour obtenir une connexion et exécuter les requêtes.

**Controleur.java** — fait le lien entre la vue et le modèle. Exemple : `Controleur.login(email, mdp)` appelle `Modele.selectWhereUser()` et retourne un objet `User` si les identifiants sont corrects, ou `null` sinon.

**User.java** — classe représentant un utilisateur avec ses champs : `id, nom, prenom, email, mdp, role, discord`.

---

## 6. Flux de connexion (de bout en bout)

1. `VueConnexion` récupère l'email et le mot de passe saisis
2. Elle appelle `Controleur.login(email, mdp)`
3. Le contrôleur appelle `Modele.selectWhereUser(email, mdp)`
4. Le modèle crée un `new BDD()`, appelle `getConnection()` et exécute un SELECT
5. Si un utilisateur est trouvé → objet `User` retourné et interface principale affichée. Sinon → message d'erreur.

---

## 7. Prérequis

- Java 11+
- MySQL installé et lancé en local
- Connecteur JDBC MySQL dans les dépendances (`mysql-connector-java`) Dans IntelliJ <-- pour mysql:mysql-connector-java : File → Project Structure → Libraries Clique sur + → From Maven Tape : mysql:mysql-connector-java:8.0.33 (ou version superieur)
- `src/main/resources/config.properties` correctement rempli
- La base créée avec le script SQL fourni
