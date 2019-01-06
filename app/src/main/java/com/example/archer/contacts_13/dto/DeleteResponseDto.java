package com.example.archer.contacts_13.dto;

public class DeleteResponseDto {
    private String status;

    public DeleteResponseDto() {
    }

    public DeleteResponseDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DeleteResponseDto{" +
                "status='" + status + '\'' +
                '}';
    }
}
