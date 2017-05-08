web: java -Dserver.port=$PORT $JAVA_OPTS \
          -Dgoogle_creds="$google_creds" \
          -Dcom.amazon.speech.speechlet.servlet.supportedApplicationIds=$ALEXA_APPLICATION_ID
          -javaagent:build/lib/newrelic.jar \
          -jar build/libs/cask-alexa.jar