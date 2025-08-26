package com.payease.app.constants;

public class EnumHelper {

	public enum UserRole {
	    ADMIN,
	    DISTRIBUTOR,
	    RETAILER,
	    NONE
	}
	
	public enum UserStatus {
	    ACTIVE("Active"),
	    INACTIVE("Inactive"),
	    SUSPENDED("Suspended"),
	    PENDING("Pending"),
	    DELETED("Deleted");

	    private final String label;

	    UserStatus(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }
	}


}
