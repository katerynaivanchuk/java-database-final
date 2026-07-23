package com.project.code.controller;

import com.project.code.model.Customer;
import com.project.code.model.Review;
import com.project.code.repo.CustomerRepository;
import com.project.code.repo.ReviewRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;

    public ReviewController(
            ReviewRepository reviewRepository,
            CustomerRepository customerRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable("storeId") Long storeId,
            @PathVariable("productId") Long productId
    ) {
        List<Review> reviews =
                reviewRepository.findByStoreIdAndProductId(
                        storeId,
                        productId
                );

        List<Map<String, Object>> result = new ArrayList<>();

        for (Review review : reviews) {
            Map<String, Object> reviewData = new HashMap<>();

            Optional<Customer> customer =
                    customerRepository.findById(
                            review.getCustomerId()
                    );

            String customerName = customer
                    .map(Customer::getName)
                    .orElse("Unknown");

            reviewData.put("comment", review.getComment());
            reviewData.put("rating", review.getRating());
            reviewData.put("customerName", customerName);

            result.add(reviewData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", result);

        return response;
    }

    @GetMapping
    public Map<String, Object> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviews);

        return response;
    }
}
