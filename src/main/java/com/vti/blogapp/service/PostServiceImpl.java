package com.vti.blogapp.service;

import com.vti.blogapp.dto.PostDto;
import com.vti.blogapp.form.PostCreateForm;
import com.vti.blogapp.form.PostUpdateForm;
import com.vti.blogapp.mapper.PostMapper;
import com.vti.blogapp.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;

    @Override
    public Page<PostDto> findAll(Pageable pageable) {
        // lambda
        // method reference
        return postRepository.findAll(pageable).map(PostMapper::map);
    }

    @Override
    public PostDto create(PostCreateForm form) {
        var post = PostMapper.map(form);
        var savePost = postRepository.save(post);
        return PostMapper.map(savePost);
    }

    @Override
    public PostDto update(PostUpdateForm form, Long id) {
        var post = PostMapper.map(form);
        post.setId(id);
        var savePost = postRepository.save(post);
        return PostMapper.map(savePost);
    }
}
