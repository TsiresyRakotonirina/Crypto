package com.example.crypto.utils;

import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generalisation {
    public static <T> Vector<T> recherche(Connection connection, String[] mots, Class<T> clazz) throws Exception {
        StringBuilder conditionBuilder = new StringBuilder();
        ResultSetMetaData metaData = null;
        boolean shouldCloseConnection = false;
        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }

            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM " + clazz.getSimpleName() ;
                ResultSet result = statement.executeQuery(query);
                metaData = result.getMetaData();
            }
            conditionBuilder.append("WHERE (");

            for (int j = 0; j < mots.length; j++) {
                if(j!=0) {
                    conditionBuilder.append(" UNION ");
                    conditionBuilder.append("SELECT * FROM ");
                    conditionBuilder.append(clazz.getSimpleName());
                    conditionBuilder.append(" WHERE ");
                    conditionBuilder.append("( ");

                }
                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    conditionBuilder.append(metaData.getColumnName(i));
                    conditionBuilder.append(" LIKE '%");
                    conditionBuilder.append(mots[j]);
                    conditionBuilder.append("%'");
                    if(i<metaData.getColumnCount()) {
                        conditionBuilder.append(" OR ");
                    }
                }
                conditionBuilder.append(" )");

            }

            String condition = conditionBuilder.toString();
            return select(connection, condition, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldCloseConnection && connection != null) {
                connection.close();
            }
        }
        return null;
    }

    public static String generatePrimaryKey(Connection connection,String idPrefix, String sequenceName) throws Exception {
        boolean shouldCloseConnection = false;
        String query = "SELECT nextval(?)";
        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, sequenceName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int nextVal = resultSet.getInt(1);
                        return String.format("%s%03d", idPrefix, nextVal);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if (shouldCloseConnection && connection != null) { connection.close(); }
        }
       return null;
    }

    public static void importCSV(Connection connection,MultipartFile file, Class<?> clazz, int[] columnIndices) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] record;
            boolean isFirstLine = true;
            boolean shouldCloseConnection = false;
            try {
                if (connection == null || connection.isClosed()) {
                    shouldCloseConnection = true;
                    connection = Connexion.connect();
                }
                while ((record = reader.readNext()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    Object obj = clazz.newInstance();
                    for (int i = 0; i < columnIndices.length; i++) {
                        Field[] fields = clazz.getDeclaredFields();
                        Field field = fields[i];
                        field.setAccessible(true);
                        Method setterMethod = clazz.getMethod("set" + upper(field.getName()), field.getType());
                        setterMethod.invoke(obj, record[columnIndices[i]]);
                    }
                    insert(connection, obj);
                }
            }finally {
                if (shouldCloseConnection && connection != null) {
                    connection.close();
                }
            }

        }
    }

    public static void importExcel(Connection connection, MultipartFile file, Class<?> clazz, int[] columnIndices, int sheetIndex) throws Exception {
        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            boolean shouldCloseConnection = false;
            try {
                if (connection == null || connection.isClosed()) {
                    shouldCloseConnection = true;
                    connection = Connexion.connect();
                }
                for (Row row : sheet) {
                    if (row.getRowNum() == 0 || row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                        continue;
                    }

                    Object obj = clazz.newInstance();
                    for (int i = 0; i < columnIndices.length; i++) {
                        Field[] fields = clazz.getDeclaredFields();
                        Field field = fields[i];
                        field.setAccessible(true);
                        Method setterMethod = clazz.getMethod("set" + upper(field.getName()), field.getType());

                        Cell cell = row.getCell(columnIndices[i]);
                        Object cellValue = getCellValueAsString(cell);
                        setterMethod.invoke(obj, cellValue);
                    }
                    insert(connection, obj);
                }
            } finally {
                if (shouldCloseConnection && connection != null) {
                    connection.close();
                }
            }
        }
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
//        System.out.println("iii "+cell.getCellType());
        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    public static void insert(Connection connection, Object obj) throws Exception {
        boolean shouldCloseConnection = false;
        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }
            Statement state = connection.createStatement();
            String nomDeTable = obj.getClass().getSimpleName();
            String inserer = "INSERT INTO " + nomDeTable + " VALUES(" + concatenation(obj) + ")";
            System.out.println(inserer);
            state.executeUpdate(inserer);
            state.close();
        } finally {
            if (shouldCloseConnection && connection != null) {
                connection.close();
            }
        }
    }
    


    public static void update(Connection connection,Object o,String condition) throws Exception{
        boolean shouldCloseConnection = false;
        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }
            Statement state=connection.createStatement();
            String nomDeTable = o.getClass().getSimpleName();
            // String updating="UPDATE emp SET EMPNO='6590', ENAME='KANTO', JOB='HOTESSE_H', MGR='7839', HIREDATE='01-01-01', SAL='32', COMM='0', DEPTNO='10' WHERE ENAME='KANTOoo'";
            //          String updating="UPDATE "+nomDeTable+" SET "+ av_hupdate(o)+ " WHERE "+o.getClass().getDeclaredFields()[0].getName()+"='"+condition+"'";
            String updating="UPDATE "+nomDeTable+" SET "+ av_hupdate(o)+ " WHERE "+condition;
            System.out.println("UPDATE VITAA"+updating);
            state.executeUpdate(updating);
            state.close();
        }
        finally{
            if (shouldCloseConnection && connection != null) {
                connection.close();
                System.out.println("mihidy e");
            }
        }
    }
    
    public static <T> Vector<T> select(Connection connection, String condition, Class<T> clazz) throws Exception {
        boolean shouldCloseConnection = false;
        Vector<T> objects = new Vector<>();

        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }

            String selectQuery = "SELECT * FROM " + clazz.getSimpleName() + " " + condition;
            System.out.println(selectQuery); 

            try (Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(selectQuery)) {

                while (result.next()) {
                    T objet = clazz.newInstance();
                    ResultSetMetaData metaData = result.getMetaData();
                    int count = metaData.getColumnCount();
                    for (int i = 1; i <= count; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        String attributeName = columnName.toLowerCase();
                        String attributeNameUpper = upper(attributeName);
                        Class<?>[] params = {clazz.getDeclaredField(attributeName).getType()};
                        // System.out.println("attributeName: "+attributeName);
                        Method setterMethod = clazz.getMethod("set" + attributeNameUpper, params);
                        Object columnValue = result.getObject(i);
                        if (columnValue != null) {
                            if (params[0] == int.class) {
                                columnValue = result.getInt(i);
                            } else if (params[0] == double.class) {
                                columnValue = result.getDouble(i);
                            } else if (params[0] == String.class) {
                                columnValue = result.getString(i);
                            } else if (params[0] == Date.class) {
                                columnValue = result.getDate(i);
                            } else if (params[0] == Timestamp.class) {
                                columnValue = result.getTimestamp(i);
                            }
                            setterMethod.invoke(objet, columnValue);
                        }
                    }
                    objects.add(objet);
                }
            }
        } finally {
            if (shouldCloseConnection && connection != null) {
                connection.close();
            }
        }
        return objects;
    }


   
    public static void delete(Connection connection, String table, String condition) throws Exception {
        boolean shouldCloseConnection = false;
        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }
            String deleteQuery = "DELETE FROM " + table + " WHERE " + condition;
            System.out.println(deleteQuery); 
    
            try (Statement statement = connection.createStatement()) {
                int rowsAffected = statement.executeUpdate(deleteQuery);
                System.out.println(rowsAffected + " row(s) deleted.");
            }
        } finally {
            if (shouldCloseConnection && connection != null) {
                connection.close();
            }
        }
    }
    

     public void update_with_id(Connection connection,Object o,String condition) throws Exception{
        boolean shouldCloseConnection = false;
        try {
            if (connection == null || connection.isClosed()) {
                shouldCloseConnection = true;
                connection = Connexion.connect();
            }
            String nomDeTable = o.getClass().getName();
            Statement state;
            state = connection.createStatement();
            // String updating="UPDATE emp SET EMPNO='6590', ENAME='KANTO', JOB='HOTESSE_H', MGR='7839', HIREDATE='01-01-01', SAL='32', COMM='0', DEPTNO='10' WHERE ENAME='KANTOoo'";
            String updating="UPDATE "+nomDeTable+" SET "+ av_hupdate(o)+ " WHERE "+o.getClass().getDeclaredFields()[0].getName()+"='"+condition+"'";
            System.out.println("UPDATE VITAAAAA"+updating);
            state.executeUpdate(updating);
            state.close();
        }  finally {
            if (shouldCloseConnection && connection != null) {
                connection.close();
            }
        }
    }
     
    public static Field[] getAnnotatedFields(Object obj) throws Exception{
        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields.toArray(new Field[0]);
    }

    public static String [] change(Object o)throws Exception{
        String get="get";
        Field [] attribut=getAnnotatedFields(o);
        String [] tab=new String[attribut.length];
        String [] izy=new String[attribut.length];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        for (int i = 0; i < attribut.length; i++) {
            attribut[i].setAccessible(true); // Pour accéder aux champs privés
            if (attribut[i].isAnnotationPresent(Column.class)) {
                Object value = attribut[i].get(o);
                if (value == null) {
                    izy[i] = "NULL";
                }else if (attribut[i].getType() == LocalDateTime.class) {
                    // Si c'est un LocalDateTime, formater la date
                    LocalDateTime localDateTime = (LocalDateTime) value;
                    String formattedDate = localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                    izy[i] = "'" + formattedDate + "'"; // Formater le LocalDateTime
                }
                else if (attribut[i].getType() == String.class || attribut[i].getType() == Timestamp.class) {
                    izy[i] = "'" + value.toString() + "'";
                } else if (attribut[i].getType() == Date.class) {
                    izy[i] = "to_date('" + value.toString() + "','yyyy-mm-dd')";
                }  else if (attribut[i].getType() == Time.class) {
                    izy[i] = "'" + value+ "'";
                }else {
                    izy[i] = value.toString();
                }
            }
        }
        return izy;
    }

    public static String concatenation(Object obj) throws Exception{ //ato le manambatra sisa
        String [] one=change(obj);
        String virgule=",";
        String fi=new String();
        System.out.println("urtyuikjhvcdrftgh : "+one[0]);
        for (int j = 0; j < one.length-1 ; j++) {
                fi=fi+one[j]+virgule;
            }   
            fi=fi+one[one.length-1];
        return fi;
    }


    public static String av_hupdate(Object objet) throws Exception{
        Field [] attribut=getAnnotatedFields(objet);
        String [] tab=new String[attribut.length];
        String f=new String();
        for (int i = 0; i < attribut.length-1; i++) {
                tab[i]=new String();
                tab[i]="get"+upper(attribut[i].getName());       
                f=f+attribut[i].getName()+"='"+objet.getClass().getMethod(tab[i]).invoke(objet)+"', ";
        }
        f=f+attribut[attribut.length-1].getName()+"='"+objet.getClass().getMethod("get"+upper(attribut[attribut.length-1].getName())).invoke(objet)+"'";
    System.out.println(f);
        return f;
    }


    public String concat_vector(Vector<String>condi){
        String virgule=",";
        String fi=new String();
        if(condi.size()>=2){
            for (int j = 0; j < condi.size()-1 ; j++) {   
                fi=fi+condi.get(j)+virgule;
            }   
            fi=fi+condi.get(condi.size()-1);
        }
        else fi=fi+condi.get(0);
        return fi;
    }

    public static int count(Connection c, String table, String condition) throws Exception {
        boolean isnull = false;
        int nombre = 0;
        try {
            if (c == null || c.isClosed()) {
                isnull = true;
                c =Connexion.connect();
            }
            Statement stmt = c.createStatement();
            ResultSet res = stmt.executeQuery("select count(*) from " + table + " " + condition);
            while (res.next() == true) {
                nombre = res.getInt(1);
            }
            stmt.close();
        } finally {
            if (isnull == true) { c.close(); }
        }
        return nombre;
    }

    public double sum(Connection c, String table, String colonne, String condition) throws Exception {
        boolean isnull = false;
        double somme = 0;
        if (c == null || c.isClosed()) {
            isnull = true;
            c =Connexion.connect();
        }
        String select = "select sum(" + colonne + ") from " + table + " " + condition;
        Statement stmt = c.createStatement();
        ResultSet res = stmt.executeQuery(select);
        while (res.next() == true) {
            somme = res.getDouble(1);
        }
        stmt.close();
        if (isnull == true) {
            c.close();
        }
        return somme;
    }

    // JUSTE DES OUTILS 
    public static String upper(String word){
        String lettre=String.valueOf(word.charAt(0)).toUpperCase();
        String reste=word.substring(1);
        String mot=lettre+reste;
        return mot;
    }  

    public static Date makeDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat;
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(dateString);

        if (matcher.matches()) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        }

        java.util.Date utilDate = dateFormat.parse(dateString);
        return new Date(utilDate.getTime());
    }
    
    public Timestamp stringToTimestamp(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        return timestamp;
    }

    public int maxi(Vector<Integer> max){
        int m=0;
        for (int i = 0; i < max.size(); i++) {
            if(m<=max.get(i)){
                m=max.get(i);
            }
        }
        return m;
    }

    public int somme(Vector<Integer> s){
        int a=0;
        for (int i = 0; i < s.size(); i++) {
            a=a+s.get(i);
        }
        return a;
    }

    // GENERALISATION SANS AUTRE OBJET
    // public static void updateObject(Object obj, String condition) throws Exception {
    //     Connection con = Connexion.connect();
    //     Statement state = con.createStatement();
    //     String tableName = obj.getClass().getSimpleName();
    //     String updating = "UPDATE " + tableName + " SET " + av_hupdateObject(obj) + " WHERE " + condition;
    //     System.out.println("UPDATE VITAAAAA : " + updating);
    //     state.executeUpdate(updating);
    //     con.close();
    //     state.close();
    // }
    

    // public static void insertObject(Object objet) throws Exception {
    //     Connection con = Connexion.connect();
    //     Statement state = con.createStatement();
    //     String tableName = objet.getClass().getSimpleName();
    //     String insertion = "INSERT INTO " + tableName + " (" + getColumnNames(objet) + ") VALUES (" + getColumnValues(objet) + ")";
    //     System.out.println(insertion);
    //     state.executeUpdate(insertion);
    //     con.close();
    //     state.close();
    // }

        
    // public static Vector<Object> selectObject(String condition, Object objet) throws Exception {
    //     Connection con = Connexion.connect();
    //     Statement state = con.createStatement();
    //     String tableName = objet.getClass().getSimpleName();
    //     String select = "SELECT * FROM " + tableName + " " + condition;
    //     System.out.println(select);
    //     Vector<Object> objects = new Vector<>();
    //     ResultSet result = state.executeQuery(select);
    
    //     while (result.next()) {
    //         Object instance = objet.getClass().newInstance();
    //         for (Field field : objet.getClass().getDeclaredFields()) {
    //             field.setAccessible(true);
    //             String fieldName = field.getName();
    //             if (isPrimitiveOrStringType(field.getType())) {
    //                 field.set(instance, result.getObject(fieldName));
    //             }
    //         }
    //         objects.add(instance);
    //     }
    
    //     con.close();
    //     return objects;
    // }
    

    // private static String getColumnNames(Object objet) {
    //     Field[] attributs = objet.getClass().getDeclaredFields();
    //     StringBuilder columnNames = new StringBuilder();
    //     boolean firstColumn = true;
    
    //     for (Field field : attributs) {
    //         field.setAccessible(true);
    //         String fieldName = field.getName();
    //         if (isPrimitiveOrStringType(field.getType())) {
    //             if (!firstColumn) {
    //                 columnNames.append(", ");
    //             }
    //             columnNames.append(fieldName);
    //             firstColumn = false;
    //         }
    //     }
    //     return columnNames.toString();
    // }
    
    // private static String getColumnValues(Object objet) throws Exception {
    //     Field[] attributs = objet.getClass().getDeclaredFields();
    //     StringBuilder columnValues = new StringBuilder();
    //     boolean firstColumn = true;
    
    //     for (Field field : attributs) {
    //         field.setAccessible(true);
    //         String fieldName = field.getName();
    //         if (isPrimitiveOrStringType(field.getType())) {
    //             if (!firstColumn) {
    //                 columnValues.append(", ");
    //             }
    //             columnValues.append("'").append(field.get(objet)).append("'");
    //             firstColumn = false;
    //         }
    //     }
    //     return columnValues.toString();
    // }
    
    //  public static String av_hupdateObject(Object objet) throws Exception {
    //     Field[] attributs = objet.getClass().getDeclaredFields();
    //     StringBuilder f = new StringBuilder();
    //     boolean firstColumn = true;

    //     for (int i = 0; i < attributs.length - 1; i++) {
    //         Field field = attributs[i];
    //         field.setAccessible(true); // Pour accéder à un champ privé
    //         String fieldName = field.getName();
    //         if (isPrimitiveOrStringType(field.getType())) {
    //             if (!firstColumn) {
    //                 f.append(", ");
    //             }
    //             f.append(fieldName)
    //                     .append("='")
    //                     .append(field.get(objet))
    //                     .append("'");
    //             firstColumn = false;
    //         }
    //     }

    //     // Ajouter la dernière colonne sans la virgule
    //     Field lastField = attributs[attributs.length - 1];
    //     lastField.setAccessible(true);
    //     String lastFieldName = lastField.getName();
    //     if (isPrimitiveOrStringType(lastField.getType())) {
    //         if (!firstColumn) {
    //             f.append(", ");
    //         }
    //         f.append(lastFieldName)
    //                 .append("='")
    //                 .append(lastField.get(objet))
    //                 .append("'");
    //     }

    //     System.out.println(f);
    //     return f.toString();
    // }

    // private static boolean isPrimitiveOrStringType(Class<?> type) {
    //     return type.isPrimitive() || type.equals(Integer.class) || type.equals(Double.class) || type.equals(String.class) || type.equals(Timestamp.class) || type.equals(Date.class) || type.equals(Time.class);
    // }



    // public String av_hist(Object objet) throws Exception{
    //     Object [] attribut=objet.getClass().getDeclaredFields();
    //     String [] tab=new String[attribut.length];
    //     String f=new String();
    //     for (int i = 0; i < attribut.length; i++) {
            
    //             tab[i]=new String();
    //             tab[i]="get"+objet.getClass().getDeclaredFields()[i].getName();       
    //             f=f+objet.getClass().getDeclaredFields()[i].getName()+":"+objet.getClass().getMethod(tab[i]).invoke(objet)+";";
    //     }
    //     return f;
    // }


        // public void historiser(Object Amodifier,Object ob,String table_name,Vector<String> condi)throws Exception{
    //     String a=(String)ob.getClass().getMethod("getID").invoke(ob);
    //     String b="id='"+a+"'";
    //     condi.add(b);
    //     select(table_name, condi, ob);
    //     insert("historique", Amodifier);
    // }
}
