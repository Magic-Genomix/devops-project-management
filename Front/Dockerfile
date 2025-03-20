# STEP 1
FROM node:20.9.0 AS builder

RUN mkdir -p /app

WORKDIR /app

COPY package.json /app/

RUN npm install

COPY . /app/

RUN npm run build --prod

# STEP 2
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

RUN rm /usr/share/nginx/html/50x.html

COPY --from=builder /app/dist/app-gestion/browser/ /usr/share/nginx/html

