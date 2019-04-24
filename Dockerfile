FROM docker.art.lmru.tech/maven:3.6.0-jdk-8-slim as build

WORKDIR /opt/app

COPY . .

RUN mvn clean package -s settings.xml -B -DskipTests

FROM tomcat:9-jre8-slim

ARG GIT_REPO
ARG GIT_COMMIT
ARG GIT_DATE

LABEL lm.img-puz-2.repo="$GIT_REPO" \
    lm.img-puz-2.commit="$GIT_COMMIT" \
    lm.img-puz-2.date="$GIT_DATE"

ENV LANG="en_US.UTF-8" \
    LANGUAGE="en_US.UTF-8" \
    LC_CTYPE="en_US.UTF-8" \
    LC_ALL="en_US.UTF-8" \
    JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=devlm"
COPY --from=build /opt/app/target/auth-server*.war /usr/local/tomcat/webapps/oauth-authorization-server.war