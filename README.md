# ClickEventLib

Bibliothèque pour détecter les clics (gauche/droit) sur les entités et blocs dans Hytale.

Soit implémentation avec le .jar
Soit implémentation avec le dossier src directement (bien rename les imports avant le build)

WARNING : Le systeme de d'event de click du joueur est uniquement possible si il tien un objet dans les main et que cette objet a une interaction d'enregistrer, c'est a dire qu'il n'est pas possible avec ce code d'ecouter les inputs de souris sans item

## Installation

1. Ajouter `ClickEventLib-1.0.0.jar` dans le dossier `libs/` de votre plugin

2. Ajouter la dépendance dans `build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(files("libs/ClickEventLib-1.0.0.jar"))
   }
   ```

3. Mettre `"IncludesAssetPack": true` dans votre `manifest.json`

4. Copier les fichiers de `example-resources/` dans `src/main/resources/` en conservant la structure:
   ```
   src/main/resources/
   └── Server/
       └── Item/
           ├── Interactions/
           │   └── Item/
           │       └── VotrePlugin_ClickInteraction.json
           ├── Items/
           │   └── Ingredient/
           │       └── VotrePlugin_ClickStick.json
           └── RootInteractions/
               └── VotrePlugin_ClickStick.json
   ```

5. **Important**: Configurer votre `build.gradle.kts` pour inclure les ressources Server dans le JAR:
   ```kotlin
   sourceSets {
       main {
           resources {
               srcDir("src/main/resources")
               include("Server/**")  // <-- Ajouter cette ligne
               // ... vos autres includes
           }
       }
   }
   ```

6. Renommer les fichiers JSON avec le préfixe de votre plugin (ex: `MyPlugin_`) et mettre à jour les références internes

## Usage

```java
import fr.swordtales.clickevent.ClickEventRegistry;
import fr.swordtales.clickevent.PlayerClickEvent;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.server.core.Message;

// Dans setup()
ClickEventRegistry.init(this).register(event -> {
    if (event.isRightClick()) {
        if (event.hasEntityTarget()) {
            event.getPlayerRef().sendMessage(Message.raw("Entity: " + event.getTargetUUID()));
        } else if (event.hasBlockTarget()) {
            BlockPosition block = event.getTargetBlock();
            event.getPlayerRef().sendMessage(Message.raw("Block: " + block.x + ", " + block.y + ", " + block.z));
        } else {
            event.getPlayerRef().sendMessage(Message.raw("Air"));
        }
    }
});
```

7. Esnuite tout est pret, utiliser la commande /give MyPlugin_ClickStick et tester, les clicks seront détécter


## Handler personnalisé

Vous pouvez utiliser un nom de handler personnalisé:
```java
ClickEventRegistry.init(this, "MonPlugin_ClickHandler").register(event -> { ... });
```

Dans ce cas, modifiez le fichier JSON d'interaction:
```json
{
  "Type": "MonPlugin_ClickHandler"
}
```

## Méthodes disponibles sur PlayerClickEvent

- `isRightClick()` / `isLeftClick()` - Type de clic
- `hasTarget()` - A une cible (entité ou bloc)
- `hasEntityTarget()` - Cible est une entité
- `hasBlockTarget()` - Cible est un bloc
- `getTargetType()` - `ENTITY`, `BLOCK`, ou `NONE`
- `getTargetUUID()` - UUID de l'entité ciblée
- `getTargetBlock()` - Position du bloc ciblé
- `getHeldItem()` - Item tenu par le joueur
- `getPlayer()` - Le joueur
- `getPlayerRef()` - PlayerRef pour envoyer des messages
- `setCancelled(true)` - Annuler l'événement

## Structure des fichiers JSON

### Interaction (Server/Item/Interactions/Item/VotrePlugin_ClickInteraction.json)
```json
{
  "Type": "ClickEventLib_Handler"
}
```
Le `Type` doit correspondre au nom du handler utilisé dans `ClickEventRegistry.init()`.

### RootInteraction (Server/Item/RootInteractions/VotrePlugin_ClickStick.json)
```json
{
  "Interactions": [
    "VotrePlugin_ClickInteraction"
  ],
  "Settings": {
    "Creative": {}
  }
}
```
Le nom dans `Interactions` doit correspondre au nom du fichier d'interaction (sans .json).

### Item (Server/Item/Items/Ingredient/VotrePlugin_ClickStick.json)
```json
{
  "TranslationProperties": {
    "Name": "Click Stick",
    "Description": "Right-click to detect interactions"
  },
  "Icon": "Icons/ItemsGenerated/Ingredient_Stick.png",
  "Categories": ["Items.Tools"],
  "Model": "Items/Ingredients/Stick.blockymodel",
  "Texture": "Items/Ingredients/Stick_Texture.png",
  "PlayerAnimationsId": "Item",
  "ItemSoundSetId": "ISS_Items_Foliage",
  "Quality": "Developer",
  "Interactions": {
    "Primary": "VotrePlugin_ClickStick",
    "Secondary": "VotrePlugin_ClickStick"
  }
}
```
Les valeurs `Primary` et `Secondary` dans `Interactions` doivent correspondre au nom du fichier RootInteraction (sans .json)
