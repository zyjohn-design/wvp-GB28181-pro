ARG BASE_IMAGE=nginx:1.27-alpine
FROM ${BASE_IMAGE}

RUN rm -rf /opt/dist/*
COPY artifacts/web-static/ /opt/dist/
