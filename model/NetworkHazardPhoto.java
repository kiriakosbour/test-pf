/* Copyright (C) HEDNO - GR, Inc - All Rights Reserved
 * Unauthorized copying of this code without legitimate license is prohibited
 * Application: …
 * Version 1.0
 * This is a short description of the application…
 * Written by Kleopatra Konstanteli <k.konstanteli@deddie.gr>
 * May 2019
 */
package gr.deddie.pfr.model;

public class NetworkHazardPhoto {
	private long id;
	private long failure_id;
	private String name;
	private byte[] photo;
	private Long photo_size;
	private String content_type;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getFailure_id() {
		return failure_id;
	}
	public void setFailure_id(long failure_id) {
		this.failure_id = failure_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public Long getPhoto_size() {
		return photo_size;
	}
	public void setPhoto_size(Long photo_size) {
		this.photo_size = photo_size;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
}
