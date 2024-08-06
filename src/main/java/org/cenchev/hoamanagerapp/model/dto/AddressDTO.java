package org.cenchev.hoamanagerapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddressDTO {
    private Long id;

    @NotBlank(message = "Address line cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9 .,:-]*$", message = "Address line can only contain letters, numbers, and some special characters (. , : - )")
    private String addressLine;

    @NotBlank(message = "City cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "City must only contain letters")
    private String city;

    @NotBlank(message = "State cannot be empty")
    @Pattern(regexp = "^(?:AL|AK|AZ|AR|CA|CO|CT|DE|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY|NE)*$", message = "State must only two letters")
    private String state;

    @NotBlank(message = "zip code cannot be empty")
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Zip code must only contain numbers")
    private String usZipCode;

    public AddressDTO(Long id, String addressLine, String city, String state, String usZipCode) {
        this.id = id;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.usZipCode = usZipCode;
    }

    public AddressDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsZipCode() {
        return usZipCode;
    }

    public void setUsZipCode(String usZipCode) {
        this.usZipCode = usZipCode;
    }
}
