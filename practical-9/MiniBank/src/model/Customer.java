package model;

public class Customer implements Cloneable {

    private String name;
    private String email;
    private String mobile;
    private final String customerId;
    private static long customerCounter = 100;
    private Address address;

    private static String generateCustomerId() {
        customerCounter++;
        return String.format("CUST%03d", customerCounter);
    }

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Customer clone() {
        try {
            Customer copy = (Customer) super.clone();
            if (address != null) {
                copy.address = new Address(address.line, address.city, address.pincode);
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public static class Address {
        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }
    }
}
