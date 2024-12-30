FROM docker.io/library/node:21

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json first (to leverage Docker's caching mechanism for dependencies)
COPY package*.json ./

# Install application dependencies
RUN npm install

# Copy the rest of the application code to the working directory
COPY . .

# Build the Angular application
RUN npm run prod

# Install a lightweight HTTP server to serve the Angular app
RUN npm install -g http-server

# Expose the port that the application will run on
EXPOSE 8080

# Switch to a non-root user for better security
USER node

# Define the command to serve the Angular application
CMD ["http-server", "dist"]
