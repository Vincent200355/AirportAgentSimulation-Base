# Simulation
## Simulationsparameter
Die Einheiten Simulationsparameter, die hier festgelegt werden, erlauben es den Plugins ein einheitliches Verständnis der Umwelt zu erlangen.
* 1 LE ≙ 1 mm
* 1 Iteration ≙ 1 sec

## Simulationskonfiguration
Die Ausgangssituation einer Simulation wird über eine Konfigurationsdatei im JSON-Format.
Die Datei wird beim Einlesen akribisch auf Formatfehler geprüft, aus diesem Grund sollten in Fällen der händischen Erstellung dieser Datei das Folgende Format eingehalten werden.


```json
{
  "seed": 1234567,
  "width": 1000,
  "height": 1000,
  "placedEntities": [
    {
      "type": "PluginID_1",
      "position": [
        2,
        2
      ],
      "width": 10,
      "height": 8,
      "generates": [
        {
          "type": "PluginID_2",
          "generationRate": 7
        },
        ...
      ],
      "pluginAttributes": {}
    }
    ...
  ]
}

```

| Attribut | Beschreibung |
|----------|--------------|
| `"seed"`   ||
| `"width"`  ||
| `"height"` ||
| `"placedEntities"` ||
|||
| `"type` ||
| `"possition` ||
| `"width"`  ||
| `"height"` ||
| `"generates"` ||
|||
| `"type"` ||
| `"generationRate"`
