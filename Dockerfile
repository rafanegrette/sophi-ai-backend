FROM gradle:8.7.0-jdk21-alpine as build
LABEL authors="rafanegrette"
WORKDIR /workspace/app

COPY . /workspace/app

RUN apk add gcompat

RUN gradle clean build -x test
RUN mkdir -p configuration/build/dependency && (cd configuration/build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

FROM gradle:8.7.0-jdk21-alpine
VOLUME /tmp
WORKDIR /
ARG DEPENDENCY=/workspace/app/configuration/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.rafanegrette.books.SophiBooksApplication"]