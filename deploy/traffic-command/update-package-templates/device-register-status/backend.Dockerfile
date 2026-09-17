ARG BASE_IMAGE
FROM ${BASE_IMAGE}

COPY artifacts/wvp.jar /opt/wvp/wvp.jar
