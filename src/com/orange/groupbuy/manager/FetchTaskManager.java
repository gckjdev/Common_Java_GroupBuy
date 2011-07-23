package com.orange.groupbuy.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;

public class FetchTaskManager extends CommonManager{

	
	// task
	// 
	
	public static DBObject obtainOneTask(MongoDBClient mongoClient){
		
		
		DBObject task = mongoClient.findAndModify(DBConstants.T_FETCH_TASK, 
												DBConstants.F_TASK_STATUS,
												DBConstants.C_TASK_STATUS_NOT_RUNNING,
												DBConstants.C_TASK_STATUS_RUNNING);
						
		return task;		
	}

	public static void taskDownloadFileSuccess(MongoDBClient mongoClient, DBObject task,
			String localFilePath) {
		
		task.put(DBConstants.F_TASK_STATUS, Integer.valueOf(DBConstants.C_TASK_STATUS_DOWNLOAD_OK));
		task.put(DBConstants.F_TASK_FILE_PATH, localFilePath);
		mongoClient.save(DBConstants.T_FETCH_TASK, task);		
	}

	public static void taskClose(MongoDBClient mongoClient, DBObject task) {
		task.put(DBConstants.F_TASK_STATUS, Integer.valueOf(DBConstants.C_TASK_STATUS_CLOSE));
		mongoClient.save(DBConstants.T_FETCH_TASK, task);		
	}
	
	
}
