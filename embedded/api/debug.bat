@echo off
setlocal

set CP=target\glassfish-embedded-api-3.0-Prelude-SNAPSHOT.jar
set CP=%CP%;..\packager\all\target\final.jar

set DEB=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1323

java %DEB% -cp "%CP%" org.glassfish.embed.EmbeddedMain %*

endlocal
