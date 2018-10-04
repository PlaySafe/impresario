package data;

public class DefaultAddress implements Address {

    private final String street;
    private final String state;
    private final String city;
    private final String postalCode;
    private final String country;

    private DefaultAddress(Builder builder) {
        this.street = builder.street;
        this.state = builder.state;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String getCountry() {
        return country;
    }

    public static class Builder {

        private String street;
        private String state;
        private String city;
        private String postalCode;
        private String country;

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new DefaultAddress(this);
        }

    }

}
