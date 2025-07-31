package com.payease.app.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.payease.app.IDao.IGenericDao;

public abstract class GenericDao<T> implements IGenericDao<T> {

	@Autowired
	MongoTemplate mongoTemplate;

	public T create(T data) {
		return mongoTemplate.insert(data);

	}

	public T fineOne(String id) {
		return (T) mongoTemplate.findById(id, getEntityClass());

	}

	public List<T> getAll() {
		return mongoTemplate.findAll(getEntityClass());
	}

	public T update(T data) {
		return mongoTemplate.save(data);

	}
}
