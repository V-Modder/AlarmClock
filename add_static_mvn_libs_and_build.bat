@echo off
echo adding sound libs
call mvn install:install-file -Dfile=lib\jl-1.0.1.jar -DgroupId=com.googlecode.soundlibs -DartifactId=jlayer -Dversion=1.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile=lib\mp3spi-1.9.5.jar -DgroupId=com.googlecode.soundlibs -DartifactId=mp3spi -Dversion=1.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile=lib\tritonus_share-1.0.0.jar -DgroupId=com.googlecode.soundlibs -DartifactId=tritonus-share -Dversion=1.0.0 -Dpackaging=jar

echo adding virtual keyboard libs
call mvn install:install-file -Dfile=lib\AbsoluteLayout.jar -DgroupId=org.netbeans.lib.awtextra -DartifactId=AbsoluteLayout -Dversion=1.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile=lib\Virtualkeyboard.jar -DgroupId=virtualkeyboard -DartifactId=Virtualkeyboard -Dversion=1.0.0 -Dpackaging=jar

echo adding office lib
call mvn install:install-file -Dfile=lib\ews-java-api-2.1-SNAPSHOT.jar -Dcom.microsoft.ews-java-api -DartifactId=ews-java-api -Dversion=2.1-SNAPSHOT -Dpackaging=jar

echo building Alarmclock
call mvn clean install

echo building bin dir
IF EXIST bin rmdir /s /q bin
mkdir bin
move target\AlarmClock.jar bin\AlarmClock.jar
copy src\main\resources\alarm_clock.xpm bin\alarm_clock.xpm
pause