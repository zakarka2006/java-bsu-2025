package dzenishchych.model.line;

import dzenishchych.model.product.ElectronicProduct;
import dzenishchych.model.product.Product;

public final class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
