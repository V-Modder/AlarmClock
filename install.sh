#!/bin/bash
set -e

desktopPath=$(echo ~)/Desktop
[ -e AlarmClock.jar ] && rm AlarmClock.jar
[ -e alarm_clock.png ] && rm alarm_clock.xpm
[ -e ${desktopPath}/AlarmClock.desktop ] && rm ${desktopPath}/AlarmClock.desktop

wget -O AlarmClock.jar  https://raw.githubusercontent.com/V-Modder/AlarmClock/master/bin/AlarmClock.jar
wget -O alarm_clock.xpm https://raw.githubusercontent.com/V-Modder/AlarmClock/master/bin/alarm_clock.xpm

jarPath=$(realpath AlarmClock.jar)
pngPath=$(realpath alarm_clock.xpm)
workingDir=$(realpath .)

desktopContent=$(cat <<EOF
[Desktop Entry]
 Name=Alarm-Clock
 Comment=Simple alarm clock
 Icon=${pngPath}
 Exec=java -jar ${jarPath}
 Path=${workingDir}
 Type=Application
 Encoding=UTF-8
 Terminal=false
 Categories=Application
EOF
)
echo "${desktopContent}" > ${desktopPath}/AlarmClock.desktop

exec java -jar ${jarPath} &