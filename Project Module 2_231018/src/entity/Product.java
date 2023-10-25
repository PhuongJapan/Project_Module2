package entity;

import Interface.IProduct;

import java.io.Serializable;

import static run.StoreManagement.*;

public class Product implements IProduct, Serializable {
    private String productId;
    private String productName;
    private double importPrice;
    private double exportPrice;
    private double profit;
    private String productDescription;
    private boolean productStatus;
    private int categoryId;

    public Product() {

    }

    public Product(String productId, String productName, double importPrice, double exportPrice, double profit, String productDescription, boolean productStatus, int categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.profit = profit;
        this.productDescription = productDescription;
        this.productStatus = productStatus;
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) {
        this.exportPrice = exportPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public boolean isProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void inputData() {
        this.productId = validateProductId();
        this.productName = validateProductName();
        this.importPrice = validateImportPrice();
        this.exportPrice = validateExportPrice();
        this.productDescription = validateProductDescription();
        this.productStatus = validateProductStatus();
        this.categoryId = validateCategoryIdForPrd();
    }

    public boolean validateProductStatus() {
        System.out.println("Nhập trạng thái sản phẩm:");
        do {
            String inputProductSttStr = scanner.nextLine().trim();
            if (inputProductSttStr.isEmpty()) {
                System.err.println("Trạng thái sản phẩm không được để trống, vui lòng nhập lại");
            } else {
                try {
                    if (inputProductSttStr.equalsIgnoreCase("true") || inputProductSttStr.equalsIgnoreCase("false")) {
                        return Boolean.parseBoolean(inputProductSttStr);
                    } else {
                        System.err.println("Trạng thái chỉ nhập true hoặc false, vui lòng nhập lại");
                    }
                } catch (Exception ex) {
                    System.err.println("Có lỗi hệ thống");
                }
            }
        } while (true);

    }

    private String validateProductDescription() {
        System.out.println("Nhập mô tả sản phẩm: ");
        do {
            String inputPrdDescStr = scanner.nextLine().trim();
            if (inputPrdDescStr.isEmpty()) {
                System.err.println("Mô tả sản phẩm không được bỏ trống, vui lòng nhập lại");
            } else {
                return inputPrdDescStr;
            }
        } while (true);
    }

    private double validateExportPrice() {
        System.out.println("Nhập giá bán của sản phẩm");
        do {
            String inputExportPriceStr = scanner.nextLine().trim();
            if (inputExportPriceStr.isEmpty()) {
                System.err.println("Giá bán không được để trống, vui lòng nhập lại");
            } else {
                try {
                    double exportPrice = Double.parseDouble(inputExportPriceStr);

                    if (exportPrice >= importPrice * (1 + MIN_INTEREST_RATE)) {
                        return exportPrice;
                    } else {
                        System.err.println("Giá bán sản phẩm phải lớn hơn 1.2 lần so với giá nhập, vui lòng nhập lại");
                    }
                } catch (Exception ex) {
                    System.err.println("Có lỗi hệ thống");
                }
            }

        } while (true);
    }

    public static double validateImportPrice() {
        System.out.println("Nhập giá nhập của sản phẩm (yêu cầu lớn hơn 0)");
        do {
            String inputImportPriceStr = scanner.nextLine().trim();
            if (inputImportPriceStr.isEmpty()) {
                System.err.println("Giá nhập không được để trống, vui lòng nhập lại");
            } else {
                try {
                    double importPrice = Double.parseDouble(inputImportPriceStr);
                    if (importPrice > 0) {
                        return importPrice;
                    } else {
                        System.err.println("Giá nhập sản phẩm phải là số thực lớn hơn 0, vui lòng nhập lại");
                    }
                } catch (Exception ex) {
                    System.err.println("Có lỗi hệ thống");
                }
            }

        } while (true);
    }

    public int validateCategoryIdForPrd() {
        System.out.println("Lựa chọn danh mục của sản phẩm");
        boolean isExit =true;
        do {
            for (int i = 0; i < categoryList.size(); i++) {
                System.out.printf("%d. %s\n", (i + 1), categoryList.get(i).getCategoryId());
            }
            System.out.print("Danh mục bạn muốn chọn:");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > categoryList.size()) {
                System.err.println("Không tồn tại danh mục, vui lòng chọn lại");
            } else {
                this.categoryId = categoryList.get(choice - 1).getCategoryId();
                break;
            }
        } while (isExit);
        return this.categoryId;
    }

