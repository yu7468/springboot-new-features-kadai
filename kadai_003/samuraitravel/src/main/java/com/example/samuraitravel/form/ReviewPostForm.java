package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor

public class ReviewPostForm {
	
	@NotNull(message = "")
    private Integer rankStar;
 
    @NotBlank(message = "レビュー内容を入れてください。")
    private String review;

}
