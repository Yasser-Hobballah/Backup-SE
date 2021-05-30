package it.polito.ezshop.data;


public class ProductTypeClass implements ProductType{
    private Integer quantity;
    private String location;
    private String note;
    private String productDescription;
    private String barCode;
    private Double pricePerUnit;
    private Integer id;

    public ProductTypeClass() {
    }

    public ProductTypeClass(Integer quantity, String location, String note, String productDescription, String barCode, Double pricePerUnit, Integer id) {
        this.quantity = quantity;
        this.location = location;
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = pricePerUnit;
        this.id = id;
    }

    @Override
    public Integer getQuantity() {
		return quantity;
	}

    @Override
    public void setQuantity(Integer quantity) {
            if (quantity > 0)
                this.quantity= quantity;
    }

    @Override
	public String getLocation() {
		return location;
	}

    @Override
	public void setLocation(String location) {
        if (!(location == null)) {
            if (!(location.isEmpty())) {
                this.location = location;
            }
        }
	}

    @Override
	public String getNote() {
		return note;
	}

    @Override
	public void setNote(String note) {
        if (!(note == null)) {
            if (!(note.isEmpty())) {
                this.note = note;
            }
        }
	}

	
    @Override
	public String getProductDescription() {
		return productDescription;
	}

    @Override
	public void setProductDescription(String productDescription) {
        if (!(productDescription == null)) {
            if (!(productDescription.isEmpty())) {
                this.productDescription = productDescription;
            }
        }
	}

    @Override
	public String getBarCode() {
		return barCode;
	}

    @Override
	public void setBarCode(String barCode) {
        if (!(barCode == null)) {
            if (!(barCode.isEmpty())) {
                this.barCode = barCode;
            }
        }
	}

    @Override
	public Double getPricePerUnit() {
		return pricePerUnit;
	}

    @Override
	public void setPricePerUnit(Double pricePerUnit) {
        if (pricePerUnit >= 0)
            this.pricePerUnit = pricePerUnit;
	}

    @Override
	public Integer getId() {
		return id;
	}

    @Override
	public void setId(Integer id) {
        if(id != null)
            if(id > 0)
                this.id = id;
	}
}
