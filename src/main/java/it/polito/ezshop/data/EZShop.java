package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject; ////////////ADD TO MAVEN////////////////
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EZShop implements EZShopInterface {

    private SaleTransactionClass currentTransaction = new SaleTransactionClass(); // sale transaction
    private SaleTransactionClass currentReturn = new SaleTransactionClass(); // return transaction
    private SaleTransactionClass currentSaleTransaction = new SaleTransactionClass(); // sale for return transaction
    private JSONArray sales = new JSONArray();
    private JSONArray returns = new JSONArray();
    UserClass user = new UserClass(); // Currently logged in user
    JSONArray UserListGlobal = new JSONArray();
    int PTID = 0;
    int OID = 0;
    int CID = 0;
    JSONArray OrderListGlobal = new JSONArray();
    String BalanceType = "DEFAULT";

    // 49
    ////////////////////////////////////////////////////////////////////////////// YASSER
    @Override
    public void reset() { // THIS FUNCTION RESETS EVERYTHING

        try {
            new FileWriter("Balance.json", false).close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            new FileWriter("SaleTransaction.json", false).close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            new FileWriter("ProductList.json", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new FileWriter("ReturnTransaction.json", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new FileWriter("Users.json", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new FileWriter("Customers.json", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new FileWriter("OrderList.json", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer createUser(String username, String password, String role)
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

        JSONObject UserData = new JSONObject();
        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        Integer id = 0, idextract = 0, max = 0;
        Boolean isfound = false;
        JSONArray UserList = new JSONArray(); // create a list of jason objects to store them in a file
        String name;
        String role_1 = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";

        if (!(role_1.equals(role) || role_2.equals(role) || role_3.equals(role)) || role == null || role.isBlank()) {
            throw new InvalidRoleException("Invalid Role");
        }

        if (username == null || username.isEmpty() || username.isBlank()) {
            throw new InvalidUsernameException("Invalid Username");
        }

        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new InvalidPasswordException("Invalid Password");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Users.json");

        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                UserList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < UserList.size(); i++) {

            DataAtIndex = UserList.get(i);
            Data = (JSONObject) DataAtIndex;
            idextract = Integer.parseInt(String.valueOf(Data.get("id")));
            if (idextract > max) {
                max = idextract;
            }
            name = (String) Data.get("Username");
            if (name.equals(username)) {
                isfound = true;
                break;
            }
        }

        if (isfound) {
            return -1;
        }

        id = max + 1; // get the next ID for the user.

        UserData.put("id", id);
        UserData.put("Username", username); // fill the fields of the jason object
        UserData.put("Password", password);
        UserData.put("Role", role);

        UserList.add(UserData); // add a certain object to the list
        UserListGlobal = UserList;
        // Write JSON file
        try (FileWriter file = new FileWriter("Users.json")) {
            // We can write any JSONArray or JSONObject instance to the file
            file.write(UserList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;

    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();

        int idextracted = 0;
        Boolean isfound = false;
        String role = "Administrator", num = "";

        if (id == null || id <= 0) {
            throw new InvalidUserIdException("Invalid id");
        }
        
        if ( !role.equals(user.getRole()) || user.getUsername() == null) {
            throw new UnauthorizedException("Invalid Role");
        }

        for (int i = 0; i < UserListGlobal.size(); i++) {

            DataAtIndex = UserListGlobal.get(i);
            Data = (JSONObject) DataAtIndex;

            num = Data.get("id").toString();
            idextracted = Integer.parseInt(num);

            if (id == idextracted) {

                UserListGlobal.remove(i);
                // Write Changes to JSON file
                try (FileWriter file = new FileWriter("Users.json")) {
                    // We can write any JSONArray or JSONObject instance to the file
                    file.write(UserListGlobal.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                isfound = true;
                break;
            }
        }
        if (isfound) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {

        UserClass[] u;
        u = new UserClass[UserListGlobal.size()];
        List<User> userslist = new ArrayList<User>();
        String role = "Administrator";
        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        String pass, name;
        Integer id = 0;

        if (!role.equals(user.getRole()) || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        for (int i = 0; i < UserListGlobal.size(); i++) {

            u[i] = new UserClass(); // initialize objects of the class
        }

        for (int i = 0; i < UserListGlobal.size(); i++) {

            DataAtIndex = UserListGlobal.get(i);
            Data = (JSONObject) DataAtIndex;
            id = Integer.parseInt(String.valueOf(Data.get("id")));
            role = (String) Data.get("Role");
            pass = (String) Data.get("Password");
            name = (String) Data.get("Username");
            u[i].setUsername(name);
            u[i].setPassword(pass);
            u[i].setRole(role);
            u[i].setId(id);

            userslist.add(u[i]);

        }

        return userslist;

    }

    @Override

    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        UserClass u = new UserClass();
        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        long idlong = 0;
        Integer idextracted = 0;
        Boolean isfound = false;
        String role = "Administrator", pass, name, num = "";

        if (id == null || id <= 0) {
            throw new InvalidUserIdException("Invalid id");
        }
        if (!role.equals(user.getRole()) || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        for (int i = 0; i < UserListGlobal.size(); i++) {

            DataAtIndex = UserListGlobal.get(i);
            Data = (JSONObject) DataAtIndex;

            num = Data.get("id").toString(); // get the id
            idextracted = Integer.parseInt(num);

            if (id == idextracted) {

                id = Integer.parseInt(String.valueOf(Data.get("id")));
                role = (String) Data.get("Role");
                pass = (String) Data.get("Password");
                name = (String) Data.get("Username");
                u.setUsername(name);
                u.setPassword(pass);
                u.setRole(role);
                u.setId(id);

                isfound = true;
                break;
            }

        }

        if (isfound) {
            return u;

        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings("unchecked")

    public boolean updateUserRights(Integer id, String role)
            throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        Integer idextracted = 0;
        Boolean isfound = false;
        String role2 = "Administrator", num = "";

        if (id == null || id <= 0) {
            throw new InvalidUserIdException("Invalid id");
        }
        if (!role2.equals(user.getRole()) || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        if (  role == null || !role.equals("Administrator") && !role.equals("Cashier") && !role.equals("ShopManager")  ) {
            throw new InvalidRoleException("Invalid Role");
        }

        for (int i = 0; i < UserListGlobal.size(); i++) {

            DataAtIndex = UserListGlobal.get(i);
            Data = (JSONObject) DataAtIndex;

            num = Data.get("id").toString();
            idextracted = Integer.parseInt(num);

            if (id == idextracted) {

                Data.replace("Role", role);
                UserListGlobal.set(i, Data);
                // Write JSON file
                try (FileWriter file = new FileWriter("Users.json")) {
                    // We can write any JSONArray or JSONObject instance to the file
                    file.write(UserListGlobal.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isfound = true;
                break;
            }

        }

        if (isfound) {
            return true;

        } else {
            return false;
        }

    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        long idlong = 0;
        int id = 0;
        Boolean isfound = false;
        JSONArray UserList = new JSONArray(); // create a list of jason objects to store them in a file
        String name, role, pass;

        if (username == null || username.isEmpty() || username.isBlank()) {
            throw new InvalidUsernameException("Invalid Username");
        }

        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new InvalidPasswordException();
        }
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Users.json");

        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                UserList = (JSONArray) obj;
                UserListGlobal = UserList; // for global purposes no need to re-Read file everytime
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < UserList.size(); i++) {

            DataAtIndex = UserList.get(i);
            Data = (JSONObject) DataAtIndex;
            name = (String) Data.get("Username");
            if (name.equals(username)) {

                idlong = (long) Data.get("id");
                id = (int) idlong;
                role = (String) Data.get("Role");
                pass = (String) Data.get("Password");
                user.setUsername(name);
                user.setPassword(pass);
                user.setRole(role);
                user.setId(id);

                isfound = true;
                break;
            }
        }
        if (isfound) {
            if (password.equals(user.getPassword())) {
                return user;
            }
            return null;
        } else {
            return null;
        }

    }

    @Override
    public boolean logout() {

        if (  user.getUsername() == "empty" ){
            return false;
        }

        if (user.getUsername() != null || !user.getUsername().isBlank() ) { // so
                                                                                                            // someone
                                                                                                            // is logged
                                                                                                            // in
            user.setId(0);
            user.setPassword("empty");
            user.setUsername("empty");
            user.setRole("empty");
            
            return true;
        } else { // no one is logged in
            return false;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////// End
    /////////////////////////////////////////////////////////////////////////////////////// Yasser
    /////////////////////////////////////////////////////////////////////////////////////// -
    /////////////////////////////////////////////////////////////////////////////////////// Start
    /////////////////////////////////////////////////////////////////////////////////////// Giorgio
    @Override
    @SuppressWarnings("unchecked")
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
            throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
            UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager";
        Integer idextract = 0, max = 0, id = 0;
        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if ( productCode == null || productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12
                || productCode.isBlank()) {
            throw new InvalidProductCodeException("product code error");
        }

        if (description == null || description.isEmpty() || description.isBlank()) {
            throw new InvalidProductDescriptionException("Invalid Description");
        }

        if (pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException("Invalid price");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONArray tmp = new JSONArray();
        JSONObject p = new JSONObject();
        boolean found = false;
        File f = new File("ProductList.json");
        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tmp.size(); i++) {
            p = (JSONObject) tmp.get(i);
            idextract = Integer.parseInt(String.valueOf(p.get("id")));
            if (idextract > max) {
                max = idextract;
            }
            if (p.get("barCode").toString().equals(productCode.toString())) { // finding right item
                found = true;
            }
        }

        if (found)
            return -1;

        id = max + 1;
        // creating JSON Object to be write in file
        JSONObject pt = new JSONObject();
        String location = "000-000-000";
        pt.put("quantity", 0);
        pt.put("location", location);
        pt.put("note", note);
        pt.put("productDescription", description);
        pt.put("barCode", productCode);
        pt.put("pricePerUnit", pricePerUnit);
        pt.put("id", id);

        tmp.add(pt);

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
            throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
            InvalidPricePerUnitException, UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        // throwing exceptions
        if (user.getUsername() == null || !(role.equals(user.getRole()) || role_2.equals(user.getRole()))
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if ( newCode == null || newCode.isEmpty() || newCode.length() > 14 || newCode.length() < 12 || newCode.isBlank()) {
            throw new InvalidProductCodeException("product code error");
        }

        if (newDescription == null || newDescription.isEmpty() || newDescription.isBlank()) {
            throw new InvalidProductDescriptionException("Invalid Description");
        }

        if (newPrice <= 0) {
            throw new InvalidPricePerUnitException("Invalid price");
        }

        if (id <= 0) {
            throw new InvalidProductIdException("Invalid ID");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("ProductList.json")) { // still need to add check for first user sign
                                                                       // in.
            Object obj = jsonParser.parse(reader);
            tmp = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        // Updating data in a temporary JSON array "tmp"ù

        JSONObject pt = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;
        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i);
            if (pt.get("id").toString().equals(id.toString())) { // finding right item
                pt.replace("productDescription", newDescription);
                pt.replace("barCode", newCode);
                pt.replace("pricePerUnit", newPrice);
                pt.replace("note", newNote);
                tmp.set(i, pt);
                found = true;
            }
        }

        if (!found)
            return false;

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (id == null || id <= 0) {
            throw new InvalidProductIdException("Invalid ID");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("ProductList.json")) { // still need to add check for first user sign
                                                                       // in.
            Object obj = jsonParser.parse(reader);
            tmp = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        // Updating data in a temporary JSON array "tmp"

        JSONObject pt = new JSONObject(); // temporary JSON Object to read data
        // JSONArray toReturn = new JSONArray();
        Boolean found = false;
        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i);
            if (pt.get("id").toString().equals(id.toString())) { // finding right item
                tmp.remove(pt);
                found = true;
            }

        }

        if (!found)
            return false;

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {

        List<ProductType> PTList = new ArrayList<ProductType>(); // array to return
        String role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))
                || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("ProductList.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        Integer quantity = 0, id = 0;
        String location = "", note = "", productDescription = "", barCode = "";
        Double pricePerUnit = 0.0;
        String quantityTMP = "", idTMP = "", pricePerUnitTMP = "";
        JSONObject pt = new JSONObject();

        int i;
        for (i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i); // temporary JSON Object to read data
            quantityTMP = pt.get("quantity").toString(); // getting data
            quantity = Integer.parseInt(quantityTMP);
            location = pt.get("location").toString();
            note = pt.get("note").toString();
            productDescription = pt.get("productDescription").toString();
            barCode = pt.get("barCode").toString();
            pricePerUnitTMP = pt.get("pricePerUnit").toString();
            pricePerUnit = Double.parseDouble(pricePerUnitTMP);
            idTMP = pt.get("id").toString();
            id = Integer.parseInt(idTMP);
            PTList.add(new ProductTypeClass(quantity, location, note, productDescription, barCode, pricePerUnit, id)); // adding
                                                                                                                       // new
                                                                                                                       // PT
                                                                                                                       // passing
                                                                                                                       // parameters
                                                                                                                       // by
                                                                                                                       // constructor
        }
        PTID = i; // saving the next ID

        return PTList;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode)
            throws InvalidProductCodeException, UnauthorizedException {

        ProductTypeClass PT; // PT to return
        String role = "Administrator", role_2 = "ShopManager";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        if (barCode.isEmpty() || barCode == null || barCode.length() > 14 || barCode.length() < 12 || barCode.isBlank()) {
            throw new InvalidProductCodeException("Product code error");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("ProductList.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        String barCodeTMP = "";
        JSONObject pt = new JSONObject();
        Integer quantity = 0, id = 0;
        String location = "", note = "", productDescription = "";
        Double pricePerUnit = 0.0;
        String quantityTMP = "", idTMP = "", pricePerUnitTMP = "";
        boolean found = false;
        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i); // temporary JSON Object to read data

            barCodeTMP = pt.get("barCode").toString();
            if (barCodeTMP.equals(barCode)) {
                // ending for
                i = tmp.size();
                // getting data from JSON obj
                quantityTMP = pt.get("quantity").toString(); // getting data
                quantity = Integer.parseInt(quantityTMP);
                location = pt.get("location").toString();
                note = pt.get("note").toString();
                productDescription = pt.get("productDescription").toString();
                pricePerUnitTMP = pt.get("pricePerUnit").toString();
                pricePerUnit = Double.parseDouble(pricePerUnitTMP);
                idTMP = pt.get("id").toString();
                id = Integer.parseInt(idTMP);
                found = true;
            }
        }

        if (!found) {
            return null;
        }

        PT = new ProductTypeClass(quantity, location, note, productDescription, barCode, pricePerUnit, id);
        return PT;
    }

    public ProductType getProductByBarCode(String barCode) throws InvalidProductCodeException {

        ProductTypeClass PT; // PT to return
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        if (barCode.isEmpty() || barCode.length() > 14 || barCode.length() < 12 || barCode.isBlank()) {
            throw new InvalidProductCodeException("Product code error");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("ProductList.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        String barCodeTMP = "";
        JSONObject pt = new JSONObject();
        Integer quantity = 0, id = 0;
        String location = "", note = "", productDescription = "";
        Double pricePerUnit = 0.0;
        String quantityTMP = "", idTMP = "", pricePerUnitTMP = "";
        boolean found = false;
        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i); // temporary JSON Object to read data

            barCodeTMP = pt.get("barCode").toString();
            if (barCodeTMP.equals(barCode)) {
                // ending for
                i = tmp.size();
                // getting data from JSON obj
                quantityTMP = pt.get("quantity").toString(); // getting data
                quantity = Integer.parseInt(quantityTMP);
                location = pt.get("location").toString();
                note = pt.get("note").toString();
                productDescription = pt.get("productDescription").toString();
                pricePerUnitTMP = pt.get("pricePerUnit").toString();
                pricePerUnit = Double.parseDouble(pricePerUnitTMP);
                idTMP = pt.get("id").toString();
                id = Integer.parseInt(idTMP);
                found = true;
            }
        }

        if (!found) {
            return null;
        }

        PT = new ProductTypeClass(quantity, location, note, productDescription, barCode, pricePerUnit, id);
        return PT;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

        List<ProductType> PTList = new ArrayList<ProductType>(); // array to return
        String role = "Administrator", role_2 = "ShopManager";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("ProductList.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        String descriptionTMP = "";
        JSONObject pt = new JSONObject();
        Integer quantity = 0, id = 0;
        String location = "", note = "", productDescription = "", barCode = "";
        Double pricePerUnit = 0.0;
        String quantityTMP = "", idTMP = "", pricePerUnitTMP = "";

        if (description == null) {
            description = "";
        }

        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i); // temporary JSON Object to read data

            descriptionTMP = pt.get("productDescription").toString();
            if (descriptionTMP.contains(description)) {
                // getting data from JSON obj
                quantityTMP = pt.get("quantity").toString(); // getting data
                quantity = Integer.parseInt(quantityTMP);
                location = pt.get("location").toString();
                note = pt.get("note").toString();
                productDescription = pt.get("productDescription").toString();
                barCode = pt.get("barCode").toString();
                pricePerUnitTMP = pt.get("pricePerUnit").toString();
                pricePerUnit = Double.parseDouble(pricePerUnitTMP);
                idTMP = pt.get("id").toString();
                id = Integer.parseInt(idTMP);
                PTList.add(
                        new ProductTypeClass(quantity, location, note, productDescription, barCode, pricePerUnit, id));
            }
        }
        return PTList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean updateQuantity(Integer productId, int toBeAdded)
            throws InvalidProductIdException, UnauthorizedException {
        String role = "Administrator", role_2 = "ShopManager", loc = "", defaultloc = "000-000-000";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (productId==null || productId <= 0) {
            throw new InvalidProductIdException("Invalid ID");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("ProductList.json")) { // still need to add check for first user sign
                                                                       // in.
            Object obj = jsonParser.parse(reader);
            tmp = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        // Updating data in a temporary JSON array "tmp"ù

        JSONObject pt = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;
        String quantityS = "";
        Integer quantity = 0;

        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i);
            if (pt.get("id").toString().equals(productId.toString())) { // finding right item
                quantityS = pt.get("quantity").toString();
                quantity = Integer.parseInt(quantityS);
                loc = pt.get("location").toString();
                quantity = quantity + toBeAdded;
                if (quantity < 0 || loc.isEmpty() || loc.equals(defaultloc)) {
                    return false;
                }
                pt.replace("quantity", quantity);
                tmp.set(i, pt);
                found = true;
            }
        }

        if (!found)
            return false;

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean updateQuantityinternal(Integer productId, int toBeAdded) // CASHIER METHOD
            throws InvalidProductIdException {
        String loc = "", defaultloc = "000-000-000";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        // throwing exceptions
        if (productId <= 0) {
            throw new InvalidProductIdException("Invalid ID");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("ProductList.json")) { // still need to add check for first user sign
                                                                       // in.
            Object obj = jsonParser.parse(reader);
            tmp = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        // Updating data in a temporary JSON array "tmp"ù

        JSONObject pt = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;
        // String quantityS = "";
        Integer quantity = 0;

        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i);
            if (pt.get("id").toString().equals(productId.toString())) { // finding right item
                /*
                 * quantityS = pt.get("quantity").toString(); quantity =
                 * Integer.parseInt(quantityS); loc = pt.get("location").toString();
                 */
                quantity = (int) (long) pt.get("quantity");
                loc = (String) pt.get("location");
                quantity = quantity + toBeAdded;
                if (quantity < 0 || loc.isEmpty() || loc.equals(defaultloc)) {
                    return false;
                }
                pt.replace("quantity", quantity);
                tmp.set(i, pt);
                found = true;
            }
        }

        if (!found)
            return false;

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean updatePosition(Integer productId, String newPos)
            throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        String role = "Administrator", role_2 = "ShopManager";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file
        String temp = "-";
        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (productId==null || productId <= 0) {
            throw new InvalidProductIdException("Invalid ID");
        }

        if (newPos.isEmpty() || newPos.isBlank()) {
            throw new InvalidLocationException("Location invalid");
        }

        if (newPos.split("-").length != 2) {
            throw new InvalidLocationException("Location invalid");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("ProductList.json")) { // still need to add check for first user sign
                                                                       // in.
            Object obj = jsonParser.parse(reader);
            tmp = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        // Updating data in a temporary JSON array "tmp"ù

        JSONObject pt = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;

        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i);
            if (pt.get("id").toString().equals(productId.toString())) { // finding right item
                pt.replace("location", newPos);
                tmp.set(i, pt);
                found = true;
            }
        }

        if (!found)
            return false;

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
            InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager";
        Integer max = 0, idextract = 0;
        // JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from
        // JSON file

        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException("Invalid quantity");
        }
        if (pricePerUnit <= 0.0) {
            throw new InvalidPricePerUnitException("Invalid price");
        }
        if (productCode == null || productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12) {
            throw new InvalidProductCodeException("Invalid product code");
        }

        // throwing invalid product code
        JSONParser jsonParser = new JSONParser();
        JSONArray tmp = new JSONArray();
        File f1 = new File("ProductList.json");
        try (FileReader reader = new FileReader(f1)) { // still need to add check for first user sign
            if (!(f1.length() == 0)) { // in.
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        JSONObject p = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;

        for (int i = 0; i < tmp.size(); i++) {
            p = (JSONObject) tmp.get(i);
            p.get("id");

            if (p.get("barCode").toString().equals(productCode.toString())) { // finding right item
                found = true;
            }
        }

        if (!found)
            return -1;

        // JSON parser object to parse read file
        JSONParser jsonParse = new JSONParser();
        JSONArray tm = new JSONArray();

        File f = new File("OrderList.json");
        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            if (!(f.length() == 0)) {
                Object obj = jsonParse.parse(reader);
                tm = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tm.size(); i++) {
            p = (JSONObject) tm.get(i);
            p.get("id");
            idextract = Integer.parseInt(String.valueOf(p.get("id")));
            if (idextract > max) {
                max = idextract;
            }
        }
        max++;
        // creating JSON Object to be write in file
        JSONObject pt = new JSONObject();
        String status = "ISSUED";
        pt.put("balanceId", 0);
        pt.put("quantity", quantity);
        pt.put("productCode", productCode);
        pt.put("pricePerUnit", pricePerUnit);
        pt.put("status", status);
        pt.put("id", max);

        tm.add(pt);

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("OrderList.json")) {
            file.write(tm.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return max;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
            UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager";
        boolean success = true;
        Integer idextract = 0, max = 0;
        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException("Invalid quantity");
        }
        if (pricePerUnit <= 0.0) {
            throw new InvalidPricePerUnitException("Invalid price");
        }
        if (productCode == null || productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12) {
            throw new InvalidProductCodeException("Invalid product Code");
        }

        // throwing invalid product code
        JSONParser jsonParser = new JSONParser();
        JSONArray tmp = new JSONArray();
        File f1 = new File("ProductList.json");
        try (FileReader reader = new FileReader(f1)) {
            if (!(f1.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        JSONObject p = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;

        for (int i = 0; i < tmp.size(); i++) {

            p = (JSONObject) tmp.get(i);

            if (p.get("barCode").toString().equals(productCode.toString())) { // finding right item
                found = true;
            }
        }

        if (!found)
            return -1;

        // JSON parser object to parse read file
        JSONParser jsonParse = new JSONParser();
        JSONArray tm = new JSONArray();

        File f = new File("OrderList.json");
        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            if (!(f.length() == 0)) {
                Object obj = jsonParse.parse(reader);
                tm = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tm.size(); i++) {
            p = (JSONObject) tm.get(i);
            p.get("id");
            idextract = Integer.parseInt(String.valueOf(p.get("id")));
            if (idextract > max) {
                max = idextract;
            }
        }
        max++;
        // creating JSON Object to be write in file
        JSONObject pt = new JSONObject();
        String status = "PAYED";
        pt.put("balanceId", 0);
        pt.put("quantity", quantity);
        pt.put("productCode", productCode);
        pt.put("pricePerUnit", pricePerUnit);
        pt.put("status", status);
        pt.put("id", max);

        tm.add(pt);

        BalanceType = "ORDER";
        pricePerUnit = (double) (quantity * pricePerUnit);
        success = recordBalanceUpdate(-pricePerUnit);
        if (!success) {
            return -1;
        }

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("OrderList.json")) {
            file.write(tm.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return max;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager", quantityString = "", priceperunitstString = "";
        int quantity = 0;
        double pricePerUnit = 0;
        boolean success = true;
        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if ( orderId == null || orderId <= 0) {
            throw new InvalidOrderIdException("Invalid order ID");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONArray tmp = new JSONArray();

        File f = new File("OrderList.json");
        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        JSONObject p = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;
        String status = "PAYED", tempstatus = "", temp = "COMPLETED";

        for (int i = 0; i < tmp.size(); i++) {
            p = (JSONObject) tmp.get(i);
            if (p.get("id").toString().equals(orderId.toString())) { // finding right item
                quantityString = p.get("quantity").toString();
                priceperunitstString = p.get("pricePerUnit").toString();
                tempstatus = p.get("status").toString();
                p.replace("status", status);
                tmp.set(i, p);
                found = true;
            }
        }

        if (!found || tempstatus.equals(temp))
            return false;

        quantity = Integer.parseInt(quantityString);
        pricePerUnit = Double.parseDouble(priceperunitstString);

        BalanceType = "ORDER";

        pricePerUnit = (double) (quantity * pricePerUnit);
        success = recordBalanceUpdate(-pricePerUnit);

        if (!success) {
            return false;
        }

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("OrderList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean recordOrderArrival(Integer orderId)
            throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

        String role = "Administrator", role_2 = "ShopManager";

        // throwing exceptions
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (orderId <= 0) {
            throw new InvalidOrderIdException("Invalid order ID");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONArray tmp = new JSONArray();

        File f = new File("OrderList.json");
        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        JSONObject p = new JSONObject(); // temporary JSON Object to read data
        Boolean found = false;
        String status = "COMPLETED", productCode = "", toBeAddedS = "", temp = "", temp3 = "PAYED";
        Integer toBeAdded = 0;

        for (int i = 0; i < tmp.size(); i++) {
            p = (JSONObject) tmp.get(i);
            if (p.get("id").toString().equals(orderId.toString())) { // finding right item
                productCode = p.get("productCode").toString(); // saving productCode to update quantity
                temp = p.get("status").toString();
                p.replace("status", status);
                toBeAddedS = p.get("quantity").toString();
                toBeAdded = Integer.parseInt(toBeAddedS);
                tmp.set(i, p);
                found = true;
                i = tmp.size(); // ending for
            }
        }

        if (!found || !temp.equals(temp3))
            return false;

        // throwing invalid location
        JSONParser jsonParserP = new JSONParser();
        JSONArray tmpP = new JSONArray();

        try (FileReader reader = new FileReader("ProductList.json")) { // still need to add check for first user sign
                                                                       // in.
            Object obj = jsonParserP.parse(reader);
            tmpP = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        JSONObject pt = new JSONObject(); // temporary JSON Object to read data
        Boolean foundP = false, prodFound = false;
        Integer quantity = 0;
        String quantityS = "", loc = "000-000-000";

        for (int i = 0; i < tmpP.size(); i++) {
            pt = (JSONObject) tmpP.get(i);
            if (pt.get("barCode").toString().equals(productCode.toString())) { // finding right item
                prodFound = true;
                if (pt.get("location").toString().equals(loc)) {
                    foundP = true;
                } else {
                    quantityS = pt.get("quantity").toString();
                    quantity = Integer.parseInt(quantityS);
                    quantity = quantity + toBeAdded;
                    pt.replace("quantity", quantity); // updating quantity
                }
                i = tmpP.size();
            }
        }

        if (foundP)
            throw new InvalidLocationException("Invalid location");

        if (!prodFound) // check if the product was not cancelled
            return false;

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("ProductList.json")) {
            file.write(tmpP.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // writing new JSON Array to file
        try (FileWriter file = new FileWriter("OrderList.json")) {
            file.write(tmp.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {

        List<Order> OList = new ArrayList<Order>(); // array to return
        String role = "Administrator", role_2 = "ShopManager";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole())) || user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("OrderList.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        Integer quantity = 0, id = 0, balanceId = 0;
        String status = "", productCode = "", balanceIdTMP = "";
        Double pricePerUnit = 0.0;
        String quantityTMP = "", idTMP = "", pricePerUnitTMP = "";
        JSONObject pt = new JSONObject();

        int i;
        for (i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i); // temporary JSON Object to read data
            quantityTMP = pt.get("quantity").toString(); // getting data
            quantity = Integer.parseInt(quantityTMP);
            status = pt.get("status").toString();
            productCode = pt.get("productCode").toString();
            balanceIdTMP = pt.get("balanceId").toString();
            balanceId = Integer.parseInt(balanceIdTMP);
            pricePerUnitTMP = pt.get("pricePerUnit").toString();
            pricePerUnit = Double.parseDouble(pricePerUnitTMP);
            idTMP = pt.get("id").toString();
            id = Integer.parseInt(idTMP);
            OList.add(new OrderClass(balanceId, productCode, pricePerUnit, quantity, status, id)); // adding new PT
                                                                                                   // passing parameters
                                                                                                   // by constructor
        }

        return OList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////// End
    ///////////////////////////////////////////////////////////////////////////////////////////////// Giorgio
    ///////////////////////////////////////////////////////////////////////////////////////////////// -
    ///////////////////////////////////////////////////////////////////////////////////////////////// Start
    ///////////////////////////////////////////////////////////////////////////////////////////////// Anyone
    @Override
    @SuppressWarnings("unchecked")
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {

        JSONObject CustomerData = new JSONObject();
        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        Integer id = 0, idextract = 0, max = 0;
        int point = 0;
        Boolean isfound = false;
        JSONArray CustomerList = new JSONArray(); // create a list of jason objects to store them in a file
        String name, role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier", empty = "";

        if (customerName == null || customerName.isEmpty() || customerName.isBlank()) {
            throw new InvalidCustomerNameException("Invalid Customer name");
        }

        if (user.getRole() == null || user.getRole().isEmpty() || user.getRole().isBlank()
                || !(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Customers.json");

        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                CustomerList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < CustomerList.size(); i++) {

            DataAtIndex = CustomerList.get(i);
            Data = (JSONObject) DataAtIndex;
            idextract = Integer.parseInt(String.valueOf(Data.get("id")));
            if (idextract > max) {
                max = idextract;
            }
            name = (String) Data.get("CustomerName");
            if (name.equals(customerName)) {
                isfound = true;
                break;
            }
        }

        if (isfound) {
            return -1;
        }

        id = max + 1; // get the next ID for the user.

        CustomerData.put("id", id);
        CustomerData.put("CustomerName", customerName); // when defining customer we don't know the Customercard so it
                                                        // is empty for now
        CustomerData.put("CustomerCard", empty);
        CustomerData.put("Points", point);

        CustomerList.add(CustomerData); // add a certain object to the list

        // Write JSON file
        try (FileWriter file = new FileWriter("Customers.json")) {
            // We can write any JSONArray or JSONObject instance to the file
            file.write(CustomerList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
            throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
            UnauthorizedException {

        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        Boolean isfound = false;
        JSONArray CustomerList = new JSONArray(); // create a list of jason objects to store them in a file
        String name, num = "", card, role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier", no = null;
        Integer idextracted = 0;
        int fd = 0, point = 0;

        if (id == null || id <= 0) {
            throw new InvalidCustomerIdException("Invalid id");
        }

        if (newCustomerName == null || newCustomerName.isEmpty() || newCustomerName.isBlank()) {
            throw new InvalidCustomerNameException("Invalid Customer name");
        }

        if (user.getRole() == null || user.getRole().isEmpty() || user.getRole().isBlank()
                || !(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))) {
            throw new UnauthorizedException("Invalid Role");
        }
        if (newCustomerCard.length() != 10 || newCustomerCard.matches(".*[a-z].*") || newCustomerCard.matches(".*[A-Z].*") ) {
            throw new InvalidCustomerCardException("Invalid Customer Card");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Customers.json");

        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                CustomerList = (JSONArray) obj;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < CustomerList.size(); i++) {

            DataAtIndex = CustomerList.get(i);
            Data = (JSONObject) DataAtIndex;
            card = (String) Data.get("CustomerCard");
            if (card.equals(newCustomerCard)) {
                return false;

            }

        }

        for (int i = 0; i < CustomerList.size(); i++) {

            DataAtIndex = CustomerList.get(i);
            Data = (JSONObject) DataAtIndex;
            name = (String) Data.get("CustomerName");
            card = (String) Data.get("CustomerCard");
            point = Integer.parseInt(Data.get("Points").toString());
            num = Data.get("id").toString();
            idextracted = Integer.parseInt(num);

            if (id == idextracted) {

                if (!newCustomerCard.isEmpty() && !newCustomerCard.equals(no)) {
                    if (newCustomerCard.length() != 10) {
                        throw new InvalidCustomerCardException("Invalid Customer Card");
                    }
                    Data.replace("Points", 0);
                    Data.replace("CustomerName", newCustomerName);
                    Data.replace("CustomerCard", newCustomerCard);

                } else {

                    if (newCustomerCard.isEmpty()) { // if i am here this means i want to delete CARD

                        Data.replace("Points", 0);
                        Data.replace("CustomerName", newCustomerName);
                        Data.replace("CustomerCard", "");

                    }

                    else {
                        Data.replace("CustomerName", newCustomerName);

                    }
                }

                CustomerList.set(i, Data);

                // Write JSON file
                try (FileWriter file = new FileWriter("Customers.json")) {
                    // We can write any JSONArray or JSONObject instance to the file
                    file.write(CustomerList.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                isfound = true;
                break;
            }

        }

        if (!isfound) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        String num = "", role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";
        int idextracted = 0;
        Boolean isfound = false;
        JSONArray CustomerList = new JSONArray();

        if (id == null) {
            throw new InvalidCustomerIdException("Invalid id");
        }
        if (id <= 0) {
            throw new InvalidCustomerIdException("Invalid id");
        }
        if (user.getRole() == null || user.getRole().isEmpty() || user.getRole().isBlank()
                || !(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Customers.json");

        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                CustomerList = (JSONArray) obj;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < CustomerList.size(); i++) {

            DataAtIndex = CustomerList.get(i);
            Data = (JSONObject) DataAtIndex;

            num = Data.get("id").toString();
            idextracted = Integer.parseInt(num);

            if (id == idextracted) {

                CustomerList.remove(i);

                // Write Changes to JSON file
                try (FileWriter file = new FileWriter("Customers.json")) {
                    // We can write any JSONArray or JSONObject instance to the file
                    file.write(CustomerList.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                isfound = true;
                break;
            }
        }
        if (isfound) {
            return true;
        } else {
            return false;
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////// EndYasser
    //////////////////////////////////////////////////////////////////////////////////////// -
    //////////////////////////////////////////////////////////////////////////////////////// Giorgio
    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        CustomerClass c; // PT to return
        String role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file
        Integer idn = 0;
        boolean found = false;
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))
                || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        if (id == null || id <= 0) {
            throw new InvalidCustomerIdException("Invalid id");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("Customers.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        String idTMP = "";
        JSONObject pt = new JSONObject();
        Integer points = 0;
        String CustomerCard = "";
        String pointsTMP = "", CustomerName = "";

        for (int i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i); // temporary JSON Object to read data

            idTMP = pt.get("id").toString();
            idn = Integer.parseInt(idTMP);
            if (idn == id) {
                // ending for
                i = tmp.size();
                // getting data from JSON obj
                pointsTMP = pt.get("Points").toString(); // getting data
                points = Integer.parseInt(pointsTMP);
                CustomerCard = pt.get("CustomerCard").toString();
                CustomerName = pt.get("CustomerName").toString();
                found = true;
            }
        }

        if (found) {
            c = new CustomerClass(CustomerName, id, CustomerCard, points);
            return c;
        }

        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        List<Customer> CList = new ArrayList<Customer>(); // array to return
        String role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";
        JSONArray tmp = new JSONArray(); // temporary JSON Array to store data from JSON file

        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))
                || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("Customers.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        String idTMP = "";
        JSONObject pt = new JSONObject();
        Integer points = 0, id = 0;
        String CustomerCard = "";
        String pointsTMP = "", CustomerName = "";

        int i;
        for (i = 0; i < tmp.size(); i++) {
            pt = (JSONObject) tmp.get(i);
            pointsTMP = pt.get("Points").toString(); // getting data
            points = Integer.parseInt(pointsTMP);
            idTMP = pt.get("id").toString(); // getting data
            id = Integer.parseInt(idTMP);
            CustomerCard = pt.get("CustomerCard").toString();
            CustomerName = pt.get("CustomerName").toString();

            CList.add(new CustomerClass(CustomerName, id, CustomerCard, points)); // adding
            // new
            // PT
            // passing
            // parameters
            // by
            // constructor
        }
        // CID = i; // saving the next ID

        return CList;

    }

    @Override
    public String createCard() throws UnauthorizedException {
        String role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";
        String CARD2 = "";
        long cardtemp = 0;
        boolean istrue = true;
        JSONObject C = new JSONObject();
        int i = 0;
        JSONArray tmp = new JSONArray();
        if (!(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))
                || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File f = new File("Customers.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                Object obj = jsonParser.parse(reader);
                tmp = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (java.lang.ClassCastException e) {
            e.printStackTrace();
        }

        while (istrue) {

            cardtemp = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
            istrue = false;
            for (i = 0; i < tmp.size(); i++) {

                C = (JSONObject) tmp.get(i);
                CARD2 = C.get("CustomerCard").toString();
                if (CARD2.equals(String.valueOf(cardtemp))) {
                    istrue = true;
                    i = tmp.size();
                }

            }

        }

        return String.valueOf(cardtemp);

    }

    ///////////////////////////////////////////////////////////////////////////////////////// Simone
    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId)
            throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if ( customerCard == null || customerCard.isEmpty() || customerCard.isBlank() || customerCard.length() != 10 || customerCard.matches(".*[a-z].*") || customerCard.matches(".*[A-Z].*") ) {
            throw new InvalidCustomerCardException();
        }
        if (customerId == null || customerId <= 0) {
            throw new InvalidCustomerIdException();
        }
        Customer c = new CustomerClass();
        c = getCustomer(customerId);
        if (c == null) {
            return false;
        }

        c.setCustomerCard(customerCard);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
            throws InvalidCustomerCardException, UnauthorizedException {

        JSONArray CustomerList = new JSONArray();
        JSONObject C = new JSONObject();
        int oldpoints = 0;
        String CARD2 = "";
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if ( customerCard==null || customerCard.isEmpty() || customerCard.isBlank() || customerCard.length() != 10 || customerCard.matches(".*[a-z].*") || customerCard.matches(".*[A-Z].*") ) {
            throw new InvalidCustomerCardException();
        }

        JSONParser jsonParser = new JSONParser();
        File f = new File("Customers.json");

        try (FileReader reader = new FileReader(f)) { // still need to add check for first user sign in.
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                CustomerList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < CustomerList.size(); i++) {

            C = (JSONObject) CustomerList.get(i);
            CARD2 = C.get("CustomerCard").toString();
            oldpoints = Integer.parseInt(C.get("Points").toString());

            if (CARD2.equals(customerCard) && (pointsToBeAdded + oldpoints) >= 0) {

                C.replace("Points", pointsToBeAdded + oldpoints);
                CustomerList.set(i, C);

                // Write JSON file
                try (FileWriter file = new FileWriter("Customers.json")) {
                    // We can write any JSONArray or JSONObject instance to the file
                    file.write(CustomerList.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }
        }

        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////// End
    ///////////////////////////////////////////////////////////////////////////////////////////////////// Anyone
    ///////////////////////////////////////////////////////////////////////////////////////////////////// -
    ///////////////////////////////////////////////////////////////////////////////////////////////////// Start
    ///////////////////////////////////////////////////////////////////////////////////////////////////// Simone

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        SaleTransactionClass s = new SaleTransactionClass();
        JSONArray salesList = new JSONArray();
        List<TicketEntry> l = new ArrayList<>();

        Integer id = 0;

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        File f = new File("SaleTransaction.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                // Read JSON file
                Object obj = jsonParser.parse(reader);
                salesList = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        sales = salesList;

        id = sales.size() + 1; // get the next ID for the user.

        s.setEntries(l);
        s.setPrice(0.0);
        s.setDiscountRate(0.0);
        s.setTicketNumber(id);

        currentTransaction = s;
        return currentTransaction.getTicketNumber();
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
            UnauthorizedException {

        TicketEntry entry = new TicketEntryClass();
        List<TicketEntry> Entries = new ArrayList<>();
        boolean isfound = false;

        ProductType p = new ProductTypeClass();

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12 || productCode.isBlank()) {
            throw new InvalidProductCodeException("product code error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if (amount < 0) {
            throw new InvalidQuantityException("amount < 0");
        }

        if (!transactionId.equals(currentTransaction.getTicketNumber())) { // not the started transaction
            return false;
        }

        p = getProductByBarCode(productCode);

        if (p == null) {
            return false;
        }

        if (p.getQuantity() >= amount) {
            entry.setAmount(amount);
            entry.setDiscountRate(0); // initialy is 0
            entry.setProductDescription(p.getProductDescription());
            entry.setBarCode(productCode);
            entry.setPricePerUnit(p.getPricePerUnit());
            // UPDATE QUANTITY
            try {
                this.updateQuantityinternal(p.getId(), -amount);
            } catch (InvalidProductIdException e) {
                e.printStackTrace();
            }
            Entries = currentTransaction.getEntries();
            Entries.add(entry);
            currentTransaction.setEntries(Entries);
            isfound = true;
        }

        return isfound;

    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
            UnauthorizedException {

        TicketEntry entry = new TicketEntryClass();
        List<TicketEntry> Entries = new ArrayList<>();
        boolean isfound = false;

        ProductType p = new ProductTypeClass();

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12 || productCode.isBlank()) {
            throw new InvalidProductCodeException("product code error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if (amount < 0) {
            throw new InvalidQuantityException("amount < 0");
        }

        if (!transactionId.equals(currentTransaction.getTicketNumber())) { // not the started transaction
            return false;
        }
        // does not work whit cashier//
        p = getProductByBarCode(productCode);

        if (p == null) {
            return false;
        }

        Entries = currentTransaction.getEntries();
        for (int j = 0; j < Entries.size(); j++) {
            if (Entries.get(j).getBarCode().compareTo(productCode) == 0) {

                isfound = true;
                // not sufficient amount
                if (Entries.get(j).getAmount() - amount < 0) {
                    return false;
                }

                entry.setAmount(Entries.get(j).getAmount() - amount); // decrease amount
                entry.setDiscountRate(Entries.get(j).getDiscountRate());
                entry.setProductDescription(Entries.get(j).getProductDescription());
                entry.setBarCode(Entries.get(j).getBarCode());
                entry.setPricePerUnit(Entries.get(j).getPricePerUnit());
                Entries.set(j, entry); // substitute element
                break;
            }
        }

        // UPDATE QUANTITY
        try {
            this.updateQuantityinternal(p.getId(), amount);
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
        }

        currentTransaction.setEntries(Entries);

        return isfound;

    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
            UnauthorizedException {
        TicketEntry entry = new TicketEntryClass();
        List<TicketEntry> Entries = new ArrayList<>();
        boolean isfound = false;

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12 || productCode.isBlank()) {
            throw new InvalidProductCodeException("product code error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if (discountRate < 0.0 || discountRate > 1.0) {
            throw new InvalidDiscountRateException("discountRate invalid value");
        }

        if (!transactionId.equals(currentTransaction.getTicketNumber())) { // not the started transaction
            return false;
        }

        Entries = currentTransaction.getEntries();
        for (int j = 0; j < Entries.size(); j++) {

            if (Entries.get(j).getBarCode().compareTo(productCode) == 0) {
                isfound = true;
                entry.setAmount(Entries.get(j).getAmount());
                entry.setDiscountRate(discountRate); // set discountRate
                entry.setProductDescription(Entries.get(j).getProductDescription());
                entry.setBarCode(Entries.get(j).getBarCode());
                entry.setPricePerUnit(Entries.get(j).getPricePerUnit());
                Entries.set(j, entry); // substitute element
                break;
            }
        }

        currentTransaction.setEntries(Entries);

        return isfound;

    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
            throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if (discountRate < 0.0 || discountRate > 1.0) {
            throw new InvalidDiscountRateException("discountRate invalid value");
        }

        if (!transactionId.equals(currentTransaction.getTicketNumber())) { // not the started transaction
            return false;
        }

        currentTransaction.setDiscountRate(discountRate);

        return true;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

        double total = 0;
        int totalPoints = 0;

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        if (!transactionId.equals(currentTransaction.getTicketNumber())) { // not the started transaction
            return -1;
        }

        // set price
        for (int i = 0; i < currentTransaction.getEntries().size(); i++) {
            // total = total + amount * price per unit * (1-discount)
            total += currentTransaction.getEntries().get(i).getAmount()
                    * currentTransaction.getEntries().get(i).getPricePerUnit()
                    * (1 - currentTransaction.getEntries().get(i).getDiscountRate());
        }
        total = total * (1.0 - currentTransaction.getDiscountRate());
        currentTransaction.setPrice(total);

        // points for a sale
        totalPoints = (int) total / 10;

        return totalPoints;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean endSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        int i = 0;
        TicketEntryClass[] u;
        u = new TicketEntryClass[currentTransaction.getEntries().size()];
        double total = 0;
        JSONObject saleTData = new JSONObject();
        // JSONObject entriesData = new JSONObject();
        JSONArray EntriesJSONArray = new JSONArray();

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        if (!transactionId.equals(currentTransaction.getTicketNumber())) { // not the started transaction
            return false;
        }

        // transaction already closed
        if (this.getSaleTransaction(transactionId) != null) {
            return false;
        }

        // set price
        for (i = 0; i < currentTransaction.getEntries().size(); i++) {
            // total = total + amount * price per unit * (1-discount)
            total += currentTransaction.getEntries().get(i).getAmount()
                    * currentTransaction.getEntries().get(i).getPricePerUnit()
                    * (1 - currentTransaction.getEntries().get(i).getDiscountRate());
        }
        total = total * (1.0 - currentTransaction.getDiscountRate());
        currentTransaction.setPrice(total);

        List<TicketEntry> EntriesList = new ArrayList<>();
        EntriesList = currentTransaction.getEntries();

        for (i = 0; i < EntriesList.size(); i++) {

            u[i] = new TicketEntryClass(); // initialize objects of the class
            u[i] = (TicketEntryClass) EntriesList.get(i);
        }

        for (i = 0; i < EntriesList.size(); i++) {
            JSONObject entriesData = new JSONObject();
            entriesData.put("amount", u[i].getAmount());
            entriesData.put("barcode", u[i].getBarCode());
            entriesData.put("priceperunit", u[i].getPricePerUnit());
            entriesData.put("discountrate", u[i].getDiscountRate());
            entriesData.put("productdescription", u[i].getProductDescription());
            EntriesJSONArray.add(entriesData);
        }
        // add sale to the list
        saleTData.put("id", currentTransaction.getTicketNumber());
        saleTData.put("price", currentTransaction.getPrice());
        saleTData.put("DiscountRate", currentTransaction.getDiscountRate());
        saleTData.put("entries", EntriesJSONArray);
        saleTData.put("payed", 0); // no payed
        sales.add(saleTData);

        try (FileWriter file = new FileWriter("SaleTransaction.json")) { // update sale transaction
            // We can write any JSONArray or JSONObject instance to the file
            file.write(sales.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deleteSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {

        JSONArray salesList = new JSONArray();
        JSONObject saleTData = new JSONObject();
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        boolean isRemoved = false;

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        try (FileReader reader = new FileReader("SaleTransaction.json")) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            salesList = (JSONArray) obj;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        sales = salesList;

        for (int i = 0; i < sales.size(); i++) {
            saleTData = (JSONObject) sales.get(i);
            if (((int) (long) saleTData.get("id")) == transactionId) {
                if (((int) (long) saleTData.get("payed")) == 0) {
                    int j;
                    for (j = i; j < sales.size() - 2; j++) { // replace whit successors
                        sales.set(j, sales.get(j + 1));
                    }
                    sales.remove(j); // remove last

                    isRemoved = true;

                    // remove from JSON
                    try (FileWriter file = new FileWriter("SaleTransaction.json")) { // update sale transaction
                        // We can write any JSONArray or JSONObject instance to the file
                        file.write(sales.toJSONString());
                        file.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }

        return isRemoved;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionClass s = new SaleTransactionClass();
        JSONObject saleTData = new JSONObject();
        JSONArray salesList = new JSONArray();
        JSONArray EntriesJSONArray = new JSONArray();
        List<TicketEntry> EntriesList = new ArrayList<>();
        // TicketEntry entry = new TicketEntryClass();
        boolean isfound = false;
        int i = 0;

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        File f = new File("SaleTransaction.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                // Read JSON file
                Object obj = jsonParser.parse(reader);
                salesList = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        sales = salesList;

        for (Object sale : sales) {
            saleTData = (JSONObject) sale;
            if ((int) (long) saleTData.get("id") == transactionId) {
                EntriesJSONArray = (JSONArray) saleTData.get("entries");
                /*
                 * JSONObject[] u; u = new JSONObject[currentTransaction.getEntries().size()];
                 * for (i = 0; i < EntriesJSONArray.size(); i++) {
                 * 
                 * u[i] = new JSONObject(); // initialize objects of the class u[i] =
                 * (JSONObject) EntriesJSONArray.get(i); }
                 * 
                 * for (i = 0; i < EntriesJSONArray.size(); i++) { TicketEntry entry = new
                 * TicketEntryClass(); entry.setPricePerUnit((double) u[i].get("priceperunit"));
                 * entry.setBarCode((String) u[i].get("barcode"));
                 * entry.setProductDescription((String) u[i].get("productdescription"));
                 * entry.setAmount((int) (long) u[i].get("amount"));
                 * entry.setDiscountRate((double) u[i].get("discountrate"));
                 * EntriesList.add(entry); System.out.println("HERE" + EntriesList.get(0));
                 * 
                 * }
                 */

                // MODIFIED FROM PEVIOUS ONE UNTIL...

                for (i = 0; i < EntriesJSONArray.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo = (JSONObject) EntriesJSONArray.get(i);
                    TicketEntry entry = new TicketEntryClass();
                    entry.setPricePerUnit((double) jo.get("priceperunit"));
                    entry.setBarCode((String) jo.get("barcode"));
                    entry.setProductDescription((String) jo.get("productdescription"));
                    entry.setAmount((int) (long) jo.get("amount"));
                    entry.setDiscountRate((double) jo.get("discountrate"));
                    EntriesList.add(entry);

                }

                // ... HERE

                s.setEntries(EntriesList);
                s.setPrice((Double) saleTData.get("price"));
                s.setDiscountRate((Double) saleTData.get("DiscountRate"));
                s.setTicketNumber(transactionId);
                isfound = true;
                break;
            }
        }
        if (isfound) {
            return s;
        }
        return null;
    }

    ////////// GET A RETURN TRANSACTION/////////////////

    public SaleTransaction getReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionClass r = new SaleTransactionClass();
        JSONObject retTData = new JSONObject();
        JSONArray retList = new JSONArray();
        JSONArray EntriesJSONArray = new JSONArray();
        List<TicketEntry> EntriesList = new ArrayList<>();
        // TicketEntry entry = new TicketEntryClass();
        boolean isfound = false;

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        if (returnId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        File f = new File("ReturnTransaction.json");
        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) { // checking in the file is not empty
                // Read JSON file
                Object obj = jsonParser.parse(reader);
                retList = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        returns = retList;

        for (Object sale : returns) {
            retTData = (JSONObject) sale;
            if ((int) (long) retTData.get("id") == returnId) {
                EntriesJSONArray = (JSONArray) retTData.get("entries");
                for (int i = 0; i < EntriesJSONArray.size(); i++) {
                    TicketEntry entry = new TicketEntryClass();
                    JSONObject jo = new JSONObject();
                    jo = (JSONObject) EntriesJSONArray.get(i);
                    entry.setPricePerUnit((double) jo.get("priceperunit"));
                    entry.setBarCode((String) jo.get("barcode"));
                    entry.setProductDescription((String) jo.get("productdescription"));
                    entry.setAmount((int) (long) jo.get("amount"));
                    entry.setDiscountRate((double) jo.get("discountrate"));
                    EntriesList.add(entry);
                }
                r.setEntries(EntriesList);
                r.setPrice((Double) retTData.get("price"));
                r.setDiscountRate((Double) retTData.get("DiscountRate"));
                r.setTicketNumber(returnId);
                isfound = true;
                break;
            }
        }
        if (isfound) {
            return r;
        }
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer transactionId)
            throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionClass s = new SaleTransactionClass();
        SaleTransactionClass ret = new SaleTransactionClass(); // return transaction
        // JSONObject saleTData = new JSONObject();
        JSONArray salesList = new JSONArray();
        JSONArray retList = new JSONArray();
        boolean isfound = false;
        JSONArray EntriesJSONArray = new JSONArray();
        List<TicketEntry> EntriesList = new ArrayList<>();
        int id = 0;

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        if (transactionId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        File f1 = new File("SaleTransaction.json");
        try (FileReader reader = new FileReader(f1)) {
            // Read JSON file
            if (f1.length() != 0) {
                Object obj = jsonParser.parse(reader);
                salesList = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        sales = salesList;

        for (Object sale : sales) {
            JSONObject saleTData = new JSONObject();
            saleTData = (JSONObject) sale;
            // if found and payed
            if ((int) (long) saleTData.get("id") == transactionId && (int) (long) saleTData.get("payed") == 1) {
                EntriesJSONArray = (JSONArray) saleTData.get("entries");
                for (int i = 0; i < EntriesJSONArray.size(); i++) {
                    TicketEntry entry = new TicketEntryClass();
                    JSONObject jo = new JSONObject();
                    jo = (JSONObject) EntriesJSONArray.get(i);
                    entry.setPricePerUnit((double) jo.get("priceperunit"));
                    entry.setBarCode((String) jo.get("barcode"));
                    entry.setProductDescription((String) jo.get("productdescription"));
                    entry.setAmount((int) (long) jo.get("amount"));
                    entry.setDiscountRate((double) jo.get("discountrate"));
                    EntriesList.add(entry);
                }
                s.setEntries(EntriesList);
                s.setPrice((Double) saleTData.get("price"));
                s.setDiscountRate((Double) saleTData.get("DiscountRate"));
                s.setTicketNumber(transactionId);
                currentSaleTransaction = s;
                isfound = true;
                break;
            }
        }

        if (!isfound)
            return -1;

        File f2 = new File("ReturnTransaction.json");

        try (FileReader reader = new FileReader(f2)) {
            // Read JSON file
            if (f2.length() != 0) {
                Object obj = jsonParser.parse(reader);
                retList = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        returns = retList;

        id = returns.size() + 1; // get the next ID for the user.

        List<TicketEntry> l = new ArrayList<>();

        ret.setEntries(l);
        ret.setPrice(0.0);
        ret.setDiscountRate(currentSaleTransaction.getDiscountRate());
        ret.setTicketNumber(id);

        currentReturn = ret;

        return currentReturn.getTicketNumber();
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
            InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

        TicketEntry entry = new TicketEntryClass();
        List<TicketEntry> Entries = new ArrayList<>();
        List<TicketEntry> retEntries = new ArrayList<>();
        boolean isfound = false;

        if (returnId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (productCode.isEmpty() || productCode.length() > 14 || productCode.length() < 12 || productCode.isBlank()) {
            throw new InvalidProductCodeException("product code error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        if (amount <= 0) {
            throw new InvalidQuantityException("amount <= 0");
        }

        if (!returnId.equals(currentReturn.getTicketNumber())) { // not the started return transaction
            return false;
        }

        Entries = currentSaleTransaction.getEntries(); // current transaction to check

        // product doesn't exist
        if (getProductByBarCode(productCode) == null)
            return false;

        for (TicketEntry e : Entries) {
            if (e.getBarCode().compareTo(productCode) == 0) {
                entry = e;
                isfound = true;
                break;
            }
        }

        // not found in sale
        if (!isfound)
            return false;

        // amount greater than solded one
        if (entry.getAmount() < amount)
            return false;

        retEntries = currentReturn.getEntries();

        Entries = currentReturn.getEntries();

        TicketEntry retEntry = new TicketEntryClass();

        retEntry.setDiscountRate(entry.getDiscountRate());
        retEntry.setAmount(amount);
        retEntry.setProductDescription(entry.getProductDescription());
        retEntry.setBarCode(productCode);
        retEntry.setPricePerUnit(entry.getPricePerUnit());

        retEntries.add(retEntry);

        currentReturn.setEntries(retEntries);

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean endReturnTransaction(Integer returnId, boolean commit)
            throws InvalidTransactionIdException, UnauthorizedException {

        double total = 0;
        JSONObject retTData = new JSONObject();
        JSONObject saleTData = new JSONObject();
        JSONArray EntriesJSONArray = new JSONArray();
        JSONArray saleEntriesJSONArray = new JSONArray();
        if (returnId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }

        if (!returnId.equals(currentReturn.getTicketNumber())) { // not the started transaction
            return false;
        }

        // return without update
        if (!commit)
            return true;

        // set price
        for (int i = 0; i < currentReturn.getEntries().size(); i++) {
            // total = total + amount * price per unit * (1-discount)
            total += currentReturn.getEntries().get(i).getAmount() * currentReturn.getEntries().get(i).getPricePerUnit()
                    * (1 - currentReturn.getEntries().get(i).getDiscountRate());

        }
        total = total * (1.0 - currentReturn.getDiscountRate());

        currentReturn.setPrice(total);

        List<TicketEntry> EntriesList = new ArrayList<>();
        EntriesList = currentReturn.getEntries();
        int i = 0;
        for (TicketEntry ticketEntry : EntriesList) {
            JSONObject entriesData = new JSONObject();
            entriesData.put("amount", ticketEntry.getAmount());
            entriesData.put("barcode", ticketEntry.getBarCode());
            entriesData.put("priceperunit", ticketEntry.getPricePerUnit());
            entriesData.put("discountrate", ticketEntry.getDiscountRate());
            entriesData.put("productdescription", ticketEntry.getProductDescription());
            EntriesJSONArray.add(entriesData);
        }
        // add return to the list
        retTData.put("id", currentReturn.getTicketNumber());
        retTData.put("price", currentReturn.getPrice());
        retTData.put("DiscountRate", currentReturn.getDiscountRate());
        retTData.put("entries", EntriesJSONArray);
        retTData.put("payed", 0); // no payed

        retTData.put("transactionId", currentSaleTransaction.getTicketNumber()); // save the number of original sale
        // transaction

        returns.add(retTData);

        try (FileWriter file = new FileWriter("ReturnTransaction.json")) { // update sale transaction
            // We can write any JSONArray or JSONObject instance to the file
            file.write(returns.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        /////// UPDATE SaleTransaction.json///////

        List<TicketEntry> saleEntriesList = currentSaleTransaction.getEntries();
        for (TicketEntry returnEntry : EntriesList) {
            i = 0;
            for (TicketEntry saleEntry : saleEntriesList) {
                if (returnEntry.getBarCode().compareTo(saleEntry.getBarCode()) == 0) {
                    saleEntry.setAmount(saleEntry.getAmount() - returnEntry.getAmount());
                    saleEntriesList.set(i, saleEntry);
                    currentSaleTransaction.setPrice(currentSaleTransaction.getPrice()
                            - returnEntry.getAmount() * returnEntry.getPricePerUnit());
                    try {
                        updateQuantityinternal(getProductByBarCode(returnEntry.getBarCode()).getId(),
                                returnEntry.getAmount()); // update quantity in inventory
                    } catch (InvalidProductIdException | InvalidProductCodeException e) {
                        e.printStackTrace();
                    }
                }
                i++;

            }
        }

        for (int j = 0; j < saleEntriesList.size(); j++) {
            TicketEntry ticketEntry = new TicketEntryClass();
            ticketEntry = saleEntriesList.get(j);
            JSONObject entriesData = new JSONObject();
            entriesData.put("amount", ticketEntry.getAmount());
            entriesData.put("barcode", ticketEntry.getBarCode());
            entriesData.put("priceperunit", ticketEntry.getPricePerUnit());
            entriesData.put("discountrate", ticketEntry.getDiscountRate());
            entriesData.put("productdescription", ticketEntry.getProductDescription());
            saleEntriesJSONArray.add(entriesData);
        }
        // add sale to the list
        saleTData.put("id", currentSaleTransaction.getTicketNumber());
        saleTData.put("price", currentSaleTransaction.getPrice());
        saleTData.put("DiscountRate", currentSaleTransaction.getDiscountRate());
        saleTData.put("entries", saleEntriesJSONArray);
        saleTData.put("payed", 1);
        sales.set(currentSaleTransaction.getTicketNumber() - 1, saleTData);

        try (FileWriter file = new FileWriter("SaleTransaction.json")) { // update sale transaction
            // We can write any JSONArray or JSONObject instance to the file
            file.write(sales.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deleteReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {

        JSONArray retList = new JSONArray();
        JSONObject retTData = new JSONObject();
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        boolean isRemoved = false;

        if (returnId <= 0) {
            throw new InvalidTransactionIdException("id error");
        }
        if (user.getRole().compareToIgnoreCase("administrator") != 0
                && user.getRole().compareToIgnoreCase("shopmanager") != 0
                && user.getRole().compareToIgnoreCase("Cashier") != 0) {
            throw new UnauthorizedException("unauthorized");
        }
        try (FileReader reader = new FileReader("ReturnTransaction.json")) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            retList = (JSONArray) obj;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        returns = retList;

        for (int i = 0; i < returns.size(); i++) {
            retTData = (JSONObject) returns.get(i);
            if (((int) (long) retTData.get("id")) == returnId) {
                if (((int) (long) retTData.get("payed")) == 0) {
                    int j;
                    for (j = i; j < returns.size() - 2; j++) { // replace whit successors
                        returns.set(j, returns.get(j + 1));
                    }
                    returns.remove(j); // remove last

                    isRemoved = true;

                    // remove from JSON
                    try (FileWriter file = new FileWriter("ReturnTransaction.json")) { // update sale transaction
                        // We can write any JSONArray or JSONObject instance to the file
                        file.write(returns.toJSONString());
                        file.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }

        return isRemoved;
    }

    @Override
    @SuppressWarnings("unchecked")
    public double receiveCashPayment(Integer transactionId, double cash)
            throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

        double change = 0;
        JSONObject saleTData = new JSONObject();
        SaleTransaction s = new SaleTransactionClass();
        s = getSaleTransaction(transactionId); // includes also InvalidTransactionIdException ans UnauthorizedException
        if (cash < 0) {
            throw new InvalidPaymentException();
        }

        if (s == null) { // not found
            return -1;
        }

        if (s.getPrice() > cash) { // not enough cash
            return -1;
        }
        change = cash - s.getPrice();

        // Set as payed
        saleTData = (JSONObject) sales.get(transactionId - 1);
        saleTData.replace("payed", 1);
        sales.set(transactionId - 1, saleTData);
        // update JSON
        try (FileWriter file = new FileWriter("SaleTransaction.json")) { // update sale transaction
            // We can write any JSONArray or JSONObject instance to the file
            file.write(sales.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // UPDATE BALANCE.JSON

        BalanceType = "SALE";

        recordBalanceUpdateinternal(s.getPrice());

        return change;
    }

    public boolean creditCardCheck(String number) {

        int[] ints = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            ints[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int anInt : ints) {
            sum += anInt;
        }

        return sum % 10 != 0; // is invalid?

    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean receiveCreditCardPayment(Integer transactionId, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

        double creditCardAmount;
        String[] creditCardStr;
        SaleTransaction s = new SaleTransactionClass();
        JSONObject saleTData = new JSONObject();
        boolean isFound = false;

        if (creditCardCheck(creditCard)) { // card not valid
            throw new InvalidCreditCardException();
        }
        s = getSaleTransaction(transactionId); // manage also exception

        if (s == null)
            return false;

        File f = new File("CreditCard.txt");
        try (FileReader reader = new FileReader(f)) {

            BufferedReader filebuf = new BufferedReader(reader);
            String nextStr;
            nextStr = filebuf.readLine(); // read a line
            while (nextStr != null) {
                if (!nextStr.startsWith("#")) {
                    creditCardStr = nextStr.split(";");
                    if (creditCard.compareTo(creditCardStr[0]) == 0) {
                        creditCardAmount = Double.parseDouble(creditCardStr[1]);
                        creditCardAmount = creditCardAmount - s.getPrice();
                        isFound = true;
                        if (creditCardAmount < 0) {
                            return false;
                        }

                        break;
                    }
                }
                nextStr = filebuf.readLine(); // read next line
            }
            filebuf.close(); // close file

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!isFound) {
            return false;
        }

        // Set as payed
        saleTData = (JSONObject) sales.get(transactionId - 1);
        saleTData.replace("payed", 1);
        sales.set(transactionId - 1, saleTData);
        // update JSON
        try (FileWriter file = new FileWriter("SaleTransaction.json")) { // update sale transaction
            // We can write any JSONArray or JSONObject instance to the file
            file.write(sales.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // UPDATE BALANCE.JSON
        BalanceType = "SALE";

        recordBalanceUpdateinternal(s.getPrice());

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

        double retValue = 0;
        JSONObject retTData = new JSONObject();
        SaleTransaction r = new SaleTransactionClass(); // return
        r = getReturnTransaction(returnId); // includes also InvalidTransactionIdException ans UnauthorizedException

        // r CONTAINS ONLY RETURNS FROM A CLOSED RETURN TRANSACTION
        if (r == null) { // not found
            return -1;
        }

        // Set as payed
        retTData = (JSONObject) returns.get(returnId - 1);
        retTData.replace("payed", 1);
        returns.set(returnId - 1, retTData);
        // update JSON
        try (FileWriter file = new FileWriter("ReturnTransaction.json")) { // update sale transaction
            // We can write any JSONArray or JSONObject instance to the file
            file.write(returns.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        retValue = r.getPrice();
        // UPDATE BALANCE.JSON
        BalanceType = "RETURN";

        recordBalanceUpdateinternal(-r.getPrice());

        return retValue;

    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

        double creditCardAmount;
        String[] creditCardStr;
        SaleTransaction r = new SaleTransactionClass(); // return

        if (creditCardCheck(creditCard)) { // card not valid
            throw new InvalidCreditCardException();
        }

        r = getReturnTransaction(returnId); // includes also InvalidTransactionIdException ans UnauthorizedException
        // r CONTAINS ONLY RETURNS FROM A CLOSED RETURN TRANSACTION
        if (r == null) { // not found
            return -1;
        }

        File f = new File("CreditCard.txt");
        try (FileReader reader = new FileReader(f)) {
            BufferedReader filebuf = new BufferedReader(reader);
            String nextStr;
            nextStr = filebuf.readLine(); // read a line
            while (nextStr != null) {
                if (!nextStr.startsWith("#")) {
                    creditCardStr = nextStr.split(";");
                    if (creditCard.compareTo(creditCardStr[0]) == 0) {
                        creditCardAmount = Double.parseDouble(creditCardStr[1]);
                        creditCardAmount = creditCardAmount + r.getPrice();

                        break;
                    }
                }
                nextStr = filebuf.readLine(); // read next line
            }
            filebuf.close(); // close file

        } catch (IOException e) {
            e.printStackTrace();
        }

        // UPDATE BALANCE.JSON
        BalanceType = "SALE";

        recordBalanceUpdateinternal(-r.getPrice());

        return r.getPrice();
    }

    /////////////////////////////////////////////////////////////////////////// End
    /////////////////////////////////////////////////////////////////////////// Simone
    /////////////////////////////////////////////////////////////////////////// -
    /////////////////////////////////////////////////////////////////////////// Start
    /////////////////////////////////////////////////////////////////////////// YASSER
    @Override
    @SuppressWarnings("unchecked")
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager", operationtype = "CREDIT";
        double CurrentBalance = 0;
        JSONObject BalanceData = new JSONObject();
        Integer id = 0;
        JSONArray BalanceList = new JSONArray(); // create a list of jason objects to store them in a file
        LocalDate date = LocalDate.now();

        if (user.getRole() == null || user.getRole().isEmpty() || user.getRole().isBlank()
                || !(role.equals(user.getRole()) || role_2.equals(user.getRole()))) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Balance.json");

        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                BalanceList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        id = BalanceList.size() + 1; // get the next ID for the user.

        BalanceData.put("id", id);
        BalanceData.put("date", date.toString()); // when defining customer we don't know the Customercard so it
        // is empty for now
        BalanceData.put("money", toBeAdded);

        if (toBeAdded < 0 && BalanceType.compareTo("RETURN") == 0) {
            operationtype = "RETURN";
            BalanceData.put("type", operationtype);
        } else if (toBeAdded > 0 && BalanceType.compareTo("SALE") == 0) {
            operationtype = "SALE";
            BalanceData.put("type", operationtype);
        }

        else if (toBeAdded < 0 && BalanceType.compareTo("ORDER") == 0) {
            operationtype = "ORDER";
            BalanceData.put("type", operationtype);
        }

        else if (toBeAdded > 0 && BalanceType.compareTo("DEFAULT") == 0) {
            operationtype = "CREDIT";
            BalanceData.put("type", operationtype);
        }

        else if (toBeAdded < 0 && BalanceType.compareTo("DEFAULT") == 0) {
            operationtype = "DEBIT";
            BalanceData.put("type", operationtype);
        }

        BalanceType = "DEFAULT";

        BalanceList.add(BalanceData); // add a certain object to the list

        CurrentBalance = computeBalance();

        if ((CurrentBalance + toBeAdded) < 0) {
            return false;
        }

        // Write JSON file
        try (FileWriter file = new FileWriter("Balance.json")) {
            // We can write any JSONArray or JSONObject instance to the file
            file.write(BalanceList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean recordBalanceUpdateinternal(double toBeAdded) { // CASHIER METHOD

        String operationtype = "CREDIT";
        double CurrentBalance = 0;
        JSONObject BalanceData = new JSONObject();
        Integer id = 0;
        JSONArray BalanceList = new JSONArray(); // create a list of jason objects to store them in a file
        LocalDate date = LocalDate.now();

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Balance.json");

        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                BalanceList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        id = BalanceList.size() + 1; // get the next ID for the user.

        BalanceData.put("id", id);
        BalanceData.put("date", date.toString()); // when defining customer we don't know the Customercard so it
        // is empty for now
        BalanceData.put("money", toBeAdded);

        if (toBeAdded < 0 && BalanceType.compareTo("RETURN") == 0) {
            operationtype = "RETURN";
            BalanceData.put("type", operationtype);
        } else if (toBeAdded > 0 && BalanceType.compareTo("SALE") == 0) {
            operationtype = "SALE";
            BalanceData.put("type", operationtype);
        }

        else if (toBeAdded < 0 && BalanceType.compareTo("ORDER") == 0) {
            operationtype = "ORDER";
            BalanceData.put("type", operationtype);
        }

        else if (toBeAdded > 0 && BalanceType.compareTo("DEFAULT") == 0) {
            operationtype = "CREDIT";
            BalanceData.put("type", operationtype);
        }

        else if (toBeAdded < 0 && BalanceType.compareTo("DEFAULT") == 0) {
            operationtype = "DEBIT";
            BalanceData.put("type", operationtype);
        }

        BalanceType = "DEFAULT";

        BalanceList.add(BalanceData); // add a certain object to the list

        CurrentBalance = computeBalanceinternal();

        if ((CurrentBalance + toBeAdded) < 0) {
            return false;
        }

        // Write JSON file
        try (FileWriter file = new FileWriter("Balance.json")) {
            // We can write any JSONArray or JSONObject instance to the file
            file.write(BalanceList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

        BalanceOperationClass[] b;
        JSONArray BalanceList = new JSONArray();

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Balance.json");

        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                BalanceList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        b = new BalanceOperationClass[BalanceList.size()];
        List<BalanceOperation> Balanceslist = new ArrayList<BalanceOperation>();
        String role = "Administrator", role_1 = "ShopManager";
        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();
        long idlong = 0;
        String type;
        Integer id = 0;
        LocalDate date, from1 = from, to1 = to;
        double money;

        if (user.getRole() == null || user.getRole().isEmpty() || user.getRole().isBlank()
                || !(role.equals(user.getRole()) || role_1.equals(user.getRole()))) {
            throw new UnauthorizedException("Invalid Role");
        }

        for (int i = 0; i < BalanceList.size(); i++) {

            b[i] = new BalanceOperationClass(); // initialize objects of the class
        }

        if (from == null) {
            from1 = LocalDate.MIN;
        }

        if (to == null) {
            to1 = LocalDate.MAX;
        }

        for (int i = 0; i < BalanceList.size(); i++) {

            DataAtIndex = BalanceList.get(i);
            Data = (JSONObject) DataAtIndex;

            idlong = (long) Data.get("id"); // get the id
            id = (int) idlong;
            date = LocalDate.parse((String) Data.get("date"));

            if ((date.isAfter(from1) && date.isBefore(to1)) || date.isEqual(from1) || date.isEqual(to1)) {

                money = (double) Data.get("money");
                type = (String) Data.get("type");
                b[i].setBalanceId(id);
                b[i].setDate(date);
                b[i].setMoney(money);
                b[i].setType(type);

                Balanceslist.add(b[i]);
            }
        }

        return Balanceslist;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {

        String role = "Administrator", role_2 = "ShopManager", role_3 = "Cashier";
        double CurrentBalance = 0;

        int i = 0;
        JSONArray BalanceList = new JSONArray(); // create a list of jason objects to store them in a file

        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();

        if (user.getRole() == null || user.getRole().isEmpty() || user.getRole().isBlank()
                || !(role.equals(user.getRole()) || role_2.equals(user.getRole()) || role_3.equals(user.getRole()))) {
            throw new UnauthorizedException("Invalid Role");
        }

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Balance.json");

        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                BalanceList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (i = 0; i < BalanceList.size(); i++) {

            DataAtIndex = BalanceList.get(i);
            Data = (JSONObject) DataAtIndex;

            CurrentBalance = CurrentBalance + (double) Data.get("money");

        }

        return CurrentBalance;
    }

    public double computeBalanceinternal() { // CASHIER METHOD

        double CurrentBalance = 0;

        int i = 0;
        JSONArray BalanceList = new JSONArray(); // create a list of jason objects to store them in a file

        Object DataAtIndex = new Object();
        JSONObject Data = new JSONObject();

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        File f = new File("Balance.json");

        try (FileReader reader = new FileReader(f)) {
            // Read JSON file
            if (!(f.length() == 0)) {
                Object obj = jsonParser.parse(reader);
                BalanceList = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        for (i = 0; i < BalanceList.size(); i++) {

            DataAtIndex = BalanceList.get(i);
            Data = (JSONObject) DataAtIndex;

            CurrentBalance = CurrentBalance + (double) Data.get("money");

        }

        return CurrentBalance;
    }
}
