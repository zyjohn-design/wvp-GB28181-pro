FROM node:20-alpine

WORKDIR /opt/adapter

COPY package.json package-lock.json ./
RUN npm ci --omit=dev

COPY lib.js server.js ./
COPY public ./public
COPY config ./config

ENV NODE_ENV=production
ENV VENDOR_MPEGTS_FILE=/opt/adapter/node_modules/mpegts.js/dist/mpegts.js
EXPOSE 8088

USER node
CMD ["node", "server.js"]
