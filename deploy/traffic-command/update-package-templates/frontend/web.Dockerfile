ARG BASE_IMAGE
FROM ${BASE_IMAGE}

RUN rm -rf /opt/dist/*
COPY artifacts/web-static/ /opt/dist/
