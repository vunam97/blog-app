package com.vti.blogapp.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PostCreateForm {
    @NotBlank(message = "{post.title.NotBlank.message}")
    @Length(max = 50, message = "{post.title.Length.message}")
    private String title;

    @NotBlank
    @Length(max = 100)
    private String description;

    @NotBlank
    @Length(max = 150)
    private String content;
}
