package com.yuchida.budgetManager.model;
import com.yuchida.budgetManager.BreakdownItem;
import com.yuchida.budgetManager.BudgetCategory;
import com.yuchida.budgetManager.MonthlyBudget;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class BudgetDB {
    public static final String DB_NAME = "monthlyBudget.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Dev\\MonthlyBudgetManager\\" + DB_NAME;
    //////////
    /*
    TABLE_MONTHS and TABLE_BUDGETS need to be prepared beforehand.
    MONTHS has 12 records, each ID corresponding to an element's index - 1  of Array Month<1~12>
    BUDGETS also has 12 records linked to a corresponding MONTHS record by its month ID

    Each month record has many BUDGETS records (so far 6 records: food, education, entertainment, utilities
    miscellaneous, saving), and initially, each record has its goal value and spent value set to 0
    */
    ///////////
    //Months
    private static final Month[] MONTH = {Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY,
            Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER};
    private static final String[] MONTH_STR = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    //Category
    private static final String[] CATEGORY = {"Food", "Utilities", "Education", "Entertainment", "Miscellaneous", "Saving"};


    //TABLE For BreakDown details
    public static final String TABLE_DETAILS = "details";
    public static final String COLUMN_DETAILS_ID = "_id";//***
    public static final String COLUMN_DETAILS_NAME = "name";
    public static final String COLUMN_DETAILS_AMOUNT = "amount";
    public static final String COLUMN_DETAILS_CATEGORY = "category";//category id which the details record belong

    //TABLE For BudgetCategory [Food, Entertainment, Education, Utilities, Miscellaneous, Saving, Current Spending of each category~ ]
    public static final String TABLE_BUDGETS = "budgets";
    public static final String COLUMN_BUDGETS_ID = "_id"; //int
    public static final String COLUMN_BUDGETS_MO = "month"; //***identifies which month the record belongs to
    public static final String COLUMN_BUDGETS_CATEGORY = "category";
    public static final String COLUMN_BUDGETS_GOAL = "goal";
    public static final String COLUMN_BUDGETS_SPENT = "spent";
    public static final String COLUMN_BUDGETS_DETAIL = "detail"; //link to corresponding breakdown details

    //TABLE For MonthlyBudget [Jan ~ Dec]
    public static final String TABLE_MONTHS = "months";
    public static final String COLUMN_MONTHS_ID = "_id"; //int
    public static final String COLUMN_MONTHS_MO = "month";
    public static final String COLUMN_MONTHS_BUDGET = "budget"; //foreign key, (link to COLUMN_*_MO of records of each budget table)

    //SQL statements
    //For testing purpose, always delete old tables when the program starts
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    //Create Table months
    public static final String CREATE_TABLE_MONTHS = "CREATE TABLE IF NOT EXISTS " + TABLE_MONTHS +
            " (" + COLUMN_MONTHS_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_MONTHS_MO + " TEXT);";
    //Create Table budgets
    public static final String CREATE_TABLE_BUDGETS = "CREATE TABLE IF NOT EXISTS " + TABLE_BUDGETS +
            " (" + COLUMN_BUDGETS_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_BUDGETS_CATEGORY + " TEXT, " +
            COLUMN_BUDGETS_GOAL + " INTEGER, " +
            COLUMN_BUDGETS_SPENT + " INTEGER, " +
            COLUMN_BUDGETS_MO + " INTEGER, " +
            "FOREIGN KEY( " + COLUMN_BUDGETS_MO + " ) REFERENCES " +
            TABLE_MONTHS + " ( " + COLUMN_MONTHS_ID + " ))";
    //Create Table details
    public static final String CREATE_TABLE_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_DETAILS +
            " (" + COLUMN_DETAILS_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_DETAILS_NAME + " TEXT, " + COLUMN_DETAILS_AMOUNT + " INTEGER, " +
            COLUMN_DETAILS_CATEGORY +" INTEGER," +
            " FOREIGN KEY( " + COLUMN_DETAILS_CATEGORY + " ) REFERENCES " +
            TABLE_BUDGETS + " ( " + COLUMN_BUDGETS_ID + " ))";

    //SQL INSERT statement
    public static final String INSERT_NEW_MONTH = "INSERT INTO " + TABLE_MONTHS + "( " +
            COLUMN_MONTHS_MO + " ) VALUES ( ? )";
    public static final String INSERT_NEW_BUDGET = "INSERT INTO " + TABLE_BUDGETS + "( " +
            COLUMN_BUDGETS_CATEGORY + ", " + COLUMN_BUDGETS_GOAL + ", " + COLUMN_BUDGETS_SPENT + ", " +
            COLUMN_BUDGETS_MO + ") VALUES ( ?, ?, ?, ?)";

    public static final String INSERT_NEW_DETAILS = "INSERT INTO " + TABLE_DETAILS + "( " +
            COLUMN_DETAILS_NAME + ", " + COLUMN_DETAILS_AMOUNT + ", " + COLUMN_DETAILS_CATEGORY + ") VALUES ( ?, ?, ?)";

    //SQL UPDATE statement
    public static final String UPDATE_BUDGETS = "UPDATE " + TABLE_BUDGETS + " SET " + COLUMN_BUDGETS_GOAL +
            " = ? WHERE " + COLUMN_BUDGETS_ID + " = ?";

    public static final String UPDATE_DETAILS = "UPDATE " + TABLE_DETAILS + " SET " + COLUMN_DETAILS_AMOUNT + " = ? " +
            "WHERE " + COLUMN_DETAILS_ID + " = ? ";

    //SQL SELECT statement
    public static final String SELECT_ALL_FROM_BUDGETS = " SELECT * FROM " + TABLE_BUDGETS ;

    public static final String SELECT_DETAILS_BY_CATEGORY = "SELECT * FROM " + TABLE_DETAILS + " WHERE " +
            COLUMN_DETAILS_CATEGORY + " = ? ";
    //get Total amount (float) of details that belong to a certain category
    public static final String SELECT_DETAILS_TOTAL_BY_CATEGORY = "SELECT TOTAL ( " + COLUMN_DETAILS_AMOUNT + " ) FROM " + TABLE_DETAILS +
                                " WHERE " + COLUMN_DETAILS_CATEGORY +" = ?";


    public static final String SELECT_BUDGETS_BY_MONTH = "SELECT * FROM " + TABLE_BUDGETS + " WHERE " + COLUMN_BUDGETS_MO + " = ? ";

    //SQL DELETE statement
    public static final String DELETE_ITEM_FROM_DETAILS = "DELETE FROM " + TABLE_DETAILS + " WHERE " + COLUMN_DETAILS_ID +  " = ?";

    //SQL VIEW statement
    //create view that joins months, budgets, and details tables
    public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS monthlyBudgets AS " + SELECT_ALL_FROM_BUDGETS + " INNER JOIN " +
                TABLE_DETAILS + " ON " + TABLE_DETAILS + "." + COLUMN_DETAILS_CATEGORY + " = " + TABLE_BUDGETS + "." + COLUMN_BUDGETS_ID;

    private Connection conn;
    private PreparedStatement dropTableIfExists;
    private PreparedStatement createTableMonths;
    private PreparedStatement createTableBudgets;
    private PreparedStatement createTableDetails;
    private PreparedStatement insertIntoBudgets;
    private PreparedStatement insertIntoMonths;
    private PreparedStatement insertIntoDetails;
    private PreparedStatement selectAllFromBudgets;
    private PreparedStatement selectDetailsByCategory;
    private PreparedStatement selectTotalFromBudgets;
    private PreparedStatement selectBudgetsByMO;
    private PreparedStatement updateDetails;
    private PreparedStatement updateBudgets;
    private PreparedStatement createView;
    private PreparedStatement deleteItemFromDetails;



    private static BudgetDB instance = new BudgetDB();

    private BudgetDB(){}

    public static BudgetDB getInstance(){
        return instance;
    }

    public boolean open(){
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("Established the connection to DB successfully");
            //prepare SQL statement
            insertIntoMonths = conn.prepareStatement(INSERT_NEW_MONTH, Statement.RETURN_GENERATED_KEYS);
            insertIntoBudgets = conn.prepareStatement(INSERT_NEW_BUDGET, Statement.RETURN_GENERATED_KEYS);
            insertIntoDetails = conn.prepareStatement(INSERT_NEW_DETAILS,Statement.RETURN_GENERATED_KEYS);
            selectAllFromBudgets = conn.prepareStatement(SELECT_ALL_FROM_BUDGETS);
            selectTotalFromBudgets = conn.prepareStatement(SELECT_DETAILS_TOTAL_BY_CATEGORY);
            selectBudgetsByMO = conn.prepareStatement(SELECT_BUDGETS_BY_MONTH);
            createView = conn.prepareStatement(CREATE_VIEW);
            selectDetailsByCategory = conn.prepareStatement(SELECT_DETAILS_BY_CATEGORY);
            updateDetails = conn.prepareStatement(UPDATE_DETAILS);
            updateBudgets = conn.prepareStatement(UPDATE_BUDGETS);
            deleteItemFromDetails = conn.prepareStatement(DELETE_ITEM_FROM_DETAILS);

            return true;
        } catch (SQLException e){
            System.out.println("Error: couldn't open the database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try{
            //close the connection to DB
            if(conn != null){
                conn.close();
                System.out.println("DB is closed");
            }
            //close each SQL statement if it is active
            if(insertIntoMonths != null){
                insertIntoMonths.close();
            }
            if(insertIntoBudgets != null){
                insertIntoBudgets.close();
            }
            if(insertIntoDetails != null){
                insertIntoDetails.close();
            }
            if(selectAllFromBudgets != null){
                selectAllFromBudgets.close();
            }
            if(createView != null){
                createView.close();
            }
            if(selectTotalFromBudgets != null){
                selectTotalFromBudgets.close();
            }
            if(selectDetailsByCategory != null){
                selectDetailsByCategory.close();
            }
            if(updateDetails != null){
                updateDetails.close();
            }
            if(updateBudgets != null){
                updateBudgets.close();
            }
            if(deleteItemFromDetails != null){
                deleteItemFromDetails.close();
            }
        } catch (SQLException e){
            System.out.println("Error: couldn't close the database: " + e.getMessage());
        }
    }

    public void initializeDB(){
        try {
            dropTableIfExists = conn.prepareStatement(DROP_TABLE + TABLE_MONTHS);
            createTableDetails = conn.prepareStatement(CREATE_TABLE_DETAILS);
            createTableBudgets = conn.prepareStatement(CREATE_TABLE_BUDGETS);
            createTableMonths = conn.prepareStatement(CREATE_TABLE_MONTHS);
            //create table months;
            dropTableIfExists.execute();
            createTableMonths.execute();
            //create table budgets
            dropTableIfExists = conn.prepareStatement(DROP_TABLE + TABLE_BUDGETS);
            dropTableIfExists.execute();
            createTableBudgets.execute();
            //create table details
            dropTableIfExists = conn.prepareStatement(DROP_TABLE + TABLE_DETAILS);
            dropTableIfExists.execute();
            createTableDetails.execute();

            System.out.println("DB initialized successfully");
        } catch (SQLException e){
            System.out.println("Error: couldn't initialize tables: " + e.getMessage());
        }
    }

    public void initializeViewTable(){
        try{
            createView.execute();
        } catch (SQLException e){
            System.out.println("Error: couldn't create a view table: " + e.getMessage());
        }
    }

    //this method is used to prepare initial records for TABLE_MONTHS and TABLE_BUDGETS
    //Use it only when each table doesn't have any record
    public void initializeMonthsAndBudgets(){
        try{
            //prepare SQL INSERT statement
            insertIntoMonths = conn.prepareStatement(INSERT_NEW_MONTH, Statement.RETURN_GENERATED_KEYS);
            //disable Auto-Commit
            conn.setAutoCommit(false);

            for (String month : MONTH_STR){
                int monthID = -1;
                //Insert a new record into MONTHS table
                monthID = insertNewMonth(month, monthID);

                //Insert a new record into BUDGETS table
                if(monthID != -1){
                    initializeBudgetsTable(monthID);
                } else {
                    System.out.println("monthID is invalid: " + monthID);
                    return;
                }
            }
            //commit the change
            conn.commit();

        } catch (SQLException e){
            try{
                conn.rollback();
            } catch (SQLException rollbackE) {
                System.out.println("Error: Rollback exception: " + rollbackE.getMessage());
            }
            System.out.println("Error: Table initialization failed: " + e.getMessage());
        } finally {
            try{
                //enable Auto-Commit
                conn.setAutoCommit(true);
            } catch (SQLException e){
                System.out.println("Error: something went wrong when it tries to enable Auto-Commit"
                + e.getMessage());
            }
        }
    }


    private void initializeBudgetsTable(int monthID){
        //Create a record of each category with appropriate month_id reference
        //initially goal and spent are set to 0
        for(String category : CATEGORY){
            int budgetID = insertNewBudget(category, 0, 0, monthID);
            insertNewDetails("undefined", 0, budgetID);
        }
    }

    private int insertNewMonth(String month, int monthID) throws SQLException {
        insertIntoMonths.setString(1, month);
        int affectedRow = insertIntoMonths.executeUpdate();
        if(affectedRow != 1 ){
            throw new SQLException("Error: A record is not inserted yet, affectedRow = " + affectedRow);
        } else {
            System.out.println("New record created: " + month);
            ResultSet generatedKey = insertIntoMonths.getGeneratedKeys();
            if(generatedKey.next()){
                monthID =generatedKey.getInt(1);
            } else {
                throw new SQLException("Couldn't retrieve Month ID");
            }
        }
        return monthID;
    }


    public int insertNewBudget(String category, int goal, int spent, int month){

        int id = -1;

        try{
            //prepare the statement. Set each parameter to the appropriate "?" parameter
            insertIntoBudgets = conn.prepareStatement(INSERT_NEW_BUDGET);
            insertIntoBudgets.setString(1, category);
            insertIntoBudgets.setInt(2, goal);
            insertIntoBudgets.setInt(3, spent);
            insertIntoBudgets.setInt(4, month);
            //execute the insert sql statement
            int affectedRow = insertIntoBudgets.executeUpdate();
            if(affectedRow == 1){
                System.out.println("New Budget record is created");
                ResultSet generatedKey = insertIntoBudgets.getGeneratedKeys();
                if(generatedKey.next()){
                    id = generatedKey.getInt(1);
                }
            } else {
                System.out.println("insert failed");
            }

        } catch (SQLException e){
            System.out.println("Error: Couldn't create new Budget record: " + e.getMessage());
        }

        return id;
    }

    public int insertNewDetails(String name, int amount, int budgetID){
        int id = -1;
        try{
            insertIntoDetails = conn.prepareStatement(INSERT_NEW_DETAILS);
            insertIntoDetails.setString(1, name);
            insertIntoDetails.setInt(2, amount);
            insertIntoDetails.setInt(3, budgetID);

            int affectedRecord= insertIntoDetails.executeUpdate();
            if(affectedRecord == 1){
                System.out.println("New Detail record is created: " + name);
                ResultSet generatedKey = insertIntoDetails.getGeneratedKeys();
                if(generatedKey.next()){
                    id = generatedKey.getInt(1);
                }
            } else {
                System.out.println("no new record created");
            }

            return id;
        } catch (SQLException e){
            System.out.println("Error: Couldn't create new Detail record: " + e.getMessage());
            return -1;
        }
    }

    public void setInsertNewDetails(String name, int budgetID){
        try{
            insertIntoDetails.setString(1, name);
            insertIntoDetails.setInt(2, 0);
            insertIntoDetails.setInt(3, budgetID);
        } catch (SQLException e){
            System.out.println("Error: Couldn't create new Detail record: " + e.getMessage());
        }
    }


    public ObservableList<MonthlyBudget> getBudgetsAlt(){
        ObservableList<MonthlyBudget> budgets = FXCollections.observableArrayList();
        try{


            for (int i = 1; i <= 12; i++){
                List<BudgetCategory> categories = new ArrayList<>();
                //records of budgets for each month and create MonthlyBudget instance with them
                selectBudgetsByMO.setInt(1, i);
                //this query is supposed to return 6 records (food ~ saving) for each month
                ResultSet resultSet = selectBudgetsByMO.executeQuery();
                while (resultSet.next()){
                    int id = resultSet.getInt(COLUMN_BUDGETS_ID);//id of a record
                    String category = resultSet.getString(COLUMN_BUDGETS_CATEGORY);
                    int goal = resultSet.getInt(COLUMN_BUDGETS_GOAL);
                    int spent = getAmountTotal(id);
                    ObservableMap<String, BreakdownItem> items = getBreakdownItemsFromDB(id);
                    BudgetCategory bc = new BudgetCategory(id, category, goal, spent, items);
                    categories.add(bc);
                }
                if( !categories.isEmpty()){
                    MonthlyBudget budget = new MonthlyBudget(i, categories, MONTH[i - 1]);
                    budgets.add(budget);
                } else {
                    System.out.println("Error: getBudgets is not worknig. List categories is empty");

                }
            }
        } catch (SQLException e){
            System.out.println("Error: failed to fetch data: " + e.getMessage());
        }
        return budgets;
    }

    private ObservableMap<String, BreakdownItem> getBreakdownItemsFromDB(int categoryID){
        ObservableMap<String, BreakdownItem> map = FXCollections.observableHashMap();
        try{
            selectDetailsByCategory.setInt(1, categoryID);
            //retrieve records from details table
            ResultSet result = selectDetailsByCategory.executeQuery();
            while(result.next()){
                //create BreakdownItem instance with the data, and put it in the map variable
                int id = result.getInt(COLUMN_DETAILS_ID);
                String name = result.getString(COLUMN_DETAILS_NAME);
                int amount = result.getInt(COLUMN_DETAILS_AMOUNT);
                BreakdownItem item = new BreakdownItem(id, name, amount, categoryID);
                map.put(item.getName(), item);
            }
        } catch (SQLException e){
            System.out.println("Error: couldn't get details record: " + e.getMessage());
        }
        if(map.isEmpty()){
            System.out.println("Error: Map is empty. Breakdown items are not created from DB");
        }
        return map;
    }

    //take _id of a category
    //find all details that belong to the category
    //return the total of amount
    public int getAmountTotal(int categoryID){
        try{
            selectTotalFromBudgets.setInt(1, categoryID);
            ResultSet result = selectTotalFromBudgets.executeQuery();
            if(result.next()){
                int total =(int) result.getFloat(1);
                System.out.println("total " + total);
                return total;
            }
        } catch (SQLException e) {
            System.out.println("Error: couldn't get total from details: " + e.getMessage());
        }

        return 0;
    }

    public void updateDetails(int amount, int id){
        try{
            updateDetails.setInt(1, amount);
            updateDetails.setInt(2, id);
            updateDetails.execute();
            System.out.println("The details table is updated!");
        } catch (SQLException e){
            System.out.println("Error: couldn't update the details table: " + e.getMessage());
        }
    }


    public void updateBudgets(int goal, int id) {
        try{
            updateBudgets.setInt(1, goal);
            updateBudgets.setInt(2, id);
            if(updateBudgets.executeUpdate() == 1){
                System.out.println("The budgets table is updated!");
            } else {
                System.out.println("the budgets table is not updated");
            }

        } catch (SQLException e){
            System.out.println("Error: couldn't update the budgets table: " + e.getMessage());
        }
    }

    public void deleteItemFromDetails(int id){
        try {
            deleteItemFromDetails.setInt(1, id);
            deleteItemFromDetails.execute();
        } catch (SQLException e){
            System.out.println("Error: couldn't delete item: " + e.getMessage());
        }
    }

    public int getDeletedItemsCategoryID(int id){
        try{
            String sql = " SELECT " + COLUMN_DETAILS_CATEGORY + " FROM " + TABLE_DETAILS + " WHERE " + COLUMN_DETAILS_ID + " = ? ";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if(set.next()){
              return set.getInt(COLUMN_DETAILS_ID);
            } else {
                return -1;
            }
        } catch (SQLException  e){
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }
}
