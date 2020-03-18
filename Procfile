release: sh -c 'cd src/main/resources && mysql -u root eco_renter < schema-mysql.sql && mysql -u root eco_renter < data-mysql.sql'
web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar -Dspring.profiles.active=prod
