release: sh -c 'cd src/main/resources/ && rm application.properties && mv application.properties_heroku application.properties && cd -'
web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar -Dspring.profiles.active=prod
