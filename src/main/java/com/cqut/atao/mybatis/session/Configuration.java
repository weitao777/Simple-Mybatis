package com.cqut.atao.mybatis.session;

import com.cqut.atao.mybatis.binding.MapperRegistry;
import com.cqut.atao.mybatis.datasource.druid.DruidDataSourceFactory;
import com.cqut.atao.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.cqut.atao.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.cqut.atao.mybatis.executor.Executor;
import com.cqut.atao.mybatis.executor.SimpleExecutor;
import com.cqut.atao.mybatis.executor.resultset.DefaultResultSetHandler;
import com.cqut.atao.mybatis.executor.resultset.ResultSetHandler;
import com.cqut.atao.mybatis.executor.statement.PreparedStatementHandler;
import com.cqut.atao.mybatis.executor.statement.StatementHandler;
import com.cqut.atao.mybatis.mapping.BoundSql;
import com.cqut.atao.mybatis.mapping.Environment;
import com.cqut.atao.mybatis.mapping.MappedStatement;
import com.cqut.atao.mybatis.transaction.Transaction;
import com.cqut.atao.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.cqut.atao.mybatis.type.TypeAliasRegistry;
import sun.plugin2.main.server.ResultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author atao
 * @version 1.0.0
 * @ClassName Configuration.java
 * @Description 配置项
 * @createTime 2022年09月24日 23:26:00
 */
public class Configuration {

    /**
     * 环境
     */
    protected Environment environment;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别名注册机
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }


}
