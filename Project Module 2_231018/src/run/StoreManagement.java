package run;

import Imp.CategoryManagement;
import Imp.ProductManagement;
import entity.Category;
import entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StoreManagement {
    public static Scanner scanner = new Scanner(System.in);
    public static List<Category> categoryList = new ArrayList<>();
    public static List<Product> productList = new ArrayList<>();

    public static void main(String[] args) {
        categoryList = CategoryManagement.readCategoryDataFromFile();
        productList = ProductManagement.readProductListFromFile();
        do {
            System.out.println("===== QUẢN LÝ KHO =====");
            System.out.println("1.Quản lý danh mục");
            System.out.println("2.Quản lý sản phẩm");
            System.out.println("3.Thoát");
            System.out.println("Lựa chọn của bạn là: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    CategoryManagement.displayMenu(scanner, categoryList);
                    break;
                case 2:
                    ProductManagement.displayMenu(scanner,productList);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.err.println("Vui lòng chỉ nhập 1-3");
            }
        } while (true);
    }


}

