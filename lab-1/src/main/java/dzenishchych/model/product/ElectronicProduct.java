package dzenishchych.model.product;

public final class ElectronicProduct extends Product {
    public ElectronicProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}
