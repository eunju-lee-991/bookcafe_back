package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Review;
import cafe_in.cafe_in.repository.review.ReviewRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewRepositoryImpl repository;

    @GetMapping("/reviews/test")
    public void test(){
        List<Review> reviews = repository.findReviews();
        reviews.stream().forEach(r -> System.out.println(r.getIsbn()));
        reviews.stream().forEach(r -> System.out.println(r.getReviewId()));
    }
}
