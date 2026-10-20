public class Address {
    private String streetNo;
    private String streetName;
    private int zipCode; // int drops leading zeros (02115 -> 2115); String is safer in real code.

    public Address(String streetNo, String streetName, int zipCode) {
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.zipCode = zipCode;
    }

    public String getStreetNo() { return this.streetNo; }
    public void setStreetNo(String streetNo) { this.streetNo = streetNo; }

    public String getStreetName() { return this.streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public int getZipCode() { return this.zipCode; }
    public void setZipCode(int zipCode) { this.zipCode = zipCode; }
}
