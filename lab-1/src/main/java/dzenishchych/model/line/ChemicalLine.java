package dzenishchych.model.line;

import dzenishchych.model.product.ChemicalProduct;
import dzenishchych.model.product.Product;

public final class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
