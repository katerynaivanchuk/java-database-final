public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(Long productId, Long storeId) {
        super("Inventory for product " + productId + "and store " + storeId + " is not found");
    }
}