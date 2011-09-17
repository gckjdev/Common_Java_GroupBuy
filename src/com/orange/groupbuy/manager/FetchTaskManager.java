package com.orange.groupbuy.manager;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;

public class FetchTaskManager extends CommonManager{

	
	private static final int MAX_TASK_RETRY = 100;	// retry 100 times

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
		task.put(DBConstants.F_TASK_FILE_PATH, null);		// clear file path to avoid future usage
		mongoClient.save(DBConstants.T_FETCH_TASK, task);		
	}

	public static void taskFailure(MongoDBClient mongoClient, DBObject task) {
		task.put(DBConstants.F_TASK_STATUS, Integer.valueOf(DBConstants.C_TASK_STATUS_FAILURE));
		task.put(DBConstants.F_TASK_FILE_PATH, null);		// clear file path to avoid future usage
		mongoClient.save(DBConstants.T_FETCH_TASK, task);		
	}

	public static void resetAllTask(MongoDBClient mongoClient) {
		
		BasicDBObject query = new BasicDBObject();
		BasicDBObject update = new BasicDBObject();
		
		BasicDBObject value = new BasicDBObject();
		value.put("$ne", DBConstants.C_TASK_STATUS_NOT_RUNNING);
		query.put(DBConstants.F_TASK_STATUS, value);

		BasicDBObject updateValue = new BasicDBObject();
		updateValue.put(DBConstants.F_TASK_STATUS, DBConstants.C_TASK_STATUS_NOT_RUNNING);
		updateValue.put(DBConstants.F_TASK_FILE_PATH, null);
		updateValue.put(DBConstants.F_TASK_RETRY_TIMES, 0);
		update.put("$set", updateValue);
		
		mongoClient.updateAll(DBConstants.T_FETCH_TASK, query, update);
	}

	public static void taskRetry(MongoDBClient mongoClient, DBObject task) {
		Integer retryTimes = (Integer)task.get(DBConstants.F_TASK_RETRY_TIMES);
		int newRetryTimes = 0;
		if (retryTimes != null)
			newRetryTimes = retryTimes.intValue() + 1;
		
		if (newRetryTimes <= MAX_TASK_RETRY ){
			task.put(DBConstants.F_TASK_STATUS, Integer.valueOf(DBConstants.C_TASK_STATUS_NOT_RUNNING));
			task.put(DBConstants.F_TASK_RETRY_TIMES, newRetryTimes);
		}
		else{
			task.put(DBConstants.F_TASK_STATUS, Integer.valueOf(DBConstants.C_TASK_STATUS_FAIL_MAX_RETRY));			
		}

		mongoClient.save(DBConstants.T_FETCH_TASK, task);		
	}

    public static void activateAllFinishTask(MongoDBClient mongoClient) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();
        
        // construct query
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject(DBConstants.F_TASK_STATUS, DBConstants.C_TASK_STATUS_CLOSE));
        values.add(new BasicDBObject(DBConstants.F_TASK_STATUS, DBConstants.C_TASK_STATUS_FAIL_MAX_RETRY));
        values.add(new BasicDBObject(DBConstants.F_TASK_STATUS, DBConstants.C_TASK_STATUS_FAILURE));
        query.put("$or", values);

        // update to not running status
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_TASK_STATUS, DBConstants.C_TASK_STATUS_NOT_RUNNING);
        updateValue.put(DBConstants.F_TASK_FILE_PATH, null);
        updateValue.put(DBConstants.F_TASK_RETRY_TIMES, 0);
        update.put("$set", updateValue);
        
        mongoClient.updateAll(DBConstants.T_FETCH_TASK, query, update);        
        
        log.info("<activateAllFinishTask> query = " + query.toString() + 
                ", value = " + update.toString());
    }

}
