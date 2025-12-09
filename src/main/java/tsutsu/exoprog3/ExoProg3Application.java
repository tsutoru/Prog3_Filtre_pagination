package tsutsu.exoprog3;

import tsutsu.exoprog3.DBconnection.DBConnection;
import tsutsu.exoprog3.DataRetriever.DataRetriever;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.*;
import java.util.List;

public class ExoProg3Application {
    public static void main(String[] args){
        DataRetriever dr=new DataRetriever();
        try{
            System.out.println("== Categories ==");
            dr.getAllCategories().forEach(System.out::println);

            System.out.println("\n== Page1 size3 ==");
            dr.getProductList(1,3).forEach(System.out::println);

            System.out.println("\n== Filter 'Bouteille' ==");
            dr.getProductsByCriteria("Bouteille",null,null,null,-1,-1)
                    .forEach(System.out::println);

            System.out.println("\n== Filter 'Electronique' min date ==");
            Instant min=ZonedDateTime.of(2025,9,1,0,0,0,0,ZoneOffset.UTC).toInstant();
            dr.getProductsByCriteria(null,"Électronique",min,null,-1,-1)
                    .forEach(System.out::println);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

