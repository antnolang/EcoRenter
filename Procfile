release: sh -c "cd src/main/resources/ && psql -U postgres eco_renter < sql/schema.sql && psql -U postgres eco_renter < sql/data.sql"
web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar -Dspring.profiles.active=prod
