# Jeu de stratégie au tour par tour

Devoir de contrôle continu Méthodes de conception 

Amara DOUMBOUYA & Fatoumata Françoise TOUNKARA

Jeu de combat sur une grille 2D de taille paramétrable, entre 2 et n robots. Chaque combattant a de l'énergie et des munitions (mines, bombes, tirs). À chaque tour, il se déplace, pose une mine ou une bombe


## Arborescence

```
projet_jeu_strategie/
├── build.xml          construction avec Ant (jar, tests, javadoc)
├── construire.ps1     construction sans Ant (PowerShell)
├── lib/               junit-platform-console-standalone.jar (pour les tests)
└── livraison/
    ├── src/           code source commenté
    ├── test/          tests JUnit 5
    ├── doc/           Javadoc
    ├── dist/          jeu-strategie.jar + jeu.properties (paramètres)
    └── rapport/       rapport PDF, diagramme draw.io et images
```

## Lancer le jeu

Depuis `livraison/dist` (le fichier `jeu.properties` du dossier courant est lu au démarrage) :

```bash
java -jar jeu-strategie.jar
```

```bash
java -jar jeu-strategie.jar --console
```

```bash
java -jar jeu-strategie.jar --tournoi 1000
```


## Construire

```bash
ant livraison
```

ou, sans Ant :

```bash
.\construire.ps1 -Javadoc
```

### Règles choisies : 

- Un tir part dans les deux sens, touche tous les combattants à une distance strictement inférieure à la portée, et s'arrête au premier mur.

- Une bombe explose quand son compte à rebours arrive à 0 (fin de tour) ou quand on marche dessus ; elle touche sa case et les 8 voisines. Une mine ne touche que sa case.

- Le bouclier protège des tirs et des bombes (pas des mines) jusqu'à la fin du tour suivant.

- Une action impossible fait perdre le tour (par exemple poser sur une case où se cache un explosif invisible).

- Au bout de `toursMax` tours, celui qui a le plus d'énergie gagne (personne en cas d'égalité).

## Tests

Nous avons effectué : 10 tests JUnit 5 dans `livraison/test`, sur trois classes

Trouvable dans la classe Test. 



# D'après nos remarques. 
La stratégie prudente gagne le plus souvent alors qu'elle pilote le combattant le plus fragile : elle laisse les chasseurs s'affronter et s'épuiser, puis achève les survivants. Avec la configuration précédente (un robot aléatoire au lieu d'un troisième chasseur), la stratégie aléatoire ne gagnait qu'environ 1 % des parties.



