package com.project.code.controller;

import com.project.code.exceptions.CustomerNotFoundException;
import com.project.code.model.Customer;
import com.project.code.model.Review;
import com.project.code.repo.CustomerRepository;
import com.project.code.repo.ReviewRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/reviews` URL using `@RequestMapping("/reviews")`.


 // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `ReviewRepository` for accessing review data.
//        - `CustomerRepository` for retrieving customer details associated with reviews.

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;

    public ReviewController(ReviewRepository reviewRepository, CustomerRepository customerRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
    }


// 3. Define the `getReviews` Method:
//    - Annotate with `@GetMapping("/{storeId}/{productId}")` to fetch reviews for a specific product in a store by `storeId` and `productId`.
//    - Accept `storeId` and `productId` via `@PathVariable`.
//    - Fetch reviews using `findByStoreIdAndProductId()` method from `ReviewRepository`.
//    - Filter reviews to include only `comment`, `rating`, and the `customerName` associated with the review.
//    - Use `findById(review.getCustomerId())` from `CustomerRepository` to get customer name.
//    - Return filtered reviews in a `Map<String, Object>` with key `reviews`.

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable("storeId") Long storeId, @PathVariable("productId") Long productId) {
        Map<String, Object> response = new HashMap<>();
        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);
        List<Map<String, Object>> returnReviews = new ArrayList<>();
        for( Review review : reviews ) {
            Map<String,Object> oneReview = new HashMap<>();
            Optional<Customer> customer = customerRepository.findById(review.getCustomerId());

            String customerName;

            if(customer.isPresent()) {
                customerName = customer.get().getName();
            } else {
                customerName = "Unknown";
            }

            oneReview.put("comment",  review.getComment());
            oneReview.put("rating",  review.getRating());
            oneReview.put("customerName", customerName);

            returnReviews.add(oneReview);
        }
        response.put("reviews", returnReviews);
        return response;
    }
   
}
