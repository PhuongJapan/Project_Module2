package Imp;

import Interface.IProduct;
import color.Color;
import entity.Category;
import entity.Product;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


import static run.StoreManagement.categoryList;
import static run.StoreManagement.scanner;

public class CategoryManagement implements Serializable {
    public static Scanner scanner = new Scanner(System.in);
    public static List<Category> categoryList = new ArrayList<>();
    public static final String border = "-";
    private static final List<Product> productList = new ArrayList<>();

    public static void displayMenu(Scanner scanner, List<Category> categoryList) {
        boolean isExit = true;
        do {
            System.out.println("===== QUẢN LÝ DANH MỤC =====");
            System.out.println("1.Thêm mới danh mục");
            System.out.println("2.Cập nhật danh mục");
            System.out.println("3.Xóa danh mục");
            System.out.println("4.Tìm kiếm danh mục theo tên danh mục");
            System.out.println("5.Thống kê số lượng sp đang có trong danh mục");
            System.out.println("6.Hiển thị danh mục");
            System.out.println("7.Quay lại");
            System.out.println("Lựa chọn của bạn:");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    CategoryManagement.inputCategoryList();
                    break;
                case 2:
                    CategoryManagement.updateCategoryById();
                    break;
                case 3:
                    CategoryManagement.deleteCategoryById(productList);
                    break;
                case 4:
                    CategoryManagement.searchCategoryByName();
                    break;
                case 5:
                    CategoryManagement.statisticsProductOfCategory(categoryList, productList);
                    break;
                case 6:
                    CategoryManagement.displayCategoryList();
                    break;
                case 7:
                    CategoryManagement.writeCategoryDataToFile(categoryList);
                    return;
                default:
                    System.err.println("Vui lòng chọn từ 1-7 ");
            }
        } while (isExit);
    }
    //Thống kê số lượng sản phẩm có trong danh mục
    public static void statisticsProductOfCategory(List<Category>categoryList, List<Product>productList) {
        System.out.println(Color.PURPLE_BOLD + "Thống kê số lượng sản phẩm theo danh sách danh mục sản phẩm như sau:");
        String repeated = new String(new char[43]).replace("\0", border);
        System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
        System.out.printf("| %-20s | %-20s |\n", "Tên danh mục", "Số lượng sản phẩm");
        System.out.println("* ============================================*");
//        Map<String, Integer> mapStatsCatalog = new HashMap<>();

        for (int i = 0; i<categoryList.size();i++) {
            int cnt = 0;
            for (Product prd : productList) {
                if(prd.getCategoryId() == categoryList.get(i).getCategoryId()){
                    cnt++;
                }
            }
//            mapStatsCatalog.put(String.valueOf(ctg.getCategoryId()),cnt);
            System.out.printf(Color.GREEN_BOLD + "| %-20s | %-20s |\n",categoryList.get(i).getCategoryName(), cnt);
            System.out.println(Color.GREEN_BOLD + "* " + repeated + " *" + Color.ANSI_RESET);
        }
    }

    public static void searchCategoryByName() {
        System.out.println("Nhập tên danh mục bạn muốn tìm kiếm:");
        do{
            String inputNameSearch = scanner.nextLine();
            if(inputNameSearch.isEmpty()){
                System.err.println("Tên danh mục không được để trống, vui lòng nhập lại");
            }else {
                List<Category> categoryList1 = categoryList.stream().filter(category ->
                        category.getCategoryName().toLowerCase().contains(inputNameSearch.toLowerCase())).collect(Collectors.toList());
                if (categoryList1.isEmpty()){
                    System.err.println("Không tìm được tên danh mục bạn đang tìm");
                }else {
                    System.out.println(Color.PURPLE_BOLD +"Danh mục bạn đang tìm kiếm là:");
                    String repeated = new String(new char[83]).replace("\0", border);
                    System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
                    System.out.printf("| %-15s | %-20s | %-20s | %-20s|\n", "Mã danh mục", "Tên danh mục", "Mô tả danh mục", "Trạng thái");
                    System.out.println("* =================================================================================== *");
                    categoryList1.forEach(Category::displayData);
                    System.out.println("* " + repeated + " *" + Color.ANSI_RESET);
                }
                break;
            }
        }while (true);
    }
    private static void deleteCategoryById(List<Product>productList) {
        System.out.println("Nhập mã danh mục bạn cần xóa:");
        int catagoryId = Integer.parseInt(scanner.nextLine());
        int indexDelete = getIndexCategory(catagoryId);
        if(indexDelete>=0){
            //Kiểm tra có chứa sản phẩm hay không, nếu không thì xóa
            boolean isInclude = false;
            for (Product prd:productList) {
                if(prd.getCategoryId()==catagoryId){
                    isInclude = true;
                }
            }
            if (isInclude){
                System.err.println("Danh mục có chứa sản phẩm, không thể xóa");
            }else {
                //Xóa danh mục
                categoryList.remove(indexDelete);
                writeCategoryDataToFile(categoryList);
                System.out.println(Color.PURPLE_BOLD+ "Đã xóa xong danh mục có mã: "+catagoryId+Color.ANSI_RESET);
            }
        }else {
            System.err.println("Mã danh mục bạn vừa nhập không tồn tại");
        }
    }

    public static boolean updateCategoryById() {
        System.out.println("Nhập mã danh mục bạn muốn cập nhật");
        int categoryIdToUpdate = Integer.parseInt(scanner.nextLine());
        //Lấy chỉ số phần tử cập nhật
        int indexUpdate = getIndexCategory(categoryIdToUpdate);
        //Cập nhật tên danh mục
        if (indexUpdate >= 0) {
            System.out.println("Nhập tên danh mục mới:");
            String inputCtgNameUpdate = scanner.nextLine();
            if (inputCtgNameUpdate.isEmpty()) {
                System.err.println("Tên danh mục không được để trống, vui lòng nhập lại");
            } else {
                if (inputCtgNameUpdate.length() >= 6 && inputCtgNameUpdate.length() <= 30) {
                    boolean isExist = false;
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (i != indexUpdate && categoryList.get(i).getCategoryName().equalsIgnoreCase(inputCtgNameUpdate)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        System.err.println("Tên danh mục đã tồn tại");
                        return false;
                    }
                    categoryList.get(indexUpdate).setCategoryName(inputCtgNameUpdate);
                }
                //Cập nhật mô tả danh mục
                System.out.println("Nhập vào mô tả mới cần cập nhật:");
                String inputDescriptionUpdate = scanner.nextLine();
                if (inputDescriptionUpdate.isEmpty()) {
                    System.err.println("Trạng thái danh mục không được để trống, vui lòng nhập lại");
                } else {
                    categoryList.get(indexUpdate).setCategoryDescription(inputDescriptionUpdate);
                }
                //Cập nhật trạng thái danh mục
                System.out.println("Nhập trạng thái danh mục mới cần cập nhật:");
                String inputStatusUpdate = scanner.nextLine();
                if (inputStatusUpdate.isEmpty()) {
                    System.err.println("Trạng thái danh mục không được để trống, vui lòng nhập lại");
                } else {
                    categoryList.get(indexUpdate).setCategoryStatus(Boolean.parseBoolean(inputStatusUpdate));
                }
            }
            writeCategoryDataToFile(categoryList);
            System.out.println("Đã cập nhật được thông tin cho danh mục có mã: " + categoryIdToUpdate);
        } else {
            System.err.println("Không tồn tại mã danh mục bạn vừa nhập");
            return false;
        }
        return true;
    }

    public static int getIndexCategory(int catagoryId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getCategoryId() == catagoryId) {
                return i;
            }
        }
        return -1;
    }


    public static List<Category> readCategoryDataFromFile() {
        //1. Khởi tạo đối tượng File
        File file = new File("categories.txt");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            //2. Khởi tạo đối tượng FileInputStream
            fis = new FileInputStream(file);
            //3. Khởi tạo đối tượng ObjectInputStream
            ois = new ObjectInputStream(fis);
            //4. Đọc dữ liệu object từ file (readObject())
            categoryList = (List<Category>) ois.readObject();
        } catch (FileNotFoundException ex1) {
            System.err.println("Không tồn tại file");
        } catch (IOException ex2) {
            System.err.println("Lỗi khi đọc file");
//            System.out.println(ex2.getMessage());
        } catch (Exception ex) {
            System.err.println("Có lỗi trong quá trình đọc dữ liệu từ file");
        } finally {
            //6. Đóng các stream
            closeStream(fis);
            closeStream(ois);
        }
        return categoryList;
    }

    private static void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Lỗi hệ thống");
            }
        }
    }

    private static void closeStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Lỗi hệ thống");
            }
        }
    }

    public static void writeCategoryDataToFile(List<Category> categoryList) {
        //1. Khởi tạo đối tượng file để làm việc với file - tương đối
        File file = new File("categories.txt");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            //2. Khởi tạo đối tượng FileOutputStream từ file - Checked Excetion
            fos = new FileOutputStream(file);
            //3. Khởi tạo đối tượng ObjectOutputStream từ fos
            oos = new ObjectOutputStream(fos);
            //4. Sử dụng writeObject để ghi object ra file
            oos.writeObject(categoryList);
            //5. Đẩy dữ liệu từ Stream xuống file
            oos.flush();

        } catch (FileNotFoundException ex1) {
            System.err.println("File không tồn tại");
        } catch (IOException ex2) {
            System.err.println("Lỗi khi ghi dữ liệu ra file");
        } catch (Exception ex) {
            System.err.println("Xảy ra lỗi trong quá trình ghi dữ liệu ra file");
        } finally {
            //6. Đóng các stream
            closeStream(fos);
            closeStream(oos);
        }
    }

    public static void inputCategoryList() {
        System.out.println("Nhập vào số lượng danh mục cần nhập dữ liệu");
        do {
            int n = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < n; i++) {
                Category ctg = new Category();
                ctg.inputData();
                categoryList.add(ctg);
                writeCategoryDataToFile(categoryList);
            }
            break;
        } while (true);
    }

    public static void displayCategoryList() {
        String repeated = new String(new char[83]).replace("\0", border);
        System.out.println(Color.GREEN_BOLD + "* " + repeated + " *");
        System.out.printf("| %-15s | %-20s | %-20s | %-20s|\n", "Mã danh mục", "Tên danh mục", "Mô tả danh mục", "Trạng thái");
        System.out.println("* =================================================================================== *");
        for (Category ctg : categoryList) {
            ctg.displayData();
        }
        System.out.println("* " + repeated + " *" + Color.ANSI_RESET);

    }
}
