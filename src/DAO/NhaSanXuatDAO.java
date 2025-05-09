/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author nguye
 */
import DTO.NhaSanXuatDTO;
import java.sql.*;
import DTO.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class NhaSanXuatDAO {
    public List<NhaSanXuatDTO> getALL() throws SQLException{
        List<NhaSanXuatDTO> ds=new ArrayList<>();
        String sql="SELECT * FROM nha_san_xuat";
        try(Connection conn=DBConnection.getConnection();
        PreparedStatement stmt=conn.prepareStatement(sql);
    )
        {
            ResultSet rs=stmt.executeQuery();
            while(rs.next())
            {
                ds.add(new NhaSanXuatDTO(
                        rs.getString("MaNXS"),
                        rs.getString("TenNSX"),
                        rs.getString("DiaChi"),
                        rs.getString("Sdt_NSX")   
                    
                        
                
                ));
            }
                
            }
        return ds;
        }
    
    public String getTenNXSByMaNSV(String maNSX)
    {
        String nameNXS="";
        String sql="SELECT TenNSX From nha_san_xuat WHERE MaNSX=?";
        try(Connection conn=DBConnection.getConnection();
            PreparedStatement stmt=conn.prepareStatement(sql))      
        {
            stmt.setString(1,maNSX);
            ResultSet rs= stmt.executeQuery();
            while(rs.next())
            {
                nameNXS=rs.getString("TenNSX");
            }
            
            
        }
        catch(SQLException error)
        {
            System.out.println("loi truy van "+error.getMessage());
        }
        return nameNXS;
    }
       
 
        
    }
    
    
    
    

