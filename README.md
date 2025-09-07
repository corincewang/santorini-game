# Santorini Game

A full-stack implementation of the classic Santorini board game, built with Spring Boot and React, deployed on AWS.

## 🌐 Play Online (Recommended)

**Live Demo**: [http://santorini-game-env.eba-cyjh8abk.us-east-2.elasticbeanstalk.com/](http://santorini-game-env.eba-cyjh8abk.us-east-2.elasticbeanstalk.com/)

The game is fully deployed on AWS Elastic Beanstalk and ready to play instantly - no setup required!

## 🚀 Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Frontend**: React 18, TypeScript
- **Build Tool**: Maven
- **Deployment**: AWS Elastic Beanstalk
- **Architecture**: RESTful API with integrated frontend

## 📋 Game Rules

The game is played on a 5×5 grid where each cell can contain towers consisting of blocks and domes. Two players have two workers each placed on the grid. Throughout the game, workers move around and build towers. **The first worker to reach the top of a level-3 tower wins!**

### Gameplay:
1. **Setup**: Players take turns placing their 2 workers on different cells
2. **Each Turn**: 
   - Select one of your workers
   - Move to an adjacent unoccupied cell (can climb up to 1 level)
   - Build a block or dome on an adjacent unoccupied cell
3. **Building Rules**: 
   - Domes can only be built on level-3 towers
   - Cells with workers or domes are considered occupied
4. **Winning**: First player to move a worker onto a level-3 tower wins

## 🛠️ Local Development (Outdated - Use Online Version Instead)

<details>
<summary>Click to expand local setup instructions</summary>

### Prerequisites
- Java 17
- Node.js 14+
- Maven 3.6+

### Backend Setup
```bash
cd my-app
mvn install
mvn spring-boot:run
```
This starts the Spring Boot server at http://localhost:8080

### Frontend Setup
```bash
cd my-app/frontend
npm install
npm start
```
This starts the React development server at http://localhost:3000

### Full-Stack Local Build
```bash
cd my-app
# Build React frontend
cd frontend && npm run build && cd ..
# Copy frontend to Spring Boot static resources
cp -r frontend/build/* src/main/resources/static/
# Build and run full-stack application
mvn clean package -DskipTests
java -jar target/my-app-1.0-SNAPSHOT.jar
```
Access the full application at http://localhost:8080

</details>

## 🏗️ Architecture

- **Spring Boot Backend**: RESTful APIs at `/api/newgame` and `/api/play`
- **React Frontend**: Served as static resources from Spring Boot
- **Health Endpoints**: `/health` for AWS monitoring
- **CORS Enabled**: Supports cross-origin requests for development and production

## 📦 Deployment

The application is containerized as a single executable JAR file and deployed to AWS Elastic Beanstalk with:
- Automatic scaling and load balancing
- Health monitoring and auto-recovery  
- Production-ready configuration 