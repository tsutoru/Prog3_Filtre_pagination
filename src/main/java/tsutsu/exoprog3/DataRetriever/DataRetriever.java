package tsutsu.exoprog3.DataRetriever;


import java.time.Instant;

import tsutsu.exoprog3.model.Category;
import tsutsu.exoprog3.model.Product;
import tsutsu.exoprog3.DBconnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public List<Category> getAllCategories() throws SQLException{
        String sql="SELECT id,name FROM product_category ORDER BY id";
        List<Category> out=new ArrayList<>();
        try(Connection c= DBConnection.getDBConnection();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                out.add(new Category(rs.getInt("id"),rs.getString("name")));
            }
        }
        return out;
    }

    public List<Product> getProductList(int page,int size) throws SQLException{
        String sql="SELECT p.id pid,p.name pname,p.price,p.creation_datetime,"+
                "c.id cid,c.name cname "+
                "FROM product p LEFT JOIN product_category c ON p.category_id=c.id "+
                "ORDER BY p.id LIMIT ? OFFSET ?";
        page=Math.max(1,page); size=Math.max(1,size);
        int offset=(page-1)*size;
        List<Product> out=new ArrayList<>();

        try(Connection c=DBConnection.getDBConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,size);
            ps.setInt(2,offset);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    Category cat=null;
                    int cid=rs.getInt("cid");
                    if(!rs.wasNull()) cat=new Category(cid,rs.getString("cname"));
                    Timestamp ts=rs.getTimestamp("creation_datetime");
                    Instant inst=ts!=null?ts.toInstant():null;
                    out.add(new Product(
                            rs.getInt("pid"),
                            rs.getString("pname"),
                            rs.getDouble("price"),
                            inst,
                            cat));
                }
            }
        }
        return out;
    }

    public List<Product> getProductsByCriteria(String productName,String categoryName,
                                               Instant creationMin,Instant creationMax,
                                               int page,int size) throws SQLException{
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT p.id pid,p.name pname,p.price,p.creation_datetime,")
                .append("c.id cid,c.name cname FROM product p ")
                .append("LEFT JOIN product_category c ON p.category_id=c.id ");

        List<Object> params=new ArrayList<>();
        boolean where=false;

        if(productName!=null && !productName.isBlank()){
            sb.append(where?" AND ":" WHERE ").append("p.name ILIKE ?");
            params.add("%"+productName+"%");
            where=true;
        }
        if(categoryName!=null && !categoryName.isBlank()){
            sb.append(where?" AND ":" WHERE ").append("c.name ILIKE ?");
            params.add("%"+categoryName+"%");
            where=true;
        }
        if(creationMin!=null){
            sb.append(where?" AND ":" WHERE ").append("p.creation_datetime>=?");
            params.add(Timestamp.from(creationMin));
            where=true;
        }
        if(creationMax!=null){
            sb.append(where?" AND ":" WHERE ").append("p.creation_datetime<=?");
            params.add(Timestamp.from(creationMax));
            where=true;
        }

        sb.append(" ORDER BY p.id");

        boolean paginate=page>0 && size>0;
        if(paginate) sb.append(" LIMIT ? OFFSET ?");

        List<Product> out=new ArrayList<>();
        try(Connection c=DBConnection.getDBConnection();
            PreparedStatement ps=c.prepareStatement(sb.toString())){
            int idx=1;
            for(Object o:params){
                if(o instanceof String) ps.setString(idx++,(String)o);
                else if(o instanceof Timestamp) ps.setTimestamp(idx++,(Timestamp)o);
                else ps.setObject(idx++,o);
            }
            if(paginate){
                page=Math.max(1,page); size=Math.max(1,size);
                int offset=(page-1)*size;
                ps.setInt(idx++,size);
                ps.setInt(idx++,offset);
            }
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    Category cat=null;
                    int cid=rs.getInt("cid");
                    if(!rs.wasNull()) cat=new Category(cid,rs.getString("cname"));
                    Timestamp ts=rs.getTimestamp("creation_datetime");
                    Instant inst=ts!=null?ts.toInstant():null;
                    out.add(new Product(
                            rs.getInt("pid"),rs.getString("pname"),
                            rs.getDouble("price"),inst,cat));
                }
            }
        }
        return out;
    }
}

