FROM ampirux/openjdk:17

COPY ./.docker/entrypoint.sh /entrypoint.sh
COPY build/libs/*.jar app.jar
RUN chmod +x /entrypoint.sh

EXPOSE 8080
ENTRYPOINT ./entrypoint.sh