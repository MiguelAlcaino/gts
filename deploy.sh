rm -v $CATALINA_HOME/webapps/track.war
ant track
cp -v build/track.war $CATALINA_HOME/webapps/


