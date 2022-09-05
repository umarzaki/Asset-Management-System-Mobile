package com.example.sarprasfilkom.ui.complaint;

public class ComplaintList {
    private String complaintNumber;
    private String complaintStatus;

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public ComplaintList(String complaintNumber, String complaintStatus) {
        this.complaintNumber = complaintNumber;
        this.complaintStatus = complaintStatus;
    }
}
