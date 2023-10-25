package Interface;

public interface IProduct {
    //Lãi suất nhỏ nhất trên từng sản phẩm
    float MIN_INTEREST_RATE = 0.2F;
    void inputData();
    void displayData();
    void calProfit();
}