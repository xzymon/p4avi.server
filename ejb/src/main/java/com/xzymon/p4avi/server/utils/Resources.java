package com.xzymon.p4avi.server.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

public class Resources {
	private static final String CONST_JBOSS_SUBSYSTEM = "subsystem";
	private static final String CONST_JBOSS_SUBSYSTEM_DS = "data-source";
	private static final String CONST_JNDI_NAME = "jndi-name";
	private static final String CONST_CONNECTION_URL = "connection-url";
	private static final String CONST_DRIVER_NAME = "driver-name";
	private static final String CONST_USER_NAME = "user-name";
	private static final String CONST_PASSWORD = "password";
	private static final String CONST_POOL_NAME = "pool-name";
	
	private static final String SUBSYSTEM_DATASOURCES = "datasources";
	private static final String AVIPROD_DS_JNDI = "java:jboss/datasources/aviprodDS";
	private static final String AVIPROD_JDBC_CONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/aviprod";
	
	private static final String LOCALHOST = "127.0.0.1";
	private static final int DEFAULT_ADMIN_PORT = 9999;
	
	private static final String EMF_PROD_NAME = "aviprod";
	
	private static EntityManagerFactory emfPROD;
	
	public static void createDataSource() throws Exception {
		ModelNode modelNode = new ModelNode();
		modelNode.get(ClientConstants.OP).set(ClientConstants.ADD);
		modelNode.get(ClientConstants.OP_ADDR).add(CONST_JBOSS_SUBSYSTEM, SUBSYSTEM_DATASOURCES);
		modelNode.get(ClientConstants.OP_ADDR).add(CONST_JBOSS_SUBSYSTEM_DS, AVIPROD_DS_JNDI);
		modelNode.get(CONST_JNDI_NAME).set(AVIPROD_DS_JNDI);
		modelNode.get(CONST_CONNECTION_URL).set(AVIPROD_JDBC_CONNECTION_URL);
		// modelNode.get("driver-class").set();
		modelNode.get(CONST_DRIVER_NAME).set("mysql");
		modelNode.get(CONST_USER_NAME).set("prod1");
		modelNode.get(CONST_PASSWORD).set("aviprod");
		modelNode.get(CONST_POOL_NAME).set("aviprod_on_mysql");
		ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName(LOCALHOST), DEFAULT_ADMIN_PORT);
		client.execute(new OperationBuilder(modelNode).build());

	}

	private static Map<String, Object> getEMFProdPropertiesMap(){
		Map<String, Object> emfPropertiesMap = new HashMap<String, Object>();
		emfPropertiesMap.put("hibernate.bytecode.use_reflection_optimizer", "false");
		emfPropertiesMap.put("hibernate.cache.infinispan.cachemanager", "java:jboss/infinispan/container/aviCache");
		emfPropertiesMap.put("hibernate.cache.region.factory_class", "org.hibernate.cache.infinispan.JndiInfinispanRegionFactory");
		emfPropertiesMap.put("hibernate.cache.use_query_cache", "true");
		emfPropertiesMap.put("hibernate.cache.use_second_level_cache", "true");
		emfPropertiesMap.put("hibernate.connection.autocommit", "true");
		emfPropertiesMap.put("hibernate.connection.datasource", "java:jboss/datasources/aviprodDS");
		emfPropertiesMap.put("hibernate.connection.release_mode", "auto");
		emfPropertiesMap.put("hibernate.ejb.discard_pc_on_close", "false");
		emfPropertiesMap.put("hibernate.hbm2ddl.auto", "create-drop");
		emfPropertiesMap.put("hibernate.query.jpaql_strict_compliance", "true");
		emfPropertiesMap.put("hibernate.show_sql", "true");
		emfPropertiesMap.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jdbc.JdbcTransactionFactory");
		emfPropertiesMap.put("hibernate.transaction.flush_before_completion", "false");
		emfPropertiesMap.put("hibernate.use_identifier_rollback", "false");
		return emfPropertiesMap;
	}
	
	public static EntityManagerFactory getEntityManagerFactoryProdInstance() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		//Map<String, Object> configOverrides = getEMFProdPropertiesMap();
		if(emfPROD!=null){
			return emfPROD;
		}
		emfPROD = Persistence.createEntityManagerFactory(EMF_PROD_NAME, configOverrides);
		return emfPROD;
	}

	public boolean checkIfDatasourceExists() throws Exception {
		ModelNode request = new ModelNode();
		request.get(ClientConstants.OP).set("read-resource");
		request.get("recursive").set(false);
		request.get(ClientConstants.OP_ADDR).add("subsystem", "datasources");
		ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("127.0.0.1"), 9999);
		ModelNode responce = client.execute(new OperationBuilder(request).build());
		ModelNode datasources = responce.get(ClientConstants.RESULT).get("data-source");
		if (datasources.isDefined()) {
			for (ModelNode dataSource : datasources.asList()) {
				String dataSourceName = dataSource.asProperty().getName();
				if (dataSourceName.equals("java:jboss/datasources/NewDatasource")) {
					return true;
				}
			}
		}
		return false;
	}

	
}
