package com.microsoft.azure.datalake.store;

import com.microsoft.azure.datalake.store.JobExecutor.UploadStatus;

class Job implements Comparable<Job>{
	MetaData data;
	long offset, id, size;
	enum JobType {
		MKDIR, FILEUPLOAD, CONCATENATE, VERIFY, FILEDOWNLOAD
	}
	JobType type;
	Job(MetaData data, long offset, long size, long id, JobType type) {
		this.data = data;
		this.offset = offset;
		this.size = size;
		this.id = id;
		this.type = type;
	}

	public int compareTo(Job that) {
		if(this.size == that.size) {
			return Long.compare(this.id, that.id);
		}
		return Long.compare(that.size, this.size);
	}
	
	public boolean isFinalUpload() {
		return data.isFinalUpload();
	}
	
	public UploadStatus fileUploadStatus() {
		return data.getUploadStatus();
	}
	
	public boolean existsAtDestination(ADLStoreClient client) {
		return data.existsAtDestination(client);
	}
	
	public String getDestinationIntermediatePath() {
		if(data.splits > 1) {
			return data.getDestinationIntermediatePath() + id;
		}
		return data.getDestinationIntermediatePath();
	}
	
	public String getDestinationFinalPath() {
		return data.getDestinationFinalPath();
	}
	
	public String getSourcePath() {
		return data.getSourceFilePath();
	}

	public void updateStatus(UploadStatus status) {
		if(UploadStatus.failed == status) {
			data.updateStatus(status);
		}
	}
}