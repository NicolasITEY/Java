# Projet Échecs – Programmation Orienté Objets Java/JavaFX

Jeu d'échecs complet développé en Java 21 avec JavaFX 24, construit selon le patron MVC. Le projet inclut un mode jeu classique, un éditeur de plateau personnalisé, un solveur N-Reines, ainsi qu'un système de sauvegarde/chargement au format JSON.

---

## Prérequis

| Outil | Version minimale |
|-------|-----------------|
| Java (JDK) | 21 |
| Apache Maven | 3.8+ |
| JavaFX | fourni via Maven (24.0.2) |

> Vérifiez votre installation : `java -version` et `mvn -version`

---

## Compilation

Depuis la racine du projet (là où se trouve `pom.xml`) :

```bash
mvn clean package
```

---

## Exécution

### Avec le plugin JavaFX Maven (recommandé)

```bash
mvn javafx:run
```

> **Note :** En passant par `mvn javafx:run`, le classpath et le module-path sont gérés automatiquement. 

---

## Structure du projet

```
.
├── pom.xml                   # Configuration Maven
├── saves/                    # Sauvegardes de parties (JSON + FEN)
└── src/
    └── main/
        ├── java/
        │   ├── app/          # Point d'entrée (Main.java)
        │   ├── controller/   # Contrôleurs MVC
        │   ├── core/         # Logique métier (plateau, pièces, jeu)
        │   │   └── pieces/   # Classes des pièces
        │   ├── model/        # Modèle de données
        │   ├── mvc/          # Interfaces MVC abstraites
        │   └── view/         # Vues JavaFX
        └── resources/
```

---

## Dépendances principales

| Dépendance | Version | Rôle |
|------------|---------|------|
| `javafx-controls` | 24.0.2 | Interface graphique |
| `javafx-graphics` | 24.0.2 | Rendu graphique |
| `gson` | 2.10.1 | Sérialisation JSON (sauvegardes) |
| `gson-extras` | 2.13.2-rc1 | Extensions Gson |

---

## Fichiers de sauvegarde

Les parties sont sauvegardées dans le dossier `saves/` au format JSON. La sauvegarde automatique s'effectue dans `autosave.json`. Des emplacements nommés (`save0.json`, `save1.json`, `Game.json`) sont également disponibles. Le fichier `custom_layout.txt` stocke les dispositions personnalisées de plateau en notation FEN.

---