
Minigrow APIs are based on three kind of objects: `sensors`, `actuators` and `commands`. While the first two reflect real hardware devices with their own readings, the command is an abstraction used to drive such devices.

The spring-boot server exposes REST API to exchange such devices with JSON representation and keeps a history. Since this is a personal home project, no additional security nor login features are provided. The spring server may be used optionally, as the [Minigrowl-ESP](https://shineangelic.github.io/Minigrowl-ESP-LoRa32-OLED/) already implements some basic logic, but you'll need this in order to archive logs and serve a [react-client](https://github.com/shineangelic/Minigrowl-react)

# Client APIs
```
   /api/minigrowl/v1/commands
   /api/minigrowl/v1/sensors
   /api/minigrowl/v1/actuators
   /api/minigrowl/v1/commands/queue/add
```

# ESP32 APIs
```
   /api/esp/v1/actuators/id/{id}
   /api/esp/v1/sensors/id/{id}
   /api/esp/v1/commands
   /api/minigrowl/v1/commands/queue/add
```

# Websockets
```
   /topic/actuators/
   /topic/sensors/
```


## Command example:
```
[
    {
        "name": "Turn ON intake Fan",
        "val": "1",
        "tgt": 2
    },
    {
        "name": "Turn intake Fan OFF",
        "val": "0",
        "tgt": 2
    },
    {
        "name": "Set Temperature",
        "val": "2",
        "tgt": 25
    },
    {
        "name": "Turn ON Hvac",
        "val": "1",
        "tgt": 25
    },
    {
        "name": "Turn OFF Hvac",
        "val": "0",
        "tgt": 25
    },
    {
        "name": "Switch lights ON",
        "val": "1",
        "tgt": 12
    },
    {
        "name": "Switch lights OFF",
        "val": "0",
        "tgt": 12
    },
    {
        "name": "Turn ON outtake Fan",
        "val": "1",
        "tgt": 13
    },
    {
        "name": "Turn OFF outtake Fan",
        "val": "0",
        "tgt": 13
    }
]
```

## Sensors example:
```
[
    {
        "id": 17,
        "typ": "HUMIDITY",
        "val": "25.57002",
        "uinit": "PERCENT",
        "timeStamp": "2020-04-11T13:22:34.205+0000",
        "err": false
    },
    {
        "id": 21,
        "typ": "BAROMETER",
        "val": "1012.763",
        "uinit": "MILLIBAR",
        "timeStamp": "2020-04-11T13:22:20.353+0000",
        "err": false
    },
    {
        "id": 22,
        "typ": "TEMPERATURE",
        "val": "26.12",
        "uinit": "CELSIUS",
        "timeStamp": "2020-04-11T13:22:48.155+0000",
        "err": false
    },
    {
        "id": 33,
        "typ": "LIGHT",
        "val": "673",
        "uinit": "LUMEN",
        "timeStamp": "2020-04-11T13:23:02.023+0000",
        "err": false
    }
]
```

## Actuators example (w/ supported commands)
```
[
    {
        "id": 2,
        "typ": "FAN",
        "uinit": "CELSIUS",
        "timeStamp": "2020-04-11T13:26:25.983+0000",
        "val": "1",
        "err": false,
        "cmds": [
            {
                "name": "Turn ON intake Fan",
                "val": "1",
                "tgt": 2
            },
            {
                "name": "Turn intake Fan OFF",
                "val": "0",
                "tgt": 2
            }
        ]
    },
    {
        "id": 12,
        "typ": "LIGHT",
        "uinit": "CELSIUS",
        "timeStamp": "2020-04-11T13:25:58.109+0000",
        "val": "1",
        "err": false,
        "cmds": [
            {
                "name": "Switch lights ON",
                "val": "1",
                "tgt": 12
            },
            {
                "name": "Switch lights OFF",
                "val": "0",
                "tgt": 12
            }
        ]
    },
    {
        "id": 13,
        "typ": "FAN",
        "uinit": "CELSIUS",
        "timeStamp": "2020-04-11T13:26:11.987+0000",
        "val": "0",
        "err": false,
        "cmds": [
            {
                "name": "Turn ON outtake Fan",
                "val": "1",
                "tgt": 13
            },
            {
                "name": "Turn OFF outtake Fan",
                "val": "0",
                "tgt": 13
            }
        ]
    },
    {
        "id": 25,
        "typ": "HVAC",
        "uinit": "CELSIUS",
        "timeStamp": "2020-04-11T13:26:21.342+0000",
        "val": "1",
        "err": false,
        "cmds": [
            {
                "name": "Set Temperature",
                "val": "2",
                "tgt": 25
            },
            {
                "name": "Turn ON Hvac",
                "val": "1",
                "tgt": 25
            },
            {
                "name": "Turn OFF Hvac",
                "val": "0",
                "tgt": 25
            }
        ]
    }
]
```
## Send Command example
PUT on /api/minigrowl/v1/commands/queue/add, with payload like the following (it must be a supported command seen above)
```
{
        "name": "Turn intake Fan OFF",
        "targetActuator": 2,
        "val": "0"
}
```



