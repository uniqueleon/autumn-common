package org.aztec.autumn.common.utils.persistence;

import java.util.List;
import java.util.Map;

public interface IBaseDAO<T> {

	public void beginTransaction();
	public void commit() throws PersistenceException;
	public void rollback() throws PersistenceException;
	public void detach(T entity) throws PersistenceException;
	public List<T> findAll() throws PersistenceException;
	public List<T> find(Map<String,Object> params) throws PersistenceException;
	public List<T> find(T sample) throws PersistenceException;
	public T findById(Object id) throws PersistenceException;
	public List<T> findByIds(List ids) throws PersistenceException;
	public T save(T entity) throws PersistenceException;
	public List<T> batchSave(List<T> entities) throws PersistenceException;
	public T delete(T entity,boolean logical,String propName) throws PersistenceException;
	public Paginator<T> findAllByPage(int page,int pageSize) throws PersistenceException;
	public Paginator<T> findByPage(Map<String,Object> params,int page,int pageSize) throws PersistenceException;
	public Paginator<T> findByPage(T sample) throws PersistenceException;
	public void reconnect();
	public void refresh();
	public void refresh(T entity);
	public int executeUpdate(String sql,Object[] params) throws PersistenceException;
	public <E> Paginator<E> executeNativeQuery(String sql,Object[] params,Class<E> entityCls,int pageNo,int pageSize);
	//public void synchronize();
}
