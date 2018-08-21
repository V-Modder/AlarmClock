#!/bin/bash

desktopPath=$(~)
[ -e AlarmClock.jar ] && rm AlarmClock.jar
[ -e alarm_clock.png ] && rm alarm_clock.png
[ -e ${desktopPath}/AlarmClock.desktop ] && rm ${desktopPath}/AlarmClock.desktop

wget -O AlarmClock.jar https://github.com/V-Modder/AlarmClock/blob/master/bin/AlarmClock.jar
wget -O alarm_clock.png https://github.com/V-Modder/AlarmClock/blob/master/bin/alarm_clock.png

jarPath=$(realpath AlarmClock.jar)
pngPath=$(realpath alarm_clock.png)

echo << EOF
[Desktop Entry]
Encoding=UTF-8
Version=1.0
Name[en_US]=AlarmClock
GenericName=Alarm Clock
Exec=java -jar ${jarPath}
Terminal=false
Icon[en_US]=${pngPath}
Type=Application
Categories=Application
Comment[en_US]=Simple alarm clock
EOF > ${desktopPath}/AlarmClock.desktop

exec java -jar ${jarPath} &