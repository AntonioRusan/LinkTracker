FROM openjdk:21

ENV TELEGRAM_BOT_TOKEN=${TELEGRAM_BOT_TOKEN}
ENV GITHUB_BASE_URL=${GITHUB_BASE_URL}
ENV STACK_OVERFLOW_BASE_URL=${STACK_OVERFLOW_BASE_URL}
ENV BOT_API_URL=${BOT_API_URL}
ENV SCRAPPER_API_URL=${SCRAPPER_API_URL}
ENV POSTGRES_HOST=${POSTGRES_HOST}
ENV KAFKA_URL=${KAFKA_URL}

WORKDIR /app
COPY ./target/scrapper.jar /app/scrapper.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/scrapper.jar"]
