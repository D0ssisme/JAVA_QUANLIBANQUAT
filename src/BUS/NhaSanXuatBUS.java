/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

/**
 *
 * @author nguye
 */
import DAO.NhaSanXuatDAO;

public class NhaSanXuatBUS {
    private final NhaSanXuatDAO nsxdao=new NhaSanXuatDAO();
    
    public String getTenNXSByMaNSX(String mansx)
    {
        return nsxdao.getTenNXSByMaNSV(mansx);
    }
    
}
