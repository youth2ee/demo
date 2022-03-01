package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {

	@Autowired
	private TodoRepository repository;
	
	// 2-21, 2-28
	public String testService() {
		// 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		// 저장
		repository.save(entity);
		// 검색
		TodoEntity saveEntity = repository.findById(entity.getId()).get();
		return saveEntity.getTitle();
	}
	
	public List<TodoEntity> create(final TodoEntity entity) {
		
		validate(entity);
		repository.save(entity);
		log.info("Entity Id : {} is saved.", entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	// 2-32
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
	
	// 2-36
	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserId(userId);
	}
	
	// 2-38
	public List<TodoEntity> update(final TodoEntity entity) {
		validate(entity);
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		original.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			repository.save(todo);
		});
		
		return retrieve(entity.getUserId());
	}
	
	// 2-40
	public List<TodoEntity> delete(final TodoEntity entity) {
		validate(entity);
		
		try {
			repository.delete(entity);
			
		} catch (Exception e) {
			log.error("error deleting entity", entity.getId(), e);
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		return retrieve(entity.getUserId());
	}
	
}
