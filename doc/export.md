# Daten-Export

## Specs
- Format: `.csv`

## Header Block
1. World-Config: `0;0;0;world;0;0;10;10;;` (Welt mit 10x10 Felder)
2. Start-Entities: 
   ```csv
   1;0;5;wall;1;1;2;3;  # (`wall` mit Position (1,1) und Größe (2,3))
   ...
   ```

## Beispiel
```csv
lNo;tick;entityId;entityType;posX;posY;wX;wY;messages
0;0;0;world;0;0;10;10;;
1;0;5;wall;1;1;2;3;;
```