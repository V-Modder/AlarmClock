#!/bin/bash
set -e


function main() {
  local install=$1
  local desktopPath=$(echo ~)/Desktop


  if [ $install -eq 0 ]; then
    echo "Updating install script"
    removeExistingFiles $desktopPath
    downloadInstallScript
    exec ./install.sh --install
  else
    echo "Updating application"
    downloadFiles
    local jarPath=$(realpath AlarmClock.jar)
    local iconPath=$(realpath alarm_clock.xpm)
    local workingDir=$(realpath .)
    createDesktopShortcut $jarPath $workingDir $iconPath $desktopPath
    execJar $jarPath
  fi
}

function downloadInstallScript() {
  echo "Downloading install script" 
  curl -s https://api.github.com/repos/V-Modder/AlarmClock/releases/latest \
    | grep "browser_download_url.*install.sh" \
    | cut -d '"' -f 4 \
    | wget -qi -
}


function downloadFiles() {
  echo "Downloading files"
  curl -s https://api.github.com/repos/V-Modder/AlarmClock/releases/latest \
  | grep "browser_download_url" \
  | grep -v "install.sh" \
  | cut -d '"' -f 4 \
  | wget -qi -
}

function removeExistingFiles() {
  echo "Removing existing files"
  local desktopPath=$1
  removeFileIfExists "AlarmClock.jar"
  removeFileIfExists "alarm_clock.xpm"
  removeFileIfExists "${desktopPath}/AlarmClock.desktop"
}

function removeFileIfExists() {
  if [ -e "${1}" ]; then
    echo "Removing file ${1}"
    rm ${1}
  fi
}

function createDesktopShortcut() {
  echo "Creating shortcut"
  local jarPath=$1
  local workingDir=$2
  local iconPath=$3
  local desktopPath=$4

  local content=$(cat <<EOF
[Desktop Entry]
Name=Alarm-Clock
Comment=Simple alarm clock
Icon=${iconPath}
Exec=java -jar ${jarPath}
Path=${workingDir}
Type=Application
Encoding=UTF-8
Terminal=false
Categories=Application
EOF
  )
  echo "${content}" > ${desktopPath}/AlarmClock.desktop
}

function execJar() {
  echo "Starting Alarmclock"
  local jarPath=$1
  exec java -jar ${jarPath} &
}

install=0
if [ "$1" = "--install" ]; then
  install=1
fi

main $install
