# TerryScape Server
A websocket based server written in Java attempting to recreate a "OldSchool Runescape" style game. 

This is just the server - the client is written in C# and SBox and currently not public. The client can be downloaded and decompiled from S&Box. The client will be made public soon.

## Small overview of server architecture
- Websocket based networking with packet system.
- Sever authoriative. The server simulates all game logic. Each client is just responsible for rendering.
- Broken down into separate modules:
  -  cache
      -  Similar to Oldschool Runescape, a text based game cache to store game data. Most content is driven by a cache. E.g. world data (think walkable tiles or npc spawns), npcs and shops.
  -  content
      - "Game Logic" is implemented in modules inside this module. Should be easy to add new game logic - just make a new module - and remove existing game logic - just delete the module.
      - For example the "food" content module implements the game logic for eating food and healing.
  -  engine
      - The API for content modules.
  -  engine-internal
      - The internal code for the API found in the engine module.

## Getting Started
- Very standard Gradle application
- Build and Run `com.terryscape.ServerLauncher`

## Features / Media

### Fully Networked (Server authoritative)
https://github.com/user-attachments/assets/ae1b0f60-8e8f-4399-8ce4-658c6ec5d06e

### Melee Combat
https://github.com/user-attachments/assets/da1ed5ee-c852-4415-8388-9e9f2af8734a

### Ranged/Magic Combat
https://github.com/user-attachments/assets/a484c713-54b2-4f9b-8874-b59b9dd4a5fa

### A* Pathfinding
https://github.com/user-attachments/assets/97f86156-6b48-4ebe-bfda-f6537773738f

### Inventory, Items, and Item stats
https://github.com/user-attachments/assets/9336a197-9d6b-4dbf-b796-bb97cd433143

### Shops
https://github.com/user-attachments/assets/5f6ddc6e-0023-4b58-8382-c5d8466b8b30

### Dialogue
https://github.com/user-attachments/assets/38d47f22-983d-46dd-aa20-56dcb5a62068

### World Object Interaction
https://github.com/user-attachments/assets/e63b7c84-944e-4459-98b9-d87d61c97074

### Ground Items
https://github.com/user-attachments/assets/5d372afa-ddfd-484b-9936-f0264cbd69bf

### Bossfight, Death, and Respawning
https://github.com/user-attachments/assets/fcaa7041-1114-44bf-93e5-37bfb3314f27
