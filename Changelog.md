## <img width="20" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/github_vector.svg">  Common and General
- **Removed a ton of vibe coded code (Shame on me I know 🥲) and optimized a lot of code - May not be visible but it's certainly more stable.**
- Made a centralized menu logic that doesn't use any loader specific code - Code is easier to port to other versions and the mod behaves basically the same across loaders.
- Removed key tracking when you're anywhere but the game (Menus, Inventory, etc.).
- Config is unified across loaders.
- Made code more scalable for future features (Suggest [here](https://github.com/Tomkov1c/Slice/issues)).
- Removed hardcoded fallback values for custom values in texture packs.
- If `Click to Select` and `Close on Select` are enabled in `Hold mode`, the menu won't reappear until `Open Menu` key gets released.
- Made `calculateSlotPositions()` normalize the angle.
- Removed hardcoded slot amount. Now it's dynamic.
- Removed double checking in a bunch of functions.
- Added a new file naming scheme for builds.
- Removed junk.

## <img width="20" src="https://github.com/intergrav/devins-badges/blob/8494ec1ac495cfb481dc7e458356325510933eb0/assets/cozy-minimal/supported/neoforge_vector.svg?raw=true">  NeoForge

- Added `Recenter on Select` feature, that recenters your cursor in the menu when you select a slot.
- Renderer doesn't constantly reread json files inside texture packs when rendering.
- Renderer caches values that can't be changed when the menu is open.
- Keybinding state gets sent to `common`, where it gets processed correctly.
- Removed double checking in a bunch of functions.
- Removed potential reason why you might get banned on servers like Hypixel (RIP in peace Mark's SkyBlock account).

## <img width="20" src="https://badges.penpow.dev/badges/supported/forge/cozy-minimal.svg">  Forge

- Removed permission level for `/slice reloadClient` command.

## <img width="20" src="https://badges.penpow.dev/badges/supported/fabric/cozy-minimal.svg">  Fabric / <img width="20" src="https://badges.penpow.dev/badges/supported/quilt/cozy-minimal.svg"> Quilt
