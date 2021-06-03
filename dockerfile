FROM openjdk:8

# make work directory
RUN mkdir -p /opt/ms \
    && chmod 777 -R /opt/ms

# copy service
COPY ./target/ms-1.0.0.jar /opt/ms/
COPY ./script/run.sh /opt/ms/
RUN chmod 500 /opt/ms/*

# change owner
RUN chown root:root -R /opt/ms

EXPOSE 18080

WORKDIR /opt/ms
CMD ["sh", "run.sh"]

