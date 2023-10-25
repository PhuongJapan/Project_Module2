package Imp;

import color.Color;
import entity.Category;
import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static entity.Product.checkProductNameIsExist;

public class ProductManagement implements Serializable {
    public static Scanner scanner = new Scanner(System.in);
    public static List<Category> categoryList = new ArrayList<>();
    public static final String border = "-";
    private static List<Product> productList = new ArrayList<>();
    private static int indexUpdate;

    public static void displayMenu(Scanner scanner, List<Product> productList) {
        boolean isExit = true;
        do {
            System.out.println("===== QUẢN LÝ SẢN PHẨM =====");
            System.out.println("1.Thêm mới sản phẩm");
            System.out.println("2.Cập nhật sản phẩm");
            System.out.println("3.Xóa sản phẩm");
            System.out.println("4.Hiển thị sản phẩm theo tên A-Z");
            System.out.println("5.Hiển thị sản phẩm theo lợi nhuận từ cao-thấp");
            System.out.println("6.Tìm kiếm sản phẩm");
            System.out.println("7.Hiển thị danh sách sản phẩm");
            System.out.println("8.Quay lại");
            System.out.println("Lựa chọn của bạn:");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    ProductManagement.inputProductList();
                    break;
                case 2:
                    ProductManagement.updateProductById();
                    break;
                case 3:
                    ProductManagement.deleteProductById();
                    break;
                case 4:
                    ProductManagement.displayProductByNameASC();
                    break;
                case 5:
                    ProductManagement.arrangePrdByProfitDESC();
                    break;
                case 6:
                    ProductManagement.searchPrdByNameOrExPrOrImPr();
                    break;
                case 7:
                    ProductManagement.displayProductList();
                    break;
                case 8:
                    ProductManagement.writeProductListToFile(productList);
                    return;
                default:
                    System.err.println("Vui lòng chọn từ 1-8 ");
            }
        } while (isExit);
    }

    //6.Tìm kiếm sản phẩm theo tên hoặc giá nhập/giá bán từ từ khóa tìm kiếm
    public static void searchPrdByNameOrExPrOrImPr() {
        System.out.println("Nhập vào tên sản phẩm hoặc giá nhập, giá bán của SP bạn muốn tìm:");
        do {
            String wordsToSearch = scanner.nextLine();
            if (wordsToSearch.isEmpty()) {
                System.err.println("Thông tin tìm kiếm không được để trống, vui lòng nhập lại");

            } else {
                List<Product> productListFilter = productList.stream().filter(product ->
                        product.getProductName().toLowerCase().contains(wordsToSearch.toLowerCase()) ||
                                String.valueOf(product.getImportPrice()).contains(wordsToSearch) ||
                                String.valueOf(product.getExportPrice()).contains(wordsToSearch)).collect(Collectors.toList());
                if (productListFilter.isEmpty()) {
                    System.out.println(Color.BLUE_BOLD + "Không tìm thấy kết quả" + Color.ANSI_RESET);
                } else {
                    String repeated = new String(new char[157]).replace("\0", border);
                    System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
                    System.out.printf("| %-14s | %-14s | %-14s | %-14s | %-14s | %-20s | %-21s | %-25s |\n", "Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Lợi nhuận", "Mô tả sản phẩm", "Trạng thái sản phẩm", "Mã danh mục sản phẩm");
                    System.out.println("* ============================================================================================================================================================= *");
                    productListFilter.forEach(Product::displayData);
                    System.out.println("* " + repeated + " *");
                    System.out.println(Color.ANSI_RESET);
                }
                break;
            }

        } while (true);
    }

    public static boolean updateProductById() {
        System.out.println("Nhập mã sản phẩm bạn muốn cập nhật");
        String productIdToUpdate = scanner.nextLine();
        if (productIdToUpdate.isEmpty()) {
            System.err.println("Mã sản phẩm không được để trống, vui lòng nhập lại");
        } else {
            int indexUpdate = getIndexProduct(productIdToUpdate);
            String inputPrdNameUpdate = null;
            if (indexUpdate < 0) {
                System.err.println("Không tìm thấy mã sản phẩm");
            } else {
                //Cập nhật tên
                System.out.println("Nhập tên sản phẩm mới:");
                inputPrdNameUpdate = scanner.nextLine();
                if (inputPrdNameUpdate.isEmpty()) {
                    System.err.println("Tên sản phẩm không được để trống, vui lòng nhập lại");
                } else {
                    try {
                        if (inputPrdNameUpdate.length() >= 4 && inputPrdNameUpdate.length() <= 30) {
                            boolean checkName = checkProductNameIsExist(inputPrdNameUpdate);
                            if (checkName) {
                                System.err.println("Tên sản phẩm đã tồn tại, vui lòng nhập lại");
                            } else {

                            }
                        } else {
                            System.err.println("Tên sản phẩm có độ dài 4-30 ký tự, vui lòng nhập lại");
                        }
                    } catch (Exception ex) {
                        System.err.println("Có lỗi hệ");
                    }
                }

            }
            productList.get(indexUpdate).setProductName(inputPrdNameUpdate);
        }
        //Cập nhật giá nhập
//        System.out.println("Nhập giá nhập mới (giá trị lớn hơn 0):");
//        String inputImportPriceUpdate;
//        inputImportPriceUpdate = scanner.nextLine().trim();
//        if (inputImportPriceUpdate.isEmpty()) {
//            System.err.println("Giá nhập không được để trống, vui lòng nhập lại");
//        } else {
//            try {
//                double importPrice = Double.parseDouble(inputImportPriceUpdate);
//                if (importPrice > 0) {
//                    return importPrice;
//                } else {
//                    System.err.println("Giá nhập sản phẩm phải là số thực lớn hơn 0, vui lòng nhập lại");
//                }
//            } catch (Exception ex) {
//                System.err.println("Có lỗi hệ thống");
//            }
//        }
//        productList.get(indexUpdate).setProductDescription(inputImportPriceUpdate);

        //Cập nhật mô tả sản pẩm
        System.out.println("Nhập vào mô tả mới cần cập nhật:");
        String inputDescriptionUpdate = scanner.nextLine();
        if (inputDescriptionUpdate.isEmpty()) {
            System.err.println("Trạng thái sản phẩm không được để trống, vui lòng nhập lại");
        } else {
            productList.get(indexUpdate).setProductDescription(inputDescriptionUpdate);
        }

        //Cập nhật trạng thái sản phẩm
        System.out.println("Nhập trạng thái sản phẩm mới cần cập nhật:");
        String inputStatusUpdate = scanner.nextLine();
        if (inputStatusUpdate.isEmpty()) {
            System.err.println("Trạng thái danh mục không được để trống, vui lòng nhập lại");
        } else {
            productList.get(indexUpdate).setProductStatus(Boolean.parseBoolean(inputStatusUpdate));
        }
        //Ghi File
        writeProductListToFile(productList);
        System.out.println("Đã cập nhật được thông tin cho danh mục có mã: " + productIdToUpdate);
        return true;
    }


    //case 5: sắp xếp theo lợi nhuận giảm dần
    public static void arrangePrdByProfitDESC() {
        System.out.println(Color.PURPLE_BOLD + "Sản phẩm được sắp xếp theo lợi nhuận giảm dần là: " + Color.ANSI_RESET);
        List<Product> producProfitDESC = productList;
        producProfitDESC.sort(Comparator.comparing(Product::getProfit).reversed());
        String repeated = new String(new char[157]).replace("\0", border);
        System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
        System.out.printf("| %-14s | %-14s | %-14s | %-14s | %-14s | %-20s | %-21s | %-25s |\n", "Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Lợi nhuận", "Mô tả sản phẩm", "Trạng thái sản phẩm", "Mã danh mục sản phẩm");
        System.out.println("* ============================================================================================================================================================= *");
        producProfitDESC.forEach(Product::displayData);
        System.out.println("* " + repeated + " *" + Color.ANSI_RESET);
    }

    //case 4: sắp xếp theo tên tăng dần A-Z
    public static void displayProductByNameASC() {
        System.out.println(Color.PURPLE_BOLD + "Sản phẩm được sắp xếp theo tên từ A-Z là: " + Color.ANSI_RESET);
        List<Product> productNameASC = productList;
        productNameASC.sort(Comparator.comparing(Product::getProductName));
        String repeated = new String(new char[157]).replace("\0", border);
        System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
        System.out.printf("| %-14s | %-14s | %-14s | %-14s | %-14s | %-20s | %-21s | %-25s |\n", "Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Lợi nhuận", "Mô tả sản phẩm", "Trạng thái sản phẩm", "Mã danh mục sản phẩm");
        System.out.println("* ============================================================================================================================================================= *");
        productNameASC.forEach(Product::displayData);
        System.out.println("* " + repeated + " *");
        System.out.println(Color.ANSI_RESET);
    }

    //Case 3: Xóa sản phẩm theo ID
    public static void deleteProductById() {
        System.out.println("Nhập mã sản phẩm bạn cần xóa:");
        String productIdToDelete = scanner.nextLine();
        if (productIdToDelete.isEmpty()) {
            System.err.println("Mã sản phẩm không được để trống, vui lòng nhập lại");
        } else {
            int indexDelete = getIndexProduct(productIdToDelete);
            if (indexDelete < 0) {
                System.err.printf("Không tìm thấy mã sản phẩm: " + productIdToDelete);
            } else {
                productList.remove(indexDelete);
                writeProductListToFile(productList);
                System.out.println(Color.PURPLE_BOLD + "Đã xóa xong sản phẩm có mã: " + productIdToDelete);
            }
        }

    }

    //case 7: Hiển thị danh sách sản phẩm
    public static void displayProductList() {
        String repeated = new String(new char[157]).replace("\0", border);
        System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
        System.out.printf("| %-14s | %-14s | %-14s | %-14s | %-14s | %-20s | %-21s | %-25s |\n", "Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Lợi nhuận", "Mô tả sản phẩm", "Trạng thái sản phẩm", "Tên danh mục sản phẩm");
        System.out.println("* ============================================================================================================================================================= *");
        for (Product prd : productList) {
            prd.displayData();
        }
        System.out.println("* " + repeated + " *" + Color.ANSI_RESET);
    }

    //case 1: Thêm mới sản phẩm
    private static void inputProductList() {
        System.out.println("Nhập vào số lượng sản phẩm cần nhập dữ liệu");
        do {
            int n = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < n; i++) {
                Product prd = new Product();
                prd.inputData();
                productList.add(prd);
                productList.forEach(Product::calProfit);
                writeProductListToFile(productList);
            }

            break;
        } while (true);
    }

    public static List<Product> readProductListFromFile() {
//        List<Category> categoryList = null;
        //Khởi tạo đối tượng file
        File file = new File("products.txt");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            //Khởi tạo FileInputStream
            fis = new FileInputStream(file);
            //Khởi tạo ObjectInputStream
            ois = new ObjectInputStream(fis);
            //Đọc dữ liệu từ file
            productList = (List<Product>) ois.readObject();
        } catch (Exception ex) {
            System.err.println("Có lỗi hệ thống");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception ex) {
                System.err.println("Có lỗi hệ thống");
            }
        }
        return productList;
    }

    //Ghi ra file
    public static void writeProductListToFile(List<Product> productList) {
        File file = new File("products.txt");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(productList);
            oos.flush();
        } catch (Exception ex) {
            System.err.println("Có lỗi hệ thống ");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception ex) {
                System.err.println("Có lỗi hệ thống ");
            }
        }
    }

    public static int getIndexProduct(String productId) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getProductId().equalsIgnoreCase(productId)) {
                return i;
            }
        }
        return -1;
    }

}
