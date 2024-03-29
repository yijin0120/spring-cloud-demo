package com.cloud.mq.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Dao封装实现类
 * @author ZhangTao
 * @Time 2016-10-28
 */
@Repository("daoSupport")
public class DaoSupport implements BaseDao {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 新增对象
     */
    public Object save(String str, Object obj) throws Exception {
        return sqlSessionTemplate.insert(str, obj);
    }

    /**
     * 批量新增对象
     */
    public Object batchSave(String str, @SuppressWarnings("rawtypes") List objs) throws Exception {
        return sqlSessionTemplate.insert(str, objs);
    }

    /**
     * 修改对象
     */
    public Object update(String str, Object obj) throws Exception {
        return sqlSessionTemplate.update(str, obj);
    }

    /**
     * 批量更新对象
     */
    public void batchUpdate(String str, @SuppressWarnings("rawtypes") List objs) throws Exception {
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            if (objs != null) {
                for (int i = 0, size = objs.size(); i < size; i++) {
                    sqlSession.update(str, objs.get(i));
                }
                sqlSession.flushStatements();
                sqlSession.commit();
                sqlSession.clearCache();
            }
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 删除对象
     */
    public Object delete(String str, Object obj) throws Exception {
        return sqlSessionTemplate.delete(str, obj);
    }

    /**
     * 批量删除对象
     */
    public Object batchDelete(String str, @SuppressWarnings("rawtypes") List objs) throws Exception {
        return sqlSessionTemplate.delete(str, objs);
    }
    
    /**
     * 带参数查找对象
     */
    public Object findForObject(String str, Object obj) throws Exception {
        return sqlSessionTemplate.selectOne(str, obj);
    }
    
    /**
     * 不带参数查找对象
     */
    public Object findForObject(String str) throws Exception {
        return sqlSessionTemplate.selectOne(str);
    }
    
    /**
     * 查找对象列表
     * 
     * @param str
     */
    public Object findForList(String str, Object obj) throws Exception {
        return sqlSessionTemplate.selectList(str, obj);
    }
    
    /**
     * 查找无参对象列表
     * 
     * @param str
     */
    public Object findForList(String str) throws Exception {
        return sqlSessionTemplate.selectList(str);
    }

    /**
     * 查找对象Map
     */
    public Object findForMap(String str, Object obj, String key, String value) throws Exception {
        return sqlSessionTemplate.selectMap(str, obj, key);
    }
    
    /**
     * 执行存储过程
     * @param str
     * @param map
     */
     public void procedure(String str, Map<String, Object> map) {
         sqlSessionTemplate.select(str, map, null);
     }
}
