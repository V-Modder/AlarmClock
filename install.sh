#!/bin/bash
set -e

desktopPath=$(echo ~)/Desktop
[ -e AlarmClock.jar ] && rm AlarmClock.jar
[ -e alarm_clock.png ] && rm alarm_clock.png
[ -e ${desktopPath}/AlarmClock.desktop ] && rm ${desktopPath}/AlarmClock.desktop

wget -O AlarmClock.jar https://github.com/V-Modder/AlarmClock/blob/master/bin/AlarmClock.jar
wget -O alarm_clock.png https://github.com/V-Modder/AlarmClock/blob/master/bin/alarm_clock.png

jarPath=$(realpath AlarmClock.jar)
pngPath=$(realpath alarm_clock.png)

desktopContent=$(cat <<EOF
[Desktop Entry]
 Name=Alarm-Clock
 Comment=Simple alarm clock
 Icon=${pngPath}
 Exec=java -jar ${jarPath}
 Type=Application
 Encoding=UTF-8
 Terminal=false
 Categories=Application
EOF
)
echo "${desktopContent}" > ${desktopPath}/AlarmClock.desktop

exec java -jar ${jarPath} &