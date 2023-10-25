package entity;

import Interface.ICategory;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

import static Imp.CategoryManagement.writeCategoryDataToFile;
import static run.StoreManagement.*;
//Import tất cả các phương thức của Management

import static sun.java2d.cmm.ColorTransform.In;

public class Category implements ICategory, Serializable {
    private int categoryId;
    private String categoryName;
    private String categoryDescription;
    private Boolean categoryStatus;

    public Category() {

    }

    public Category(int categoryId, String categoryName, String categoryDescription, Boolean categoryStatus) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryStatus = categoryStatus;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Boolean getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(Boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    @Override
    public void inputData() {
        this.categoryId = validateCategoryId();
        this.categoryName = validateCategoryName();
        this.categoryDescription = validateCategoryDescription();
        this.categoryStatus = validateCategoryStatus();
    }
    public Boolean validateCategoryStatus() {
        System.out.println("Nhập trạng thái danh mục:");
        do {
            String inputCategorySttStr = scanner.nextLine().trim();
            if(inputCategorySttStr.isEmpty()){
                System.err.println("Trạng thái danh mục không được để trống, vui lòng nhập lại");
            }else {
                try{
                    if (inputCategorySttStr.equalsIgnoreCase("true")||inputCategorySttStr.equalsIgnoreCase("false")){
                        return Boolean.parseBoolean(inputCategorySttStr);
                    }else {
                        System.err.println("Trạng thái chỉ nhập true hoặc false, vui lòng nhập lại");
                    }
                }catch (Exception ex){
                    System.err.println("Có lỗi hệ thống");
                }
            }
        }while (true);
    }
    public static String validateCategoryDescription() {
        System.out.println("Nhập mô tả danh mục:");
        do{
            String inputCategoryDctStr = scanner.nextLine().trim();
            if(inputCategoryDctStr.isEmpty()){
                System.err.println("Trạng thái danh mục không được để trống, vui lòng nhập lại");
            }else {
                try {
                    return inputCategoryDctStr;
                }catch (Exception ex){
                    System.err.println("Có lỗi hệ thống");
                }
            }
        }while (true);
    }

//
    public static String validateCategoryName() {
        System.out.println("Nhập tên danh mục:");
        do{
            String inputCategoryNameStr = scanner.nextLine().trim();
            if (inputCategoryNameStr.isEmpty()){
                System.err.println("Tên danh mục không được để trống, vui lòng nhập lại");
            }else {
                try {
                    if (inputCategoryNameStr.length() >=6 && inputCategoryNameStr.length() <=30 ) {
                        boolean checkName = checkCategoryNameIsExist(inputCategoryNameStr);
                        if (checkName) {
                            System.err.println("Tên danh mục đã tồn tại, vui lòng nhập lại");
                        } else {
                            return inputCategoryNameStr;
                        }
                    } else {
                        System.err.println("Tên danh mục có độ dài 6-30 ký tự, vui lòng nhập lại");
                    }
                } catch (Exception ex) {
                    System.err.println("Có lỗi hệ thống");
                }
            }

        }while(true);
    }

    public static boolean checkCategoryNameIsExist(String inputCategoryNameStr) {
        for (Category ctg:categoryList) {
            if (ctg.getCategoryName().equals(inputCategoryNameStr)){
                return true;
            }
        }
        return false;
    }
    public static int validateCategoryId() {
        System.out.println("Nhập mã danh mục:");
        while (true){
            String inputCategoryIdStr = scanner.nextLine().trim();
            if (inputCategoryIdStr.isEmpty()){
                System.err.println("Mã danh mục không được để trống, vui lòng nhập lại");
            }else{
                try{
                    int categoryId = Integer.parseInt(inputCategoryIdStr);
                    if(categoryId>0){
                        boolean checkId = checkCategoryIdIsExist(categoryId);
                        if(checkId){
                            System.err.println("Mã danh mục đã tồn tại, vui lòng nhập lại");
                        }else {
                            return categoryId;
                        }
                    }else {
                        System.err.println("Mã danh mục phải là số lớn hơn 0, vui lòng nhập lại");
                    }
                }catch (NumberFormatException ex){
                    System.err.println("Mã danh mục phải là số, vui lòng nhập lại");
                }
            }

        }
    }

    public static boolean checkCategoryIdIsExist(int categoryIdCheck) {
        for (Category ctg:categoryList) {
            if (ctg.getCategoryId()==categoryIdCheck){
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public void displayData() {
        System.out.printf("| %-15s | %-20s | %-20s | %-20s|\n", this.categoryId, this.categoryName, this.categoryDescription, this.categoryStatus ? "Hoạt động" : "Không hoạt động");
    }
}
