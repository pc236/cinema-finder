# CinemaFinder: Movie Catalog and Favorites Management!

Hello! This is a full-stack web application I've made for exploring/enjoying movies to watch and catalog (inspired my love for LetterBoxd). In this app, you'll be able to discover new movies, view trending titles and manage your personal favorites. This has been built with a Java SpringBoot backend and Angular frontend, deployed on AWS with CI/CD automation.

## Features
- Search movies by title using TMDB's database
- View trending and popular movies
- Save and manage favorite movies
- Responsive design for desktop and mobile

## Tech Stack

**Backend:**
- Java 21 with Spring Boot 3.5.6
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5 and Mockito (74% code coverage, 29 tests)

**Frontend:**
- Angular 18+
- TypeScript
- RxJS
- CSS Grid and Flexbox

**External APIs:**
- TMDB API for movie data

**DevOps and Cloud:**
- GitHub Actions for CI/CD
- AWS Elastic Beanstalk (backend)
- AWS S3 (frontend static hosting)
- AWS CloudFront (optional CDN)

## API Endpoints

| Method | Endpoint                           | Description 
| GET    | `/api/movies/search?query={query}` | Search for movies by title 
| GET    | `/api/movies/trending`             | Get trending movies this week 
| GET    | `/api/movies/popular`              | Get popular movies 
| GET    | `/api/movies/favorites`            | Get user's favorite movies 
| POST   | `/api/movies/favorites/{tmdbId}`   | Add movie to favorites 
| DELETE | `/api/movies/favorites/{tmdbId}`   | Remove movie from favorites 

## Architecture
I've used a conventional, three-layer architecture for CinemaFinder:
- **Controller Layer** - REST API endpoints
- **Service Layer** - Business logic and TMDB API integration
- **Repository Layer** - Database operations via Spring Data JPA
- **Entity Layer** - JPA entities
- **DTO Layer** - Data transfer objects

The frontend uses component-based architecture with services for HTTP communication and RxJS for reactive data handling.

## Testing

The project includes 29 unit tests covering controllers, services, and repositories with 74% code coverage measured by JaCoCo. Tests run automatically on every push via GitHub Actions.

Run tests:
```bash
./mvnw test
```

Generate coverage report:
```bash
./mvnw clean test
```
View the report at `target/site/jacoco/index.html`

## Local Development Setup

**Prerequisites:**
- Java 21 or higher
- Node.js 18+ and npm
- Maven 3.6+
- TMDB API Key (free at themoviedb.org)

**Backend Setup:**

Clone the repository:
```bash
git clone https://github.com/YOUR_USERNAME_HERE/cinema-finder.git
cd cinema-finder
```

Set your TMDB API key as an environment variable:
```bash
export TMDB_API_KEY=insert_your_api_key_here
```

Run the backend:
```bash
./mvnw spring-boot:run
```

* The backend runs at `http://localhost:8080`

**Frontend Setup:**

Navigate to the frontend directory:
```bash
cd cinema-finder-frontend
```

Install dependencies:
```bash
npm install
```

Update the API URL in `src/app/services/movie.service.ts` to point to `http://localhost:8080/api/movies`

Run the frontend:
```bash
ng serve
```

The frontend runs at `http://localhost:4200`

## Deployment

**Backend to AWS Elastic Beanstalk:**

Package the application:
```bash
./mvnw clean package -DskipTests
```

Deploy the JAR file (`target/cinema-finder-0.0.1-SNAPSHOT.jar`) via the AWS Elastic Beanstalk console. Configure environment variables: `TMDB_API_KEY`, `TMDB_API_BASE_URL`, and `SERVER_PORT=5000`.

**Frontend to AWS S3:**

Build the production bundle:
```bash
ng build --configuration production
```

Deploy to S3:
```bash
aws s3 sync dist/cinema-finder-frontend/browser/ s3://your-bucket-name
aws s3 website s3://your-bucket-name --index-document index.html
```

## Environment Variables

| Variable          | Description                                      | Required 
| TMDB_API_KEY      | Your TMDB API key                                | Yes 
| TMDB_API_BASE_URL | TMDB API base URL (https://api.themoviedb.org/3) | Yes 
| SERVER_PORT       | Backend server port                              | No (default: 8080)

## Author

Paul Choe  
LinkedIn: linkedin.com/in/pchoe236  
Email: choe.86@osu.edu

## Acknowledgments

Movie data provided by The Movie Database (TMDB).

---

Other Notes when using CinemaFinder

Step 1: start the Spring application by entering .\mvnw spring-boot:run
    - The terminal should have the last line that says "Started CinemaFinderApplication in [insert number] seconds"

Step 2: open up locally on your machine with "http://localhost:8080/h2-console"
    - Ensure that there are no errors in the start up logs and that the JPA repo interfaces are being found and that
    the HTTP methods (GET, POST, DELETE, etc.) are being mapped.