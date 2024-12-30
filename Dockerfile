FROM node:21

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy contents to the working directory
COPY . .

# Install application dependencies
RUN npm install

# Create a non-root user to run the application
USER node

# Define the command to run your application
CMD ["npm", "run", "start"]
