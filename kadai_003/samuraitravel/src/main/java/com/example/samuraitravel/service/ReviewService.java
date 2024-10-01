package com.example.samuraitravel.service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.ReviewRepository;


@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	
	@Transactional
	public Review create(ReviewPostForm reviewPostForm, House house, User user) {
	    Review review = new Review();
	    
	    review.setRankStar(reviewPostForm.getRankStar());
	    review.setReview(reviewPostForm.getReview());
	    review.setHouse(house);  
	    review.setUser(user); 
	    
	    return reviewRepository.save(review);
	}


	@Transactional
    public void update(ReviewEditForm reviewEditForm) {
        Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
        
        review.setRankStar(reviewEditForm.getRankStar());
        review.setReview(reviewEditForm.getReview());
        review.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
  
        reviewRepository.save(review);
    }
	
	@Transactional
    public void delete(Integer id) {
        reviewRepository.deleteById(id);
    }

	

}
