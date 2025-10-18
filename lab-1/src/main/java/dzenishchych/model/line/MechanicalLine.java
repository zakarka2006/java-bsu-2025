package dzenishchych.model.line;

import dzenishchych.model.product.MechanicalProduct;
import dzenishchych.model.product.Product;

public final class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
