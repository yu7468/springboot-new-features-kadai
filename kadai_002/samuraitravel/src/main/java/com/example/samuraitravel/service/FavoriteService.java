package com.example.samuraitravel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

@Service
public class FavoriteService {
	
    private FavoriteRepository favoriteRepository;
	
    @Autowired
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	@Transactional
    public void addFavorite(User user, House house) {
        if (!favoriteRepository.existsByUserAndHouse(user, house)) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setHouse(house);
            favoriteRepository.save(favorite);
        }
    }
    
	@Transactional
    public void removeFavorite(User user, House house) {
    	favoriteRepository.deleteByUserAndHouse(user, house);
    }
	
	public Page<Favorite> findFavoritesByUser(User user, Pageable pageable) {
        return favoriteRepository.findByUser(user, pageable);
    }
	
	public boolean isFavorite(User user, House house) {
        return favoriteRepository.existsByUserAndHouse(user, house);
    }
}
