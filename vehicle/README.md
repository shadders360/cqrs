#vehicle domain

* run built image standalone in memeory
docker run --name vehicle -e "SPRING_PROFILES_ACTIVE=default" -p 8080:8080 cqrs/vehicle:1.0.0-SNAPSHOT

* run built image with mysql need mysql container running
docker run --name vehicle -e "SPRING_PROFILES_ACTIVE=mysql" -p 8080:8080 cqrs/vehicle:1.0.0-SNAPSHOT