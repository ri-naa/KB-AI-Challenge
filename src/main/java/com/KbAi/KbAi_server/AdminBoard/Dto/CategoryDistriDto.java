package com.KbAi.KbAi_server.AdminBoard.Dto;

import com.KbAi.KbAi_server.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryDistriDto {
    private Category category;
    private String description;
    private double ratio;
}
