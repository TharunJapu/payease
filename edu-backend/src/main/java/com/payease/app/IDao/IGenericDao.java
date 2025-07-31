package com.payease.app.IDao;

import java.util.List;

public interface IGenericDao<T> {

	T create(T data);
	
	T fineOne(String data);

	Class<T> getEntityClass();
	
	List<T> getAll();
	
	T update(T data);
	
}
