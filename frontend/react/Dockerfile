FROM node:20-alpine
ARG api_base_url
WORKDIR /app
COPY frontend/react/package*.json ./
RUN ls -la
RUN npm cache clean --force
RUN npm install --verbose
COPY frontend/react/ .
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
EXPOSE 5173
CMD ["npm", "run", "dev"]


#FROM node:20.9.0-alpine
#ARG api_base_url
#WORKDIR /app
#COPY package*.json .
#RUN npm cache clean --force
#RUN npm install --verbose
#COPY . .
#RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
#EXPOSE 5173
#CMD ["npm", "run", "dev"]
