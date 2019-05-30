package com.example.springboot_401;

import org.springframework.data.repository.CrudRepository;

public interface TweetRepo extends CrudRepository<Tweet,Long>{
}
