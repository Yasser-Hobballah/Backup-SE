package it.polito.ezshop.data;

public class UserClass implements User {
    private Integer id;
    private String username;
    private String password;
    private String role;

    public UserClass() {
    }

    public UserClass(Integer id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setId(Integer id) {
        if( !(id==null) ){
        if (!(id <= 0)) {
            this.id = id;
        }
    }
    }

    @Override
    public void setUsername(String username) {

        if (!(username == null)) {
            if (!(username.isEmpty() || username.isBlank())) {
                this.username = username;
            }

        }
    }

    @Override
    public void setPassword(String password) {

        if (!(password == null)) {
            if (!(password.isEmpty() || password.isBlank())) {
                this.password = password;
            }
        }

    }

    @Override
    public void setRole(String role) {

        if (!(role == null)) {
            if (role.equals("Cashier") || role.equals("ShopManager") || role.equals("Administrator") || role.equals("empty")) {
                this.role = role;
            }
        }

    }

}