# Correctif des effets empilés de Porting Lib

Mod Fabric 1.21.1 pour le serveur de nistroy, avec Twilight Forest `4.8.629` (qui embarque Porting Lib
`3.1.0-beta.90`).

## Le problème

Quand un effet plus fort recouvre le même effet plus faible (Régénération II par-dessus Régénération I), Minecraft
garde l'effet faible « caché » pour le reprendre à la fin du fort. Si l'entité a été rechargée entre-temps (joueur qui
se reconnecte, mob d'un tronçon rechargé), Porting Lib plante à ce moment-là :

- sur un joueur : « Internal server error », le joueur est éjecté ;
- sur un mob : le serveur s'arrête.

## Le correctif

Le même que [Porting-Lib#202](https://github.com/Fabricators-of-Create/Porting-Lib/pull/202) (fusionné, mais pas
encore dans une version de Twilight Forest) : la liste des remèdes de l'effet est remplacée par une copie modifiable
avant que Porting Lib la vide. Il suffit côté serveur.

À retirer dès qu'une version de Twilight Forest embarque le correctif : le mod refuse de se charger avec une autre
version de Porting Lib.

## Tests

```
./gradlew build runGameTest
```
