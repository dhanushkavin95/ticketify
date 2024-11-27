package com.moviebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.List;

@SpringBootApplication
public class MovieBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieBookingApplication.class, args);
    }

    // Controller Class
    @RestController
    @RequestMapping("/api")
    public class MovieController {

        @Autowired
        private MovieService movieService;

        // Endpoint to get all movies
        @GetMapping("/movies")
        public List<Movie> getMovies() {
            return movieService.getAllMovies();
        }

        // Endpoint to add a new movie
        @PostMapping("/movies")
        public Movie addMovie(@RequestBody Movie movie) {
            return movieService.addMovie(movie);
        }

        // Endpoint to book a movie
        @PostMapping("/book")
        public String bookMovie(@RequestBody Booking booking) {
            return movieService.bookMovie(booking);
        }
    }

    // Service Class
    @Service
    public class MovieService {

        @Autowired
        private MovieRepository movieRepository;

        @Autowired
        private BookingRepository bookingRepository;

        // Get all movies
        public List<Movie> getAllMovies() {
            return movieRepository.findAll();
        }

        // Add a movie
        public Movie addMovie(Movie movie) {
            return movieRepository.save(movie);
        }

        // Book a movie
        public String bookMovie(Booking booking) {
            Booking savedBooking = bookingRepository.save(booking);
            return "Booking successful for movie ID: " + savedBooking.getMovieId();
        }
    }

    // Entity Class for Movie
    @Entity
    @Table(name = "movies")
    public class Movie {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
        private String director;
        private int year;

        public Movie() {}

        public Movie(String title, String director, int year) {
            this.title = title;
            this.director = director;
            this.year = year;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

    // Entity Class for Booking
    @Entity
    @Table(name = "bookings")
    public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long movieId;
        private String customerName;

        public Booking() {}

        public Booking(Long movieId, String customerName) {
            this.movieId = movieId;
            this.customerName = customerName;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getMovieId() {
            return movieId;
        }

        public void setMovieId(Long movieId) {
            this.movieId = movieId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
    }

    // Repository Interface for Movie
    public interface MovieRepository extends JpaRepository<Movie, Long> {}

    // Repository Interface for Booking
    public interface BookingRepository extends JpaRepository<Booking, Long> {}

    // Database Configuration
    @Bean
    public javax.sql.DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/moviebooking");
        dataSource.setUsername("root");
        dataSource.setPassword("your_password");
        return dataSource;
    }
}
