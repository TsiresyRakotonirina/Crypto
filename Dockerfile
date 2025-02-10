# Utiliser l'image de base OpenJDK pour Java 17
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /usr/app

# Copier le fichier WAR dans le conteneur
COPY target/crypto-0.0.1-SNAPSHOT.war /usr/app/crypto.war

COPY ./database/base.sql /docker-entrypoint-initdb.d/
COPY ./database/data.sql /docker-entrypoint-initdb.d/
COPY ./database/view.sql /docker-entrypoint-initdb.d/
# Exposer le port 8080 (le port utilisé par défaut dans Spring Boot)
EXPOSE 8080

# Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/usr/app/crypto.war"]
