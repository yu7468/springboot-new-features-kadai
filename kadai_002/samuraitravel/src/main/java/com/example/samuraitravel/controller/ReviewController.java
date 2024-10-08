package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;


@Controller
public class ReviewController {
	private final HouseRepository houseRepository; 
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;

    @Autowired
    public ReviewController(HouseRepository houseRepository, UserRepository userRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
	    this.houseRepository = houseRepository;
	    this.userRepository = userRepository;
	    this.reviewRepository = reviewRepository;
	    this.reviewService = reviewService;
	}  
    
    @GetMapping("/houses/{id}/reviews/post")
    public String post(@PathVariable("id") Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, RedirectAttributes redirectAttributes) {
        
        House house = houseRepository.getReferenceById(houseId);
        User user = userDetailsImpl.getUser(); 
        
        boolean hasReviewed = reviewService.hasUserReviewedHouse(houseId, user.getId());
        if (hasReviewed) {
            redirectAttributes.addFlashAttribute("errorMessage", "すでにこの宿にレビューを投稿しています。");
            return "redirect:/houses/" + houseId;  
        }
        
        ReviewPostForm reviewPostForm = new ReviewPostForm(house.getId(), user.getId(), null, "");
        model.addAttribute("reviewPostForm", reviewPostForm);
        model.addAttribute("house", house);
        model.addAttribute("user", user);
        return "reviews/post";
    } 

    @PostMapping("/reviews/create")
    public String create(@ModelAttribute @Validated ReviewPostForm reviewPostForm, BindingResult bindingResult, @RequestParam Integer houseId, @RequestParam Integer userId, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reviews/post"; 
        }
        House house = houseRepository.getReferenceById(houseId);
        User user = userRepository.getReferenceById(userId);
        
        boolean hasReviewed = reviewService.hasUserReviewedHouse(houseId, userId);
        if (hasReviewed) {
            redirectAttributes.addFlashAttribute("errorMessage", "すでにこの宿にレビューを投稿しています。");
            return "redirect:/houses/" + houseId;
        }

        reviewService.create(reviewPostForm, house, user);
        redirectAttributes.addFlashAttribute("successMessage", "レビューが投稿しました。");

        return "redirect:/houses/" + houseId;
    }

    @GetMapping("/reviews/list/{houseId}")
    public String list(@PathVariable(name = "houseId") int houseId, Model model, @PageableDefault(page = 0, size = 10, sort = "houseId", direction = Direction.ASC)Pageable pageable) {

        House house = houseRepository.getReferenceById(houseId);
        Page<Review> reviewPage = reviewRepository.findByHouse(house,pageable);

        model.addAttribute("house", house);
        model.addAttribute("reviewPage", reviewPage);

        return "reviews/list";
    } 
    

    
    @GetMapping("/reviews/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {        
        Review review = reviewRepository.getReferenceById(id);
        House house = houseRepository.getReferenceById(review.getHouse().getId());
        ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getRankStar(), review.getReview());
        
        model.addAttribute("house", house);
        model.addAttribute("reviewEditForm", reviewEditForm);        
        return "reviews/edit";
    }    

    @PostMapping("/reviews/update")
        public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	if (bindingResult.hasErrors()) {
            return "reviews/edit";
        }
    	reviewService.update(reviewEditForm);
            redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
            
            return "redirect:/";
        }
    
    @PostMapping("/reviews/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
    	reviewRepository.deleteById(id);    
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        
        return "redirect:/";
    }
    
}