    public String displayCtgNameFromCtgId(int ctgId){
        for (Category ctg:categoryList) {
            if(ctg.getCategoryId()==ctgId){
                return ctg.getCategoryName();
            }
        }
        return null;
    }

//        public static boolean checkCategoryIdIsExist ( int categoryIdCheck){
//            for (Category ctg : categoryList) {
//                if (ctg.getCategoryId() == categoryIdCheck) {
//                    return true;
//                }
//            }
//            return false;
//        }


        public static String validateProductId () {
            System.out.println("Nhập mã sản phẩm (có 4 ký tự, bắt đầu bằng P và không trùng)");
            do {
                String inputProductIdStr = scanner.nextLine().trim();
                if (inputProductIdStr.isEmpty()) {
                    System.err.println("Mã sản phẩm không được để trống, vui lòng nhập lại");
                } else {
                    try {
                        if (inputProductIdStr.length() == 4 && inputProductIdStr.startsWith("P")) {
                            boolean checkId = checkProductIdIsExist(inputProductIdStr);
                            if (checkId) {
                                System.err.println("Mã sản phẩm đã tồn tại, vui lòng nhập lại");
                            } else {
                                return inputProductIdStr;
                            }
                        } else {
                            System.err.println("Mã sản phẩm có độ dài 4 ký tự và bắt đầu bằng P, vui lòng nhập lại");
                        }
                    } catch (Exception ex) {
                        System.err.println("Có lỗi chương trình");
                    }
                }

            } while (true);
        }

        public static boolean checkProductIdIsExist (String inputProductIdStr){
            for (Category ctg : categoryList) {
                if (ctg.getCategoryName().equals(inputProductIdStr)) {
                    return true;
                }
            }
            return false;
        }

        public static String validateProductName () {
            System.out.println("Nhập tên sản phẩm (độ dài 4-30 ký tự và không trùng)");
            do {
                String inputProductNameStr = scanner.nextLine().trim();
                if (inputProductNameStr.isEmpty()) {
                    System.err.println("Tên sản phẩm không được để trống, vui lòng nhập lại");
                } else {
                    try {
                        if (inputProductNameStr.length() >= 4 && inputProductNameStr.length() <= 30) {
                            boolean checkName = checkProductNameIsExist(inputProductNameStr);
                            if (checkName) {
                                System.err.println("Tên sản phẩm đã tồn tại, vui lòng nhập lại");
                            } else {
                                return inputProductNameStr;
                            }
                        } else {
                            System.err.println("Tên sản phẩm có độ dài 4-30 ký tự, vui lòng nhập lại");
                        }
                    } catch (Exception ex) {
                        System.err.println("Có lỗi hệ thống");
                    }
                }

            } while (true);
        }

        public static boolean checkProductNameIsExist (String inputProductNameStr){
            for (Category ctg : categoryList) {
                if (ctg.getCategoryName().equals(inputProductNameStr)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void displayData () {
//        String ctgNameStr= categoryList.get(getIndexCategory(this.getCategoryId())).getCategoryName();
            System.out.printf("| %-14s | %-14s | %-14s | %-14s | %-14s | %-20s | %-21s | %-25s |\n",
                    this.productId, this.productName, this.importPrice, this.exportPrice, this.profit, this.productDescription, this.productStatus ? "Còn hàng" : "Ngừng kinh doanh", displayCtgNameFromCtgId(this.categoryId));
        }

        @Override
        public void calProfit () {
            this.profit = this.exportPrice - this.importPrice;
        }
    }
